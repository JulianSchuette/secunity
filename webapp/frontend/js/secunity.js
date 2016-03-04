var apiInstitutes = "http://localhost:8080/secunity-backend/api/institutions";
var apiInstitute = "http://localhost:8080/secunity-backend/api/institution";
var apiPerson = "http://localhost:8080/secunity-backend/api/person/";

function getInstitute(inst) {
  $.getJSON( apiInstitute+"/" + encodeURIComponent(inst))
    .done(function( data ) {
    	alert(data.entity.directType);
      $.each( data.items, function( i, item ) {
        //TODO put into frontend
        alert(i + " - " + item);
      });
    })
	.fail( function(d, textStatus, error) {
        alert("getJSON failed, status: " + textStatus + ", error: "+error)
    });
}
