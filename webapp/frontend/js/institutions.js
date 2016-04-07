
var geocoder = new google.maps.Geocoder();
var mapObject = null;
var restURL = "http://"+window.location.hostname + ":8080/secunity-backend/api/";
var nominatim = "http://nominatim.openstreetmap.org/search/";

var markerClusterer = null;
var imageUrl = 'http://chart.apis.google.com/chart?cht=mm&chs=24x32&' +
	'chco=FFFFFF,008CFF,000000&ext=.png';


function writeAddressName(latLng) {
        geocoder.geocode({
          "location": latLng
        },
        function(results, status) {
          if (status == google.maps.GeocoderStatus.OK)
            console.log(results[0].formatted_address);
          else
            console.log("Unable to retrieve your address" + "<br />");
        });
      }

      function geolocationSuccess(position) {
        var userLatLng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        // Write the formatted address
        writeAddressName(userLatLng);

        var myOptions = {
          zoom : 5,
          center : userLatLng,
          mapTypeId : google.maps.MapTypeId.ROADMAP,
          minZoom: 2
        };
        // Draw the map
        mapObject = new google.maps.Map(document.getElementById("map"), myOptions);
        
        initialize();
      }

      function geolocationError(positionError) {
        document.getElementById("error").innerHTML += "Error: " + positionError.message + "<br />";
        var center = new google.maps.LatLng(47.5, 19.05);

        mapObject = new google.maps.Map(document.getElementById('map'), {
          zoom: 5,
          center: center,
          mapTypeId: google.maps.MapTypeId.ROADMAP,
          minZoom: 2
        });
        initialize();
      }

      function geolocateUser() {
        // If the browser supports the Geolocation API
        if (navigator.geolocation)
        {
          var positionOptions = {
            enableHighAccuracy: true,
            timeout: 10 * 1000 // 10 seconds
          };
          navigator.geolocation.getCurrentPosition(geolocationSuccess, geolocationError, positionOptions);
        }
        else
          console.log("Your browser doesn't support the Geolocation API");
      }

      function loadFromdirection(direction){
        geocodeAddress(geocoder, mapObject, direction);
      }

      function geocodeAddress(geocoder, resultsMap, address) {
        geocoder.geocode({'address': address}, function(results, status) {
          if (status === google.maps.GeocoderStatus.OK) {
            resultsMap.setCenter(results[0].geometry.location);
            var marker = new google.maps.Marker({
              map: resultsMap,
              position: results[0].geometry.location
            });
          } else {
            console.log('Geocode was not successful for the following reason: ' + status);
          }
        });
      }
function drawmarker(resultsMap, latlong, title) {
  var marker = new google.maps.Marker({
    map: resultsMap,
    position: latlong,
    title: title
    });
  markerClusterer.addMarker(marker, true);
  markerClusterer.redraw();
  }
function clearClusters(e) {
        e.preventDefault();
        e.stopPropagation();
        markerClusterer.clearMarkers();
      }
function initialize() {
  var institutions = queryInstitutions();
  var markers = [];
  /*for (var i = 0; i < 100; i++) {
    var dataPhoto = data.photos[i];
    var latLng = new google.maps.LatLng(dataPhoto.latitude,
      dataPhoto.longitude);
    var marker = new google.maps.Marker({
      position: latLng,
      label: ''+i
      });
    markers.push(marker);
  }*/
  markerClusterer = new MarkerClusterer(mapObject, markers);
}

function queryInstitutions(){
     getInstitutes(100, 0, function(data) {
        $.each(data.entity, function(k, v) {
          // Assert location for each institution
          getInstitute(v, function(inst) {
            assertLocation(inst.entity[v])
          });
        });
    });  
}

function assertLocation(institution){
  if(!institution.hasOwnProperty("su:loc_lat") || !institution.hasOwnProperty("su:loc_lng") ){
    queryNtimGeocoding(institution, true);
  }else{
    var loc = new google.maps.LatLng(institution["su:loc_lat"], institution["su:loc_lng"]);
    drawmarker(mapObject, loc, decodeURIComponent(institution["su:has_full_name"]));
  }
}

function queryGmapsGeocoding(institution, retry_if_over_limit){
  geocoder.geocode({'address': buildAddressforGeoCode(institution)}, function(results, status) {
      if (status === google.maps.GeocoderStatus.OK) {
        postLocation(institution.key, results[0].geometry.location.lat(), results[0].geometry.location.lng());
        console.log("Googleresolved");
        var loc = new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
        drawmarker(mapObject, loc, decodeURIComponent(institution["su:has_full_name"]));
      } else if(status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT){
            if(retry_if_over_limit){
              setTimeout(function(){
                geocoder = new google.maps.Geocoder();
                queryGmapsGeocoding(institution, true);
              }, 2000);
            }
            console.log("Google: Over query limit");
          }else{
            console.log("Google:resolution failed");
          }
        });
}

function queryNtimGeocoding(institution){
  console.log("REQUEST: " + nominatim+buildAddressforNominatime(institution)+"?format=json");
    $.getJSON(nominatim+buildAddressforNominatime(institution)+"?format=json",function(data){
      if(data.length){
        var result = data[0];
        postLocation(institution.key, result.lat, result.lon)
        var loc = new google.maps.LatLng(result.lat, result.lon);
        console.log("Nominaresolved");
        drawmarker(mapObject, loc, decodeURIComponent(institution["su:has_full_name"]));
      }else{
        queryGmapsGeocoding(institution);
      }
    });
}

function buildAddressforNominatime(inst){
  var street = inst["su:address_street"];
  var city = inst["su:address_city"];
  var country = inst["su:address_country"];
  var postalcode = inst["su:address_postcode"];
  var address = "";
  address += street ? street + ",+" : ""
  address += city ? city + ",+" : ""
  address += country ? country + ",+" : ""
  //address += postalcode ? postalcode : ""
  if(address.endsWith(",+"))
    address = address.substring(0, address.length - 2);
  var replacer1 = new RegExp("%20","g");
  var replacer2 = new RegExp("\"","g");
  address = address.replace(replacer1,"+");
  return address.replace(replacer2,"");
}

function buildAddressforGeoCode(inst){
  var street = inst["su:address_street"];
  var city = inst["su:address_city"];
  var country = inst["su:address_country"];
  var postalcode = inst["su:address_postcode"];
  var address = "";
  address += street ? street + ",+" : ""
  address += city ? city + ",+" : ""
  address += country ? country + ",+" : ""
  address += postalcode ? postalcode : ""
  if(address.endsWith(",+"))
    address = address.substring(0, address.length - 2);
  var replacer1 = new RegExp("%20","g");
  var replacer2 = new RegExp("\"","g");
  address = address.replace(replacer1," ");
  return address.replace(replacer2,"");
}

function postLocation(instName, lat, lng){
  if (!instName)
    return;
  $.put(restURL+"institution/"+instName, "{su:loc_lat='"+lat+"', su:loc_lng='" + lng + "'}", function(data,code){}, "application/json; charset=UTF-8");
  
}



google.maps.event.addDomListener(window, 'load', geolocateUser);