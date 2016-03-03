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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("person/{person}")
public class Person {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getPerson(@PathParam("person") String person) {
		HashMap<String, String> data = new HashMap<String, String>();

		//get all attributes of a person from triple store
    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(person, null, null, true);
    	while (res.hasNext()) {
    		Statement stmt = res.next();
    		IRI p = stmt.getPredicate();
    		Value o = stmt.getObject();
    		if (o instanceof Literal) {
    			data.put(p.getLocalName(), ((Literal) o).getLabel());
    		} else if (o instanceof IRI) {
    			data.put(p.getLocalName(), ((IRI) o).getLocalName());
    		} else {
    			System.err.println("Do not know what to do with object of type " + o.getClass());
    		}
    	}
    	HashMap<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
    	result.put(person, data);
    	return Response.ok().entity(Entity.json(data)).build();    	
	}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response createPerson(@PathParam("person") String person, Map<String, String> data) {
    	// Check if there is already a person with that name in db. If true, return error:
    	RepositoryResult<Statement> results = TripleStore.getInstance().getTriples(person, "rdf:a", "Person", false);
    	if (results.hasNext()) {
    		Response.status(Response.Status.CONFLICT).entity("person already exist");
    	}
    	
    	//Otherwise, create a person with that name and create triples of the form
    	// <person> <key> <value>
    	TripleStore.getInstance().addTriple(person, "rdf:a", "Person", false);
    	for (String predicate:data.keySet()) {
			String object = data.get(predicate);
			TripleStore.getInstance().addTriple(person, predicate, object, false);
    	}
    	
    	// Return ok
    	return Response.ok().build();
	}
}
