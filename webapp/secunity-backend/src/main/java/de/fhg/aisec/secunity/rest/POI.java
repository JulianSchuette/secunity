package de.fhg.aisec.secunity.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

@Path("POI/")
public class POI {

    @GET
    @Produces("plain/text; charset=UTF-8")
	public Response getPOI(@QueryParam("z") int zoom, @QueryParam("l") int left, @QueryParam("t") int top, @QueryParam("r") int right, @QueryParam("b") int bottom) {
    	String poiList = "";
    	return Response.ok().entity(Entity.text(poiList)).build();    	
	}
}
