package de.fhg.aisec.secunity.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.fhg.aisec.secunity.beans.Address;
import de.fhg.aisec.secunity.beans.Institution;

@Path("institution")
public class InstitutionResource {

    @GET()
    @Produces(MediaType.APPLICATION_JSON)
	public Institution getInstitution() {
		//TODO Get attributes of an institution from TripletStore and return as JSON
    	return new Institution("bla", new Address("blubb"));
	}
}
