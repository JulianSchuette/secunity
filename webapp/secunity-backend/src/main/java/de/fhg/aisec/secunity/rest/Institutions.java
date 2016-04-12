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
import org.openrdf.query.BindingSet;

import de.fhg.aisec.secunity.db.TripleStore;

@Path("institutions") //TODO support filters
public class Institutions {

    @GET
    @Produces("application/json; charset=UTF-8")
	public Response getInstitutions(@QueryParam(value = "limit") String limit, @QueryParam(value = "offset") String offset
			, @QueryParam(value = "wlocfirst") String withlocfirst) {
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
		// If this parameter is set the locations with lat or lng set are returned first so that the number of gecoding queries can
		// be reduced. But depending on the query this may be replaced when a filtering is used for the retrieval of the insstitution names
		if(withlocfirst != null){
			sb.append("SELECT ?s ");
			sb.append("WHERE { ");
			sb.append("  ?s rdf:type <"+TripleStore.DEFAULT_NS+"Organisation> .");
			sb.append("  OPTIONAL {?s su:loc_lat ?lat} .");
			sb.append("  OPTIONAL {?s su:loc_lng ?lng}");
			sb.append("} ");
			sb.append("ORDER BY DESC (?lat) DESC (?lng)");
		}else{
			sb.append("SELECT ?s ");
			sb.append("WHERE { ");
			sb.append("  ?s rdf:type <"+TripleStore.DEFAULT_NS+"Organisation> .");
			sb.append("} ");
			sb.append("ORDER BY ASC(?s)");
		}
		if (limit != null) {	// Enforce max. limit even when not given
			sb.append(" LIMIT " + limit);
		}
		if (limit != null && offset !=null) {
			sb.append(" OFFSET " + offset);
		}
		String query = sb.toString();
		System.out.println(query);
		List<BindingSet> res = TripleStore.getInstance().querySPARQLTuples(query, false);
		for (BindingSet bs:res) {
			if (bs.getValue("s") instanceof IRI) {
				IRI subjIRI = (IRI) bs.getValue("s");
				String institution = TripleStore.getInstance().getPrefix(subjIRI.getNamespace()) + ":" + subjIRI.getLocalName();
				data.add(institution);
			} else {
				System.out.println("Unkown type " + bs.getValue("s"));
			}
		}

    	return Response.ok().entity(Entity.json(data)).build();
	}
}
