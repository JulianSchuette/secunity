package de.fhg.aisec.secunity.db;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openrdf.IsolationLevels;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Namespace;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.BooleanQuery;
import org.openrdf.query.Query;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.resultio.BooleanQueryResultFormat;
import org.openrdf.query.resultio.BooleanQueryResultWriter;
import org.openrdf.query.resultio.BooleanQueryResultWriterRegistry;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.helpers.QueryResultCollector;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.sail.config.SailRepositoryConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.inferencer.fc.config.DirectTypeHierarchyInferencerConfig;
import org.openrdf.sail.memory.config.MemoryStoreConfig;

import info.aduna.iteration.Iterations;

/**
 * Facade for triple store backend.
 *
 * @author Julian Schuette (julian.schuette@aisec.fraunhofer.de)
 *
 */
public class TripleStore {
	private static final Logger log = Logger.getLogger(TripleStore.class.getName());
	public static final String DB_URL = "http://db:8080/openrdf-sesame"; 
	public static final String DEFAULT_NS = "http://secunity/";
	private static TripleStore instance = null;
	private static HTTPRepository repo = null;
	private ExecutorService executor = Executors.newFixedThreadPool(10);

	public static TripleStore getInstance() {
		if (instance == null) {
			instance = new TripleStore();
			try {
				//TODO make URL and dbID configurable
				log.info("Creating new DB connection to " + DB_URL);
				Repository r = instance.connect(new URL(DB_URL), "secunity");
				log.info("Connection success: " + r.getConnection().isOpen());
			} catch (Throwable e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return instance;
	}

	/**
		Connect to remote backend.
		@param url http://example.org/openrdf-sesame/
		@param repoID example-db
	*/
	public Repository connect(URL url, String repoID) {
		// Use RemoteRepoManager to check if repo exists
	    RemoteRepositoryManager manager = new RemoteRepositoryManager(url.toString());
	    manager.initialize();
	    boolean repoExists = manager.getRepository(repoID)!=null;
	    if (!repoExists) {
	    	//Create repo, if not there
	    	log.info("Creating repository " + repoID);
	    	createRemoteRepository(manager, repoID);
	    }

	    //Connect to repo.
		repo = new HTTPRepository(url.toString(), repoID);
		repo.initialize();

		// setNamespace is super slow for large data sets
		repo.getConnection().setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		repo.getConnection().setNamespace("akt", "http://www.aktors.org/ontology/portal#");
		repo.getConnection().setNamespace("akts", "http://www.aktors.org/ontology/support#");
		repo.getConnection().setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		repo.getConnection().setNamespace("su", DEFAULT_NS);
		
		if (!repoExists) {
	    	try {
				loadInitialData();
			} catch (IOException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return repo;
	}

	private void loadInitialData() throws IOException {
		log.info("Importing initial datasets from " + new File(".").getAbsolutePath());
		Enumeration<URL> en=getClass().getClassLoader().getResources("datasets");

		while (en.hasMoreElements()) {
			URL url = en.nextElement();
			for (File f: new File(url.getPath()).listFiles()) {
				try {
					String ext = f.getName().substring(f.getName().lastIndexOf(".")+1);
					switch (ext.toLowerCase()) {
					case "ttl":
						repo.getConnection().add(f, DEFAULT_NS, RDFFormat.TURTLE);
						break;
					case "rdf":
						repo.getConnection().add(f, DEFAULT_NS, RDFFormat.RDFXML);
						break;
					case "n3":
						repo.getConnection().add(f, DEFAULT_NS, RDFFormat.N3);
					}
				} catch (Throwable e) {
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}

	public Repository getRepository() {
		return repo;
	}

	/**
	 * Creates a repository in a remote OpenRDF server.
	 *
	 * @param manager
	 */
	private void createRemoteRepository(RemoteRepositoryManager manager, String repoID) {
	    //Configure new repo
		RepositoryConfig config = new RepositoryConfig();
	    config.setID(repoID);

	    // We make it a normal SAIL (Storage and Inference Layer) repo
	    SailRepositoryConfig epConfig = new SailRepositoryConfig();

	    // We set inference to direct types only, for performance reasons
	    DirectTypeHierarchyInferencerConfig cType = new DirectTypeHierarchyInferencerConfig();

	    // We want the repo to be persisted
	    MemoryStoreConfig memConf = new MemoryStoreConfig();
	    memConf.setPersist(true);
	    cType.setDelegate(memConf);
	    epConfig.setSailImplConfig(cType);
	    config.setRepositoryImplConfig(epConfig);

	    // Give it a title and set it up.
	    config.setTitle("Secunity repo");
	    config.validate();
	    manager.addRepositoryConfig(config);
	    log.info("Repository created: " + repoID);
	}

	/**
	 * Import RDF from some RDF/XML file into triple store.
	 *
	 * @param url
	 * @throws RDFParseException
	 * @throws RepositoryException
	 * @throws IOException
	 */
	public void importRDF(URL url) throws RDFParseException, RepositoryException, IOException {
		repo.getConnection().add(url, url.toString(), RDFFormat.RDFXML);
	}

	public String getPrefix(String namespace) {
		for ( Namespace ns: Iterations.asList(repo.getConnection().getNamespaces())) {
			if (ns.getName().equals(namespace)) {
				return ns.getPrefix();
			}
		}
		return null;
	}

	public List<String> getNamespaces() {
		List<String> results = new ArrayList<String>();
		RepositoryResult<Namespace> ns = repo.getConnection().getNamespaces();
		for (Namespace n: Iterations.asList(ns)) {
			results.add(n.getName());
		}
		return results;
	}

	public String getNamespace(String prefix) {
		return repo.getConnection().getNamespace(prefix);
	}

	public IRI toEntity(String literal) {
		ValueFactory f = repo.getValueFactory();
		if (literal.contains(":") && !literal.contains("://")) {
			String[] parts = literal.split(":");
			return f.createIRI(repo.getConnection().getNamespace(parts[0]), parts[1]);
		} else if (!literal.contains("://")) {
			return f.createIRI(DEFAULT_NS, literal);
		} else {
			return f.createIRI(literal);
		}
	}

	/**
	 * Queries triple store and returns results.
	 *
	 * Depending on the type of the query the results will be as follows:
	 *
	 * TupleQuery:		JSON
	 * GraphQuery:		N-Triples
	 * BooleanQuery:	Plain text
	 *
	 * @param query
	 * @param includeInferred
	 * @return
	 */
	public List<BindingSet> querySPARQLTuples(String query, boolean includeInferred) {
		Query q = repo.getConnection().prepareQuery(query);
		q.setIncludeInferred(includeInferred);

		if (!(q instanceof TupleQuery)) {
			throw new IllegalArgumentException("SPARQL query is not a tuple query.");
		}

		QueryResultCollector resultCollector = new QueryResultCollector();
		((TupleQuery) q).evaluate(resultCollector);
		return resultCollector.getBindingSets();
	}

	public String querySPARQLBoolean(String query, boolean includeInferred) {
		Query q = repo.getConnection().prepareQuery(query);
		q.setIncludeInferred(includeInferred);
		ByteArrayOutputStream response = new ByteArrayOutputStream();

		if (!(q instanceof BooleanQuery)) {
			throw new IllegalArgumentException("SPARQL query is not a boolean query.");
		}

		final BooleanQueryResultWriterRegistry r = BooleanQueryResultWriterRegistry.getInstance();
		final BooleanQueryResultWriter writer = QueryResultIO.createBooleanWriter(BooleanQueryResultFormat.TEXT, response);
		writer.handleBoolean(((BooleanQuery) q).evaluate());
		return response.toString();
	}

	/**
	 * Get triples from store which match given subject, predicate, object.
	 *
	 * null is a wildcard.
	 * includeInferred=true will additionally return inferred triples, if the underlying store supports inference.
	 *
	 * @param subject
	 * @param predicate
	 * @param object
	 * @param includeInferred
	 * @return
	 */
	public RepositoryResult<Statement> getTriples(String subject, String predicate, String object, boolean includeInferred) {
		ValueFactory f = repo.getValueFactory();
		IRI s = subject!=null?f.createIRI(DEFAULT_NS, subject):null;
		IRI p = predicate!=null?f.createIRI(DEFAULT_NS, predicate):null;
		Value o = object!=null?f.createLiteral(object):null;
		return repo.getConnection().getStatements(s, p, o, includeInferred);
	}

	public RepositoryResult<Statement> getTriples(String subject, IRI predicate, String object, boolean includeInferred) {
		ValueFactory f = repo.getValueFactory();
		IRI s = subject!=null?f.createIRI(DEFAULT_NS, subject):null;
		Value o = object!=null?f.createLiteral(object):null;
		return repo.getConnection().getStatements(s, predicate, o, includeInferred);
	}

	public RepositoryResult<Statement> getTriples(String subject, IRI predicate, IRI object, boolean includeInferred) {
		ValueFactory f = repo.getValueFactory();
		IRI s = subject!=null?f.createIRI(DEFAULT_NS, subject):null;
		return repo.getConnection().getStatements(s, predicate, object, includeInferred);
	}

	public RepositoryResult<Statement> getTriples(IRI subject, String predicate, String object, boolean includeInferred) {
		ValueFactory f = repo.getValueFactory();
		IRI p = predicate!=null?f.createIRI(DEFAULT_NS, predicate):null;
		Value o = object!=null?f.createLiteral(object):null;
		RepositoryResult<Statement> stmts = repo.getConnection().getStatements(subject, p, o, includeInferred);
		repo.getConnection().close();
		return stmts;
	}
	
	/**
	 * Adds multiple triples in a single transaction. Use this method if you need to add more than one statement at a time.
	 * 
	 * @param triples
	 * @return 
	 */
	public CompletableFuture<Boolean> addTriples(List<Triple> triples) {
		final CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
			boolean success = true;
			try {
				RepositoryConnection con = repo.getConnection();
				con.begin(IsolationLevels.READ_UNCOMMITTED);
				triples.parallelStream().forEach(triple -> {
					log.fine("Committing " + triple.getSubject() + " - " + triple.getPredicate() + " - " + triple.getObject());
					addTriple(triple.getSubject(), triple.getPredicate(), triple.getObject());
				});
				con.commit();
				log.fine("Committed");
				con.close();
			} catch (Throwable t) {
				success = false;
				log.log(Level.SEVERE, t.getMessage(), t);
			}
		    return success;
		}, executor);
		return future;
	}
	
	public void addTriple(String subject, Value predicate, Value object) {
		if (predicate instanceof IRI && object instanceof IRI) {
			addTriple(subject, (IRI) predicate, (IRI) object, false);
		} else if (predicate instanceof IRI && object instanceof Literal) {
			addTriple(subject, (IRI) predicate, ((Literal) object).stringValue(), true);			
		} else if (predicate instanceof Literal && object instanceof Literal) {
			addTriple(subject, predicate.stringValue(), object.stringValue(), true);
		} else if (predicate instanceof IRI && object instanceof IRI) {
			addTriple(subject, predicate, object);
		} else {
			log.warning("Unexpected triplet type " + subject + " - " + predicate + " - " + object);
		}
	}


	public void addTriple(String subject, IRI predicate, IRI object, boolean isLiteral) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		Statement stmt = f.createStatement(s, predicate, object);
		repo.getConnection().add(stmt);
	}

	public void addTriple(String subject, IRI predicate, String object, boolean isLiteral) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		Statement stmt;
		if (isLiteral) {
			Literal o = f.createLiteral(object);
			stmt = f.createStatement(s, predicate, o);
		} else {
			IRI o = f.createIRI(DEFAULT_NS, object);
			stmt = f.createStatement(s, predicate, o);
		}
		repo.getConnection().add(stmt);
	}
	
	public void addTriple(String subject, String predicate, String object, boolean isLiteral) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		IRI p = null;
		if (predicate.contains(":") && !predicate.contains("://")) {
			String[] parts = predicate.split(":");
			p = f.createIRI(getNamespace(parts[0]), parts[1]);
		} else {
			p = f.createIRI(DEFAULT_NS, predicate);
		}
		Statement stmt;
		if (isLiteral) {
			Literal o = f.createLiteral(object);
			stmt = f.createStatement(s, p, o);
		} else {
			IRI o = f.createIRI(DEFAULT_NS, object);
			stmt = f.createStatement(s, p, o);
		}
		repo.getConnection().add(stmt);
	}

	public void addTripleLiteral(String subject, String predicate, int object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		IRI p = f.createIRI(DEFAULT_NS, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}

	public void addTripleLiteral(String subject, String predicate, Date object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		IRI p = f.createIRI(DEFAULT_NS, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}

	public void addTripleLiteral(String subject, String predicate, boolean object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(DEFAULT_NS, subject);
		IRI p = f.createIRI(DEFAULT_NS, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}

	/**
	 * Deletes all content from the database. This method is only for testing purposes. It will throw a RuntimeException if not called from a JUnit test.
	 * @param confirmation
	 */
	public void deleteAll(boolean deleteNamespaces) {
		boolean isTesting = false;
		for (StackTraceElement se: new Exception().getStackTrace()) {
			if (se.getClassName().startsWith("org.junit")) {
				isTesting = true;
			}
		}
		if (!isTesting) {
			throw new RuntimeException("Rejected attempt to delete database outside of JUnit test!");
		}

		// Delete statements
		repo.getConnection().clear();

		// Delete namespaces
		if (deleteNamespaces)
			Iterations.asList(repo.getConnection().getNamespaces()).forEach(x -> repo.getConnection().removeNamespace(x.getPrefix()));
	}


	public String replaceNamespace(String s){
		List<Namespace> nses = new ArrayList<Namespace>();
		Iterations.addAll(repo.getConnection().getNamespaces(), nses);
		for(Namespace ns: nses)
			if(s.startsWith(ns.getName()))
				return s.replace(ns.getName(), ns.getPrefix()+":");
		return s;
	}

	public String replacePrefix(String s){
		List<Namespace> nses = new ArrayList<Namespace>();
		Iterations.addAll(repo.getConnection().getNamespaces(), nses);
		for(Namespace ns: nses)
			if(s.startsWith(ns.getPrefix()))
				return s.replace(ns.getPrefix()+":", ns.getName());
		return s;
	}

	public String cutOffNamespace(String s){

		List<Namespace> nses = new ArrayList<Namespace>();
		Iterations.addAll(repo.getConnection().getNamespaces(), nses);
		for(Namespace ns: nses)
			if(s.startsWith(ns.getName()))
				return s.replace(ns.getName(), "");
		return s;
	}
}