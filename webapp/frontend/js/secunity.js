
var apiInstitutes = "http://"+window.location.hostname + ":8080/secunity-backend/api/institutions";
var apiInstitute = "http://"+window.location.hostname + ":8080/secunity-backend/api/institution";
var apiPerson = "http://"+window.location.hostname + ":8080/secunity-backend/api/person/";


// Add put and delete functions for jQuery
$.put = function(url, data, callback, type){
 
  if ( $.isFunction(data) ){
    type = type || callback,
    callback = data,
    data = {}
  }
 
  return $.ajax({
    url: url,
    type: 'PUT',
    success: callback,
    data: data,
    contentType: type
  });
}

$.delete = function(url, data, callback, type){
 
  if ( $.isFunction(data) ){
    type = type || callback,
        callback = data,
        data = {}
  }
 
  return $.ajax({
    url: url,
    type: 'DELETE',
    success: callback,
    data: data,
    contentType: type
  });
}

/* Handle creation of new institution */
$("#addInstForm").submit(function(e){
    e.preventDefault();

    var data = {}
    var Form = this;

    //Gather data and remove undefined attributes
    $.each(this.elements, function(i, v) {
        var input = $(v);
        if(input.val() && input.val() !== 'undefined')
        data[input.attr("name")] = input.val();
        delete data["undefined"];
    });

    //Validate data
    var has_name = data['su:has_full_name'];
    if (!has_name) {
    	//TODO Display proper error message
        console.log("Name missing");
    } 

    // Post data 
    createInstitute(
        has_name,
        data, 
        function(callback){
            //Where $(this) => context == FORM
            document.querySelector('#modal_add_inst').close();
            resetForm($( '#addInstForm' ));
        },
        function(){
            $(this).html("Error!");
        }
        );
});



function resetForm(form) {
    form.find('input:text, input:password, input:file, select, textarea').val('');
    form.find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
}


/* ---------------------------------- */
/* 			REST functions	    	  */
/* ---------------------------------- */

/* Return institute data */ //TODO return Promise
function getInstitute(inst, done, fail) {
  $.getJSON( apiInstitute+"/" + encodeURIComponent(inst))
    .done(function( data ) {
    	if (done)
    		done(data);
    })
	.fail( function(d, textStatus, error) {
        if (fail)
        	fail(d,textStatus,error);
    });
}

/* List institutes */
function getInstitutes(limit, offset, wlocfirst, done, fail) {
  $.getJSON( apiInstitutes + '?limit='+limit+'&offset='+offset + (wlocfirst ? "&wlocfirst=wlocfirst":"") )
    .done(function( data ) {
    	if (done)
    		done(data);
    })
	.fail( function(d, textStatus, error) {
        if (fail)
        	fail(d,textStatus,error);
        else
        	console.log(textStatus + ", " + error);
    });
}

/* Create new institute */
function createInstitute(name, map, success, fail) {
    //Save form data
    $.ajax({
        cache: false,
        url : apiInstitute + "/" + name,
        type: "POST",
        dataType : "json",
        contentType: "application/json; charset=UTF-8",
        data : JSON.stringify(map),
        context : document.body,
        success : success,
        error : fail
    });
}

function locate(){
    // If the browser supports the Geolocation API
        if (navigator.geolocation)
        {
          var positionOptions = {
            enableHighAccuracy: true,
            timeout: 20 * 1000, // 10 seconds
          };
          navigator.geolocation.getCurrentPosition(function(response){
            // Location success
            var yourLatLng = new google.maps.LatLng(response.coords.latitude, response.coords.longitude);
            placeMarker(yourLatLng, "Your location");
            insertAddress(yourLatLng);
          }, function(response){
            // Location error
            alert("Locating your position failed");
          }, positionOptions);
        }
        else
          alert("Your browser doesn't support the Geolocation API");
}

function verifyLocation(){
    if($('#inststreetnum').val() &&
        $('#instCity').val() &&
        $('#instCountry').val()){
        checkAddress();
    }else{
        alert("Fill in values for street, city and Country first");
    }
}

function checkAddressIf(){
    if($('#inststreetnum').val() &&
        $('#instCity').val() &&
        $('#instCountry').val()){
        checkAddress();
    }
}

function checkAddress(){
    
    var street =  $('#inststreetnum').val();
    var city =$('#instCity').val();
    var country = $('#instCountry').val();
    var postalcode = $('#instPostcode').val();
    var address = "";
    address += street ? street + ",+" : ""
    address += city ? city + ",+" : ""
    address += country ? country + ",+" : ""
    address += postalcode ? postalcode : ""
    if(address.endsWith(",+"))
        address = address.substring(0, address.length - 2);

    geocoder.geocode({'address': address}, function(results, status) {
          if (status === google.maps.GeocoderStatus.OK) {
            placeMarker(results[0].geometry.location, address);
          } else {
            console.log('Geocode was not successful for the following reason: ' + status);
          }
        });
    
}

function placeMarker(loc, title){
    // Set longitude and latitude values from the searched location to the hidden input fields
    $('#instlat').val(loc.lat);
    $('#instlng').val(loc.lng);
    var myOptions = {
          zoom : 15,
          center : loc,
          mapTypeId : google.maps.MapTypeId.ROADMAP,
          minZoom: 2
        };
        // Draw the map
        mapObject = new google.maps.Map(document.getElementById('map'), myOptions);
        var marker = new google.maps.Marker({
          map: mapObject,
          position: loc,
          title: title
          });
}

function insertAddress(latLng){
    geocoder.geocode({
          "location": latLng
        },
        function(results, status) {
          if (status == google.maps.GeocoderStatus.OK){
            var acs = results[0].address_components;
            var street = "";
            var streetnum = "";
            var city = "";
            var country = "";
            var postalcode = "";
            for(var key in acs){
                var info = acs[key];
                console.log(JSON.stringify(info));
                if(_.contains(info["types"], "route")){
                    street = info["short_name"];
                }else if(_.contains(info["types"], "street_address")){
                    streetnum = info["short_name"];
                }else if(_.contains(info["types"], "locality")){
                    city = info["short_name"];
                }else if(_.contains(info["types"], "country")){
                    country = info["short_name"];
                }else if(_.contains(info["types"], "postal_code")){
                    postalcode = info["short_name"];
                }
            }
            if(streetnum){
                handleInputAndLabel($("#inststreetnum"),streetnum);
            }else{
                handleInputAndLabel($("#inststreetnum"),street);
            }
            handleInputAndLabel($("#instCity"),city);
            handleInputAndLabel($("#instCountry"),country);
            handleInputAndLabel($("#instPostcode"),postalcode);
            

            console.log(results[0].formatted_address);
          }else
            console.log("Unable to retrieve your address" + "<br />");
        });
}

function handleInputAndLabel(inputcp, text){
    inputcp.val(text);
    if(text){
        inputcp.parent().addClass("is-dirty");
    }else{
        inputcp.parent().removeClass("is-dirty");
    }

}

/*
    input ids addresses

    inststreetnum
    instCity
    instPostcode
    instCountry

    input ids buttons
    locateButton
    checkButton

*/