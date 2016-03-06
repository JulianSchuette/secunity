package de.fhg.aisec.secunity.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("institution/{institution}")
public class Institution {

    @GET
    @Produces("application/json; charset=UTF-8")
	public Response getInstitution(@PathParam("institution") String institution) {
		HashMap<String, String> data = new HashMap<String, String>();

		//get all attributes of an institution from triple store
    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(institution, (String) null, (String) null, true);
    	while (res.hasNext()) {
    		Statement stmt = res.next();
    		IRI p = stmt.getPredicate();
    		Value o = stmt.getObject();
    		//Strip off namespace, if any
    		String object;
    		if (o instanceof Literal) {
    			object = ((Literal) o).getLabel();
    		} else if (o instanceof IRI) {
    			object = ((IRI) o).getLocalName();
    		} else {
    			throw new RuntimeException("Unexpected type " + o.getClass());
    		}
    		data.put(p.getLocalName(), object);
    	}
    	HashMap<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
    	result.put(institution, data);
    	return Response.ok().entity(Entity.json(result)).build();    	
	}

    @POST
    @Consumes("application/json; charset=UTF-8")
	public Response createInstitution(@PathParam("institution") String institution, Map<String, String> data) {
    	// Check if there is already an institution with that name in db. If true, return error:
    	RepositoryResult<Statement> results = TripleStore.getInstance().getTriples(institution, RDF.TYPE, TripleStore.getInstance().toEntity("Institution"), false);
    	if (results.hasNext()) {
    		return Response.status(Response.Status.CONFLICT).entity("institution already exist").build();
    	}
    	
    	//Otherwise, create an institution with that name and create triples of the form
    	// <institution> <key> <value>
    	TripleStore.getInstance().addTriple(institution, RDF.TYPE, TripleStore.getInstance().toEntity("Institution"), false);
    	for (String predicate:data.keySet()) {
			String object = data.get(predicate);
			System.out.println(institution + " " + predicate + " " + object);
			TripleStore.getInstance().addTriple(institution, predicate, object, false);
    	}
    	
    	// Return ok
    	return Response.ok().build();
	}
}
