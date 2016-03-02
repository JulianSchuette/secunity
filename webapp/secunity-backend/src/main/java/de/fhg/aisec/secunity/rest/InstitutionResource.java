package de.fhg.aisec.secunity.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryResult;

import de.fhg.aisec.secunity.beans.Institution;
import de.fhg.aisec.secunity.db.TripleStore;
import info.aduna.iteration.Iterations;

@Path("institution")
public class InstitutionResource {
	private TripleStore db;
	
	public void InstitutionResource() {
		System.out.println("Constructor of bean " + this.toString());
		db = TripleStore.getInstance();
		try {
			db.connect(new URL("http://localhost:8081/openrdf-sesame"), "secunity");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

    @GET
    @Path("institution/{institution}")
    @Produces(MediaType.APPLICATION_JSON)
	public Institution getInstitution(@PathParam("institution") String institution) {
		//TODO get all attributes of an institution from triple store
    	RepositoryResult<Statement> res = TripleStore.getInstance().getTriples(institution, null, null, true);
    	System.out.println(Iterations.asList(res).toString()); //TODO !
    	return null;
	}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
	public Response createInstitution(Map<String, String> data) {
    	//TODO check if key "id" is there
    	
    	//TODO check if there is already an institution with that "name" in db. If true, return error:
    	//Response.status(Response.Status.CONFLICT).entity("name already exist");
    	
    	//TODO Otherwise, create an institution with that name and create triples of the form
    	// <institution> <key> <value>
    	
    	// Return ok
    	return Response.ok().build();
	}
}
