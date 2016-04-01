var apiInstitutes = "http://localhost:8080/secunity-backend/api/institutions";
var apiInstitute = "http://localhost:8080/secunity-backend/api/institution";
var apiPerson = "http://localhost:8080/secunity-backend/api/person/";



/* Handle creation of new institution */
$("#addInstForm").submit(function(e){
    e.preventDefault();

    var data = {}
    var Form = this;

    //Gather data and remove undefined attributes
    $.each(this.elements, function(i, v) {
        var input = $(v);
        data[input.attr("name")] = input.val();
        delete data["undefined"];
    });

    //Validate data
    var has_name = data['akts:has-pretty-name'];
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
function getInstitutes(limit, offset, done, fail) {
  $.getJSON( apiInstitutes + '?limit='+limit+'&offset='+offset )
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

