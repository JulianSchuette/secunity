package de.fhg.aisec.secunity.rest;

import java.util.HashMap;
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
		HashMap<String, HashMap<String, String>> institutions = new HashMap<String, HashMap<String, String>>();

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
		sb.append("  ?s rdf:type su:Organisation .");
		sb.append("  ?s ?p ?o ");
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
			institution = TripleStore.getInstance().cutOffNamespace(institution);
			if(!institutions.containsKey(institution)){
				institutions.put(institution, new HashMap<String, String>());
			}
			HashMap<String, String> instInfo = institutions.get(institution);
			instInfo.put(TripleStore.getInstance().cutOffNamespace(bs.getValue("p").stringValue()),
					TripleStore.getInstance().cutOffNamespace(bs.getValue("o").stringValue()));
		}
    	return Response.ok().entity(Entity.json(institutions)).build();
	}
}
