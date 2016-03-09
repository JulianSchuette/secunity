import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import de.fhg.aisec.secunity.db.TripleStore;
import de.fhg.aisec.secunity.rest.Institution;
import de.fhg.aisec.secunity.rest.Person;

public class BackendTest extends JerseyTest {

	    @Override
	    protected Application configure() {
	    	return new ResourceConfig(Institution.class, Person.class);
	    }

	    @Before
	    public void cleanDB() {   	
	    	// Clear database
	    	System.out.println("Cleaning database");
	    	TripleStore.getInstance().deleteAll(true);
	    }
	    
	    @Test
	    public void testCreateInstitution() throws UnsupportedEncodingException {
	    	// Create new entry
	    	Map<String, String> data = new HashMap<String, String>();
	    	data.put("city", "Garching near Munich");
	    	data.put("topic", "Hardware Security");
	    	data.put("topic", "Software Security");
	        Response resp = target(URLEncoder.encode("institution/Fraunhofer AISEC", "UTF-8")).request().post(Entity.json(data), Response.class);
	        assertEquals(200, resp.getStatus());
	        
	        // Retrieve entry
	        resp = target("institution/Fraunhofer AISEC").request().get(Response.class);
	        System.out.println(resp.getEntity().toString());
	        assertEquals(200, resp.getStatus());

	    }
	
	    @Test
	    public void testCreatePerson() {
	    	// Create new entry
	    	Map<String, String> data = new HashMap<String, String>();
	    	data.put("affiliation", "Fraunhofer AISEC");
	        Response resp = target("person/Julian Schütte").request().post(Entity.json(data), Response.class);
	        assertEquals(200, resp.getStatus());
	        
	        // Retrieve entry
	        resp = target("person/Julian Schütte").request().get(Response.class);
	        System.out.println(resp.getEntity().toString());
	        assertEquals(200, resp.getStatus());
	        System.out.println(resp.getEntity());
	    }
}