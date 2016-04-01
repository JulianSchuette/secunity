package de.fhg.aisec.secunity.rest;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.openrdf.query.BindingSet;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("institutions") //TODO support filters
public class Institutions {

    @GET
    @Produces("application/json; charset=UTF-8")
	public Response getInstitutions(@QueryParam(value = "limit") String limit, @QueryParam(value = "offset") String offset) {
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
		sb.append("SELECT ?s ");
		sb.append("WHERE { ");
		sb.append("  ?s rdf:type su:Organisation .");
		sb.append("} ");
		sb.append("ORDER BY ASC(?s)");
		if (limit != null) {	// Enforce max. limit even when not given
			sb.append(" LIMIT " + limit);
		}
		if (limit != null && offset !=null) {
			sb.append(" OFFSET " + offset);
		}
		String query = sb.toString();
		List<BindingSet> res = TripleStore.getInstance().querySPARQLTuples(query, false);
		for (BindingSet bs:res) {
			String institution = bs.getValue("s").stringValue();
			data.add(institution);
		}
		
    	return Response.ok().entity(Entity.json(data)).build();    	
	}
}
