package de.fhg.aisec.secunity.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.beans.Institution;
import de.fhg.aisec.secunity.db.TripleStore;

@Path("institution")
public class InstitutionResource {
	private TripleStore db;
	
	public void InstitutionResource() {
		System.out.println("Constructor of bean " + this.toString());
		db = TripleStore.getInstance();
	}

    @GET
    @Path("institution/{institution}")
    @Produces(MediaType.APPLICATION_JSON)
	public Institution getInstitution(@PathParam("institution") String institution) {
		//TODO get all attributes of an institution from triple store
    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(institution, null, null, true);
    	while (res.hasNext()) {
    		Statement stmt = res.next();
    		Resource s = stmt.getSubject();
    		IRI p = stmt.getPredicate();
    		Value o = stmt.getObject();
    		
    		//TODO convert RDF to JSON
    	}
    	//Response.ok().entity()
    	return null;
	}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response createInstitution(Map<String, String> data) {
    	// Check if key "name" is there. If not --> BAD REQUEST
    	if (!data.containsKey("name") || data.get("name")==null) {
    		return Response.status(Response.Status.BAD_REQUEST).entity("name missing").build();
    	}
    	
    	// Check if there is already an institution with that "name" in db. If true, return error:
    	RepositoryResult<Statement> results = TripleStore.getInstance().getTriples(data.get("name"), null, null, false);
    	if (results.hasNext()) {
    		Response.status(Response.Status.CONFLICT).entity("name already exist");
    	}
    	
    	//Otherwise, create an institution with that name and create triples of the form
    	// <institution> <key> <value>
    	TripleStore.getInstance().addTriple(data.get("name"), "rdf:a", "Institution", false);
    	for (String predicate:data.keySet()) {
    		if (!predicate.equals("name")) {
    			String object = data.get(predicate);
    			TripleStore.getInstance().addTriple(data.get("name"), predicate, object, false);
    		}
    	}
    	
    	// Return ok
    	return Response.ok().build();
	}
}
