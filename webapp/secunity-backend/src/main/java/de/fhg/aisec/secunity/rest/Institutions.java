package de.fhg.aisec.secunity.rest;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("institutions") //TODO support filters
public class Institutions {

    @GET
    @Produces("application/json; charset=UTF-8")
	public Response getInstitutions(@QueryParam(value = "limit") String limit) {
		HashSet<String> data = new HashSet<String>();

		// build SPARQL query
		StringBuilder sb = new StringBuilder();
		for (String ns: TripleStore.getInstance().getNamespaces()) {
			sb.append("PREFIX ");
			sb.append(TripleStore.getInstance().getPrefix(ns));
			sb.append(": <");
			sb.append(ns);
			sb.append(">\n");
		}
		sb.append("SELECT ?s ?p ?o ");
		sb.append("WHERE { ");
		sb.append("  ?s rdf:type su:Organisation ;");
		sb.append("} ");
		sb.append("ORDER BY DESC(?s)");
		if (limit != null) {
			sb.append(" LIMIT " + limit);
		}		
		String query = sb.toString();
		System.out.println(query);
		List<BindingSet> res = TripleStore.getInstance().querySPARQLTuples(query, false);
		for (BindingSet bs:res) {
			String institution = bs.getValue("s").stringValue();
			data.add(institution);
		}
		
//		//get all institutions from triple store
//    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(null,  RDF.TYPE, TripleStore.getInstance().toEntity("su:Organisation"), false);
//    	while (res.hasNext()) {
//    		Statement stmt = res.next();
//    		Resource s = stmt.getSubject();
//    		if (s instanceof IRI) {
//    			String prefix = TripleStore.getInstance().getPrefix(((IRI)s).getNamespace());
//    			if (prefix !=null) {
//    				data.add(prefix + ":" + ((IRI)s).getLocalName());
//    			} else {
//    				data.add(s.stringValue());
//    			}
//    		} else {
//    			throw new RuntimeException("Unsupported type " + s.getClass());
//    		}
//    	}
    	return Response.ok().entity(Entity.json(data)).build();    	
	}
}
