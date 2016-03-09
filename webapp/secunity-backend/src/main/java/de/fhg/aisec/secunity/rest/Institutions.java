package de.fhg.aisec.secunity.rest;

import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("institutions") //TODO support filters
public class Institutions {

    @GET
    @Produces("application/json; charset=UTF-8")
	public Response getInstitutions() {
		HashSet<String> data = new HashSet<String>();

		//get all institutions from triple store
    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(null,  RDF.TYPE, TripleStore.getInstance().toEntity("akt:Organization"), false);
    	while (res.hasNext()) {
    		Statement stmt = res.next();
    		Resource s = stmt.getSubject();
    		if (s instanceof IRI) {
    			String prefix = TripleStore.getInstance().getPrefix(((IRI)s).getNamespace());
    			if (prefix !=null) {
    				data.add(prefix + ":" + ((IRI)s).getLocalName());
    			} else {
    				data.add(((IRI)s).getLocalName());
    			}
    		} else {
    			throw new RuntimeException("Unsupported type " + s.getClass());
    		}
    	}
    	return Response.ok().entity(Entity.json(data)).build();    	
	}
}
