import static org.junit.Assert.assertEquals;

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
import de.fhg.aisec.secunity.rest.InstitutionResource;

public class BackendTest extends JerseyTest {

	    @Override
	    protected Application configure() {
	        return new ResourceConfig(InstitutionResource.class);
	    }

	    @Before
	    public void cleanDB() {   	
	    	// Clear database
	    	TripleStore.getInstance().deleteAll();
	    }
	    
	    @Test
	    public void testCreate() {
	    	// Create new entry
	    	Map<String, String> data = new HashMap<String, String>();
	    	data.put("name", "Fraunhofer AISEC");
	    	data.put("city", "Garching near Munich");
	    	data.put("topic", "Hardware Security");
	    	data.put("topic", "Software Security");
	        Response resp = target("institution").request().post(Entity.json(data), Response.class);
	        assertEquals(200, resp.getStatus());
	        
	        // Retrieve entry
	        resp = target("institution/Fraunhofer AISEC").request().get(Response.class);
	        System.out.println(resp.getEntity().toString());
	        assertEquals(Response.Status.OK, resp.getStatus());

	    }
	
	
}
