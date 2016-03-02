import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import de.fhg.aisec.secunity.rest.InstitutionResource;

public class BackendTest extends JerseyTest {

	    @Override
	    protected Application configure() {
	        return new ResourceConfig(InstitutionResource.class);
	    }

	    @Test
	    public void testCreate() {
//	        String hello = target("institution/0").request().get(String.class);
	    	Map<String, String> data = new HashMap<String, String>();
	    	data.put("name", "Fraunhofer AISEC");
	    	data.put("city", "Garching near Munich");
	    	data.put("topic", "Hardware Security");
	    	data.put("topic", "Software Security");
	        String hello = target("institution").request().post(Entity.json(data), String.class);
	        System.out.println(hello);
	    }
	
}
