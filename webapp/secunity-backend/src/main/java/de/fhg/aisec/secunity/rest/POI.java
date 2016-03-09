package de.fhg.aisec.secunity.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("pois/")
public class POI {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
	public String getPOI(@QueryParam("z") int zoom, @QueryParam("l") int left, @QueryParam("t") int top, @QueryParam("r") int right, @QueryParam("b") int bottom) {
    	System.out.println("Sending POI list");
    	String poiList = "lat	lon	icon	iconSize	iconOffset	title	description	popupSize\n" + 
    			"48.6118713	19.7747109	images/poi.png	14,14	7,7	<nobr>Utekáč,,Drahová</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.6118713,19.7747109,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.6118863	19.7746318	images/poi.png	14,14	7,7	<nobr>Utekáč,,Drahová</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.6118863,19.7746318,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5842125	19.7685823	images/poi.png	14,14	7,7	<nobr>Kokava nad Rimavicou, Močiar</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5842125,19.7685823,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.6178646	19.7681537	images/poi.png	14,14	7,7	<nobr>Utekáč,,Havrilovo</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.6178646,19.7681537,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5875727	19.7570709	images/poi.png	14,14	7,7	<nobr>Šoltýska, rázcestie</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5875727,19.7570709,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.6177409	19.7508295	images/poi.png	14,14	7,7	<nobr>Beračka</nobr>	<nobr>[Vrch - vrcholy hôr a kopcov]</nobr><br/><a href='javascript:cfsetMapCenter(48.6177409,19.7508295,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5983513	19.7447348	images/poi.png	14,14	7,7	<nobr>Močiar</nobr>	<nobr>[Vrch - vrcholy hôr a kopcov]</nobr><br/><a href='javascript:cfsetMapCenter(48.5983513,19.7447348,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5804538	19.744152	images/poi.png	14,14	7,7	<nobr>Šoltýska</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5804538,19.744152,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5874495	19.7347594	images/poi.png	14,14	7,7	<nobr>Homračka</nobr>	<nobr>[Vrch - vrcholy hôr a kopcov]</nobr><br/><a href='javascript:cfsetMapCenter(48.5874495,19.7347594,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5926447	19.7340314	images/poi.png	14,14	7,7	<nobr>Kokava nad Rimavicou, Línia</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5926447,19.7340314,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5928763	19.7274239	images/poi.png	14,14	7,7	<nobr>Kokava nad Rimavicou, Ujať r.s. Ipeľ</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5928763,19.7274239,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.5968603	19.7213118	images/poi.png	14,14	7,7	<nobr>Kokava nad Rimavicou, Ujať</nobr>	<nobr>[Autobusová zastávka - autobusové zastávky]</nobr><br/><a href='javascript:cfsetMapCenter(48.5968603,19.7213118,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.630778	19.707111	images/poi.png	14,14	7,7	<nobr>Ďurkov vrch</nobr>	<nobr>[Vrch - vrcholy hôr a kopcov]</nobr><br/><a href='javascript:cfsetMapCenter(48.630778,19.707111,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"48.6186531	19.7007692	images/poi.png	14,14	7,7	<nobr>Drahová</nobr>	<nobr>[Vrch - vrcholy hôr a kopcov]</nobr><br/><a href='javascript:cfsetMapCenter(48.6186531,19.7007692,16)'>Zobraziť&gt;</a>	200,80\n" + 
    			"";
    	return poiList;   	
	}
}
