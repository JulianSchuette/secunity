package de.fhg.aisec.secunity.db;

import java.net.MalformedURLException;
import java.net.URL;

import org.openrdf.model.IRI;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

/**
 * Just for testing purposes.
 * 
 * @author julian
 *
 */
public class Main {

	public static void main(String[] args) throws MalformedURLException {
		Repository repo = TripletStore.getInstance().connect(new URL("http://localhost:8081/openrdf-sesame"), "secunity");
		System.out.println(repo.isInitialized());
		
		ValueFactory f = repo.getValueFactory();
		Resource subject = f.createIRI("http://secunity/myInstitute");
		IRI object = f.createIRI("http://secunity/Address");
		RepositoryConnection conn = repo.getConnection();
		conn.add(subject, RDF.TYPE, object);
		conn.close();
	}
}
