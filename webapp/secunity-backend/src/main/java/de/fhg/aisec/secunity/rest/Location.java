package de.fhg.aisec.secunity.rest;

import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.json.JSONArray;
import org.json.JSONException;




public class Location {

	private static String apiNominatim = "http://nominatim.openstreetmap.org/search/#address#?format=json";
	private static String apiGMaps = "http://maps.googleapis.com/maps/api/geocode/json?address=#address#&sensor=false";

	public static HashMap<String,String> queryLocation(HashMap<String, String> address){
		HashMap<String,String> ret = null;
		ret = queryOSMAPILoc(address);
		if(ret == null)
			ret = queryGoogleAPILoc(address);
		return ret;
	}

	public static HashMap<String,String> queryOSMAPILoc(HashMap<String, String> a){
		HashMap<String,String> ret = null;

		Client client = ClientBuilder.newClient();


		String address = prepareAdressForNominatim(a);
		System.out.println("Address: " + address);
		WebTarget resource = client.target(apiNominatim.replace("#address#", address));
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);

		Response response = request.get();

		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
		    System.out.println("Success! " + response.getStatus());
		    try{
			    JSONArray ja =new JSONArray(response.readEntity(String.class));
			    if(ja.length() >0){
			    	ret = new HashMap<String, String>();
			    	ret.put("loc_lat", ja.getJSONObject(0).getString("lat"));
			    	ret.put("loc_lng", ja.getJSONObject(0).getString("lon"));
			    }
		    }catch(JSONException jo){
		    	ret = null;
		    }
	    	return ret;
		} else {
		    System.out.println("ERROR! " + response.getStatus());
		   return null;
		}

	}

	public static HashMap<String,String> queryGoogleAPILoc(HashMap<String, String> address){
		HashMap<String,String> ret = null;
		return ret;
	}

	public static String prepareAdressForNominatim(HashMap<String, String> a){
		String address= "";

		String street = a.get("su:address_street");
		String city = a.get("su:address_city");
		String country = a.get("su:address_country");
		String postalcode = a.get("su:address_postcode");
		address += street != null ? street + ",+" : "";
		address += city != null ? city + ",+" : "";
		address += country != null ? country + ",+" : "";
			  //address += postalcode ? postalcode : ""

		if(address.endsWith(",+"))
			address = address.substring(0, address.length() - 2);

		address.replaceAll("%20", "+");
		address.replaceAll("\"", "");

		return address;
	}

	public static String prepareAdressForGmaps(HashMap<String, String> a){
		String address= "";

		String street = a.get("su:address_street");
		String city = a.get("su:address_city");
		String country = a.get("su:address_country");
		String postalcode = a.get("su:address_postcode");
		address += street != null ? street + ",+" : "";
		address += city != null ? city + ",+" : "";
		address += country != null ? country + ",+" : "";
		address += postalcode != null ? postalcode : "";

		if(address.endsWith(",+"))
			address = address.substring(0, address.length() - 2);

		address.replaceAll("%20", " ");
		address.replaceAll("\"", "");

		return address;
	}

}
