package de.fhg.aisec.secunity.db;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
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

/**
 * Facade for triple store backend.
 * 
 * @author Julian Schuette (julian.schuette@aisec.fraunhofer.de)
 *
 */
public class TripleStore {
	private static final String NAMESPACE = "http://secunity/";
	private static TripleStore instance = null;
	private HTTPRepository repo = null;

	public static TripleStore getInstance() {
		if (instance == null) {
			instance = new TripleStore();
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
	    if (manager.getRepository(repoID)==null) {
	    	//Create repo, if not there
	    	System.out.println("Creating repository " + repoID);
	    	createRemoteRepository(manager, repoID);
	    }
		
	    //Connect to repo.
		repo = new HTTPRepository(url.toString(), repoID);
		repo.initialize();
			
		return repo;
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
		IRI s = subject!=null?f.createIRI(NAMESPACE, subject):null;
		IRI p = predicate!=null?f.createIRI(NAMESPACE, predicate):null;
		Value o = object!=null?f.createLiteral(object):null;
		return repo.getConnection().getStatements(s, p, o, includeInferred);
	}
	
	public void addTriple(String subject, String predicate, String object, boolean isLiteral) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(NAMESPACE, subject);
		IRI p = f.createIRI(NAMESPACE, predicate);
		Statement stmt;
		if (isLiteral) {
			Literal o = f.createLiteral(object);
			stmt = f.createStatement(s, p, o);
		} else {
			IRI o = f.createIRI(NAMESPACE, object);
			stmt = f.createStatement(s, p, o);
		}
		repo.getConnection().add(stmt);
	}
	
	public void addTripleLiteral(String subject, String predicate, int object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(NAMESPACE, subject);
		IRI p = f.createIRI(NAMESPACE, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}
	
	public void addTripleLiteral(String subject, String predicate, Date object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(NAMESPACE, subject);
		IRI p = f.createIRI(NAMESPACE, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}
	
	public void addTripleLiteral(String subject, String predicate, boolean object) {
		ValueFactory f = repo.getValueFactory();
		IRI s = f.createIRI(NAMESPACE, subject);
		IRI p = f.createIRI(NAMESPACE, predicate);
		Literal o = f.createLiteral(object);
		Statement stmt = f.createStatement(s, p, o);
		repo.getConnection().add(stmt);
	}
	
}