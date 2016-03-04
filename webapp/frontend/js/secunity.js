var apiInstitutes = "http://localhost:8080/secunity-backend/api/institutes";
var apiInstitute = "http://localhost:8080/secunity-backend/api/institute/";
var apiPerson = "http://localhost:8080/secunity-backend/api/person/";

function getInstitute(inst) {
  $.getJSON( apiInstitute + "/" + inst)
    .done(function( data ) {
      $.each( data.items, function( i, item ) {
        //TODO put into frontend
        alert(i + " - " + item);
      });
    });
}