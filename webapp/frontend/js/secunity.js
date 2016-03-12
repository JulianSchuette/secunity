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

    //Save form data
    $.ajax({
        cache: false,
        url : apiInstitute + "/" + has_name,
        type: "POST",
        dataType : "json",
        contentType: "application/json; charset=UTF-8",
        data : JSON.stringify(data),
        context : Form,
        success : function(callback){
            //Where $(this) => context == FORM
			document.querySelector('#modal_add_inst').close();
			resetForm($( '#addInstForm' ));
        },
        error : function(){
            $(this).html("Error!");
        }
    });
});

/* get list of institutes and add to frontend */ //TODO retrieve subset, triggered by scrollspy
getInstitutes(function(data) {
      $.each(data.entity, function(k, v) {
	    // create institute card in frontend
	    getInstitute(v, function(data) {
		    $('#instTable').append('\
			  			<div class="mdl-cell mdl-cell--4-col">\
	                        <div class="mdl-card mdl-shadow--4dp">\
	                            <div class="mdl-card__title">\
	                                <h2 class="mdl-card__title-text">'+decodeURIComponent(data.entity[v]['su:has_short_name'])+'</h2>\
	                            </div>\
	                            <div class="mdl-card__media">\
	                                <img src="skytower.jpg" alt="" style="padding:10px;" border="0" height="157" width="173">\
	                              </div>\
	                              <div class="mdl-card__supporting-text">\
	                                '+decodeURIComponent(data.entity[v]['su:has_full_name'])+'\
	                              </div>\
	                              <div class="mdl-card__supporting-text">\
	                                '+decodeURIComponent(data.entity[v]['su:address_country'])+'\
	                              </div>\
	                              <div class="mdl-card__supporting-text">\
	                                '+decodeURIComponent(data.entity[v]['su:address_city'])+'\
	                              </div>\
	                            <div class="mdl-card__menu">\
	                                <button data-upgraded=",MaterialButton,MaterialRipple" id="show-dialog" class="mdl-button mdl-button--icon mdl-js-button mdl-js-ripple-effect">\
	                                <i class="material-icons">edit</i>\
	                                <span class="mdl-button__ripple-container"><span class="mdl-ripple"></span></span></button>\
	                            </div>           \
	                        </div> \
	                    </div>');
			});
	  });
});


function resetForm(form) {
    form.find('input:text, input:password, input:file, select, textarea').val('');
    form.find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
}


/* ---------------------------------- */
/* 			REST functions	    	  */
/* ---------------------------------- */

/* Return instititute data */ //TODO return Promise
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

