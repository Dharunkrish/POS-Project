var e=0;
var pattern=new RegExp("^[a-zA-Z0-9_]*$")
function getbrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addbrand(event){
	//Set the values to update
	console.log(event);
	if (!pattern.test($("#inputBrand").val())){
       document.getElementById('inputBrand').setCustomValidity('Enter only alpahanumeric charaters & underscores');
       return;
	}
	else{
		       document.getElementById('inputBrand').setCustomValidity('');
	}
	if (!pattern.test($("#inputcategory").val())){
       document.getElementById('inputcategory').setCustomValidity('Enter only alpahanumeric charaters & underscores');
       return;
	}
	else{
		       document.getElementById('inputcategory').setCustomValidity('');
	}
	var $form = $("#brand-add-form");
	var json = toJson($form);
	var url = getbrandUrl()+"/supervisor";

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
	   		toastr.options.timeOut = 2000;
	   		toastr.success("Brand added successfully");
	   },
	   error: handleAjaxError
	});
    $('#add-brand').attr("disabled",true);
	return false;
}

function updatebrand(event){
	console.log("fgg");
	if (!pattern.test($("#editbrand").val())){
		console.log("f");
       document.getElementById('editbrand').setCustomValidity('Enter only alpahanumeric charaters & underscores');
       return;
	}
	else{
		       document.getElementById('editbrand').setCustomValidity('');
	}
	if (!pattern.test($("#editcategory").val())){
       document.getElementById('editcategory').setCustomValidity('Enter only alpahanumeric charaters & underscores');
       return;
	}
	else{
		       document.getElementById('editcategory').setCustomValidity('');
	}
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var b = $("#brand-edit-form input[name=brand]").val();	
	var c = $("#brand-edit-form input[name=category]").val();	
	var url = getbrandUrl() + "/supervisor/" + id;

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
	   		toastr.options.timeOut = 2000;
	   		toastr.success("Brand updated successfully"); 
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


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
    $('#process-data').attr("disabled",true);
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
	setTimeout(function(){
		            if (e===1){
		            	        e=0
            		   			toastr.timeOut=0;
    toastr.error("Uploaded file has some errors. Download the file to see the error")
            }
            else{
            				   		toastr.options.timeOut = 2000;
	
	   		toastr.success("File uploaded successfully");
            }
        },200);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows(0);
}

function uploadRows(i){
	
	if (fileData.length>5000){
		alert("File Rows should be within 5000 rows");
		return;
	}
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		    $("#brandFile").val("");
            $('#brandFilecategory').html("Choose File");
		return;
	}
	//Process next row
	var row = fileData[processCount];
	processCount++;

	
	var json = JSON.stringify(row);
	var url = getbrandUrl()+"/supervisor";

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows(i++);  
	   },
	   error: function(response){
	   		row.error=response.responseJSON.message
	   		errorData.push(row);
	   		uploadRows(i++);
	}
	});
}


function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displaybrandList(data){
	$('#brand-table').dataTable().fnClearTable();
    $('#brand-table').dataTable().fnDestroy();
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
	if (getRole()==="operator"){
	var ta=$('#brand-table').DataTable({
  columnDefs: [
            {"className": "dt-center", "targets": "_all"},
                { 'visible': false, 'targets': [2] }],
                 pageLength : 7,
        autoWidth: true,
    lengthMenu: [[7, 10, 20, -1], [7, 10, 20, 'All']]} );
}
else{
		var ta=$('#brand-table').DataTable({
  columnDefs: [
    {
        className: 'dt-center'
    }
],   "info":false,
        autoWidth: true,
    lengthMenu: [[10, 20, -1], [10, 20, 'All']]
 } );
}
    new $.fn.dataTable.FixedHeader(ta);

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
	var file = $('');
	$('#brandFilecategory').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	 	$("#download-errors").attr("disabled",true);
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	if (errorData.length>0){
		$("#download-errors").attr("disabled",false);
		e=1;
	}
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	var f=fileName.split("\\");
	l=f[f.length-1];
	if (l.slice(-3)!=="tsv"){
		toastr.options.timeOut = 0;
	   		toastr.error("Upload only TSV files");
	   		$("#brandFile").val("");
	   		return;
	}
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
        if (getRole()==="operator"){
    	$("#add").css("display","none");
    	 $("#upload-data").css("display","none");
    }
    $("#brandFile").click(resetUploadDialog)
}

$(document).ready(init);
$(document).ready(getbrandList);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#brand-nav").addClass("active");
});

