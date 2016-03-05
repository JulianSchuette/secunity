var apiInstitutes = "http://localhost:8080/secunity-backend/api/institutions";
var apiInstitute = "http://localhost:8080/secunity-backend/api/institution";
var apiPerson = "http://localhost:8080/secunity-backend/api/person/";



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
    var instName = data.instName;
    if (!instName) {
    	$(this).html("Name missing");
    } 

    //Save form data
    $.ajax({
        cache: false,
        url : apiInstitute + "/" + instName,
        type: "POST",
        dataType : "json",
        contentType: "application/json; charset=UTF-8",
        data : JSON.stringify(data),
        context : Form,
        success : function(callback){
            //Where $(this) => context == FORM
            console.log(JSON.parse(callback));
            $(this).html("Success!");
        },
        error : function(){
            $(this).html("Error!");
        }
    });
});



/* ---------------------------------- */
/* 			REST functions	    	  */
/* ---------------------------------- */

/* Return instititute data */
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
function getInstitutes(done, fail) {
  $.getJSON( apiInstitutes )
    .done(function( data ) {
    	done(data);
    })
	.fail( function(d, textStatus, error) {
        fail(d,textStatus,error);
    });
}

