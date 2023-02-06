
function getbrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addbrand(event){
	//Set the values to update
	var $form = $("#brand-add-form");
	console.log($("#brand-form"));
	console.log("Hello");
	console.log("DF");
	var json = toJson($form);
	var url = getbrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   	        $("#add-brand-modal").modal("toggle");

	   		getbrandList();  
	   },
	   error: handleAjaxError
	});
    	 $('#add-brand').attr("disabled",true);

	return false;
}

function updatebrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var b = $("#brand-edit-form input[name=brand]").val();	
	var c = $("#brand-edit-form input[name=category]").val();	
	console.log("d");
	console.log(id,b,c);
	var url = getbrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getbrandList();   
	   },
	   error: handleAjaxError
	});

	return false;
}


function getbrandList(){
	var url = getbrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displaybrandList(data);  
	   },
	   error: handleAjaxError
	});
}

function deletebrand(id){
	var url = getbrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getbrandList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();

}

function uploadRows(){
	if (fileData.length>5000){
		alert("File Rows should be within 5000 rows");
		return;
	}
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getbrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displaybrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button onclick="deletebrand(' + e.id + ')">delete</button>'
		var buttonHtml = ' <button type="button" class="btn-sm btn-outline-info" onclick="displayEditbrand(' + e.id + ')"><i class="fa-solid fa-pen-to-square"></button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	$('#brand-table').DataTable({
  columnDefs: [
    {
        className: 'dt-center'
    } ] } );
}

function displayEditbrand(id){
	var url = getbrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displaybrand(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	if (errorData.length>0){
		$("#download-errors").attr("disabled",false);
	}
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	var f=fileName.split("\\");
	$('#brandFilecategory').html(f[f.length-1]);

}

function displayUploadData(){
 	resetUploadDialog(); 	
 	$("#download-errors").attr("disabled",true);
	 $('#process-data').attr("disabled",true);
	$('#upload-brand-modal').modal('toggle');
	  
}

function displaybrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}

function button(){
   $('#add-brand').attr("disabled",(($('#brand-add-form input[name=brand]').val().trim()=="") || ($('#brand-add-form input[name=category]').val().trim()=="")));
   }

//INITIALIZATION CODE
function init(){
	 $('#add-brand').attr("disabled",true);
	$('#add-brand').click(addbrand);
	$('#add').click(function(){
	document.getElementById("brand-add-form").reset();
    $("#add-brand-modal").modal("toggle");
	});
	$('#upload-brand-modal').on("hide.bs.modal",function(){getbrandList();});
    $('#brand-add-form').on("input change",button);
	$('#update-brand').click(updatebrand);
	$('#refresh-data').click(getbrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', function(){
    $('#process-data').attr("disabled",false);
    	updateFileName();});

}

$(document).ready(init);
$(document).ready(getbrandList);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#brand-nav").addClass("active");
});

