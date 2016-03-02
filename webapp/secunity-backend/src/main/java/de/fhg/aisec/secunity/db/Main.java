package de.fhg.aisec.secunity.db;

import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryResult;

/**
 * Just for testing purposes.
 * 
 * @author Julian Schuette (julian.schuette@aisec.fraunhofer.de)
 *
 */
public class Main {

	public static void main(String[] args) throws MalformedURLException {
		TripleStore.getInstance().connect(new URL("http://localhost:8081/openrdf-sesame"), "secunity-db");

		// Add simple triple
		TripleStore.getInstance().addTriple("myInstitute", "hasAddress", "someAddress", false);
		
		// Add weird triple
		TripleStore.getInstance().addTriple("my ! \" &amp; <bla>'Institute", "my ! \" &amp; <bla>'hasAddress", "my ! \" &amp; <bla>'\nsomeAddress", false);
		
		// Add triple with literal
		TripleStore.getInstance().addTriple("myInstitute", "hasAddress", "This is\n"
				+ "some multiline\n"
				+ "text.\n", true);
		
		// Get all triples from DB (null = wildcard)
		RepositoryResult<Statement> results = TripleStore.getInstance().getTriples(null,  null,  null,  false);
		
		// Print out triples
		while (results.hasNext()) {
			Statement stmt = results.next();
			System.out.println(stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject().stringValue());
		}
		
	}
}
