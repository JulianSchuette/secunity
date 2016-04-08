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
import org.json.JSONObject;




public class Location {

	private static String apiNominatim = "http://nominatim.openstreetmap.org/search/#address#?format=json";
	private static String apiGMaps = "https://maps.googleapis.com/maps/api/geocode/json?address=#address#";

	public static int solvG = 0;
	public static int solvN = 0;
	public static int solvGPC = 0;
	public static int solvNPC = 0;



	public static HashMap<String,String> queryLocation(HashMap<String, String> address){
		HashMap<String,String> ret = null;
		String addr = Location.prepareAdress(address);
		String addrWPC = Location.prepareAdressWithPostcode(address);
		ret = queryOSMAPILoc(addrWPC);
		if(ret != null){
			solvNPC++;
			return ret;
		}
		ret = queryOSMAPILoc(addr);
		if(ret != null){
			solvN++;
			return ret;
		}
		ret = queryGoogleAPILoc(addrWPC);
		if(ret != null){
			solvGPC++;
			return ret;
		}
		ret = queryGoogleAPILoc(addr);
		if(ret != null){
			solvG++;
			return ret;
		}
		return ret;
	}

	public static HashMap<String,String> queryOSMAPILoc(String address){
		HashMap<String,String> ret = null;

		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target(apiNominatim.replace("#address#", address));
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);

		Response response = request.get();

		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
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
		   return null;
		}

	}

	public static HashMap<String,String> queryGoogleAPILoc(String address){
		HashMap<String,String> ret = null;

		Client client = ClientBuilder.newClient();
		WebTarget resource = client.target(apiGMaps.replace("#address#", address));
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);

		Response response = request.get();

		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
		    try{
			    JSONObject jo =new JSONObject(response.readEntity(String.class));
			    JSONArray ja = jo.getJSONArray("results");
			    if(ja.length() >0){
			    	JSONObject loc = ja.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
			    	ret = new HashMap<String, String>();
			    	ret.put("loc_lat", loc.getString("lat"));
			    	ret.put("loc_lng", loc.getString("lng"));
			    }
		    }catch(JSONException jo){
		    	ret = null;
		    }
	    	return ret;
		} else {
		   return null;
		}
	}

	/**
	 * Prepares the URL address string to query over the network.
	 *
	 * @param a - Map of institution properties containing address parts
	 * @return URL formated string for addresses
	 */
	public static String prepareAdress(HashMap<String, String> a){
		String address= "";

		String street = a.get("su:address_street");
		String city = a.get("su:address_city");
		String country = a.get("su:address_country");
		address += street != null ? street + ",+" : "";
		address += city != null ? city + ",+" : "";
		address += country != null ? country + ",+" : "";

		if(address.endsWith(",+"))
			address = address.substring(0, address.length() - 2);
		// replace % encoded spaces with + and remove possibly contained quotes
		address.replaceAll("%20", "+");
		address.replaceAll("\"", "");

		return address;
	}

	/**
	 * Prepares the URL address string to query over the network. Also uses the contained postal code
	 *
	 * @param a - Map of institution properties containing address parts
	 * @return URL formated string for addresses
	 */
	public static String prepareAdressWithPostcode(HashMap<String, String> a){
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
		// replace % encoded spaces with + and remove possibly contained quotes
		address.replaceAll("%20", "+");
		address.replaceAll("\"", "");

		return address;
	}

}
