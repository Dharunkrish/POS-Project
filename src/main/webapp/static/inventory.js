var glob_id=0;
function getinventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addinventory(event){
	//Set the values to update
	console.log("K");
	var $form = $("#inventory-add-form");
    $('#add-inventory-modal').modal('toggle');
	var json = toJson($form);
    document.getElementById("inventory-add-form").reset();    
	var url = getinventoryUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getinventoryList();  
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateinventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();	
	var b = $("#inventory-edit-form input[name=quantity]").val();	
	console.log("d");
	console.log(id,b);
	var url = getinventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
    console.log(json);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getinventoryList();   
	   },
	   error: handleAjaxError
	});

	return false;
}

function showdropdown(){
	   //var d1=$("#id");
	   //$("#idvalue").find('option').remove();
	   //document.getElementById("idvalue").innerHTML = "";
	   console.log("show");
	   var url=getinventoryUrl()+"/id"
	   console.log(url);
	   $.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	console.log(data);
	   	displaydropdown(data);
	   },
	   error: handleAjaxError
	});
}

function showdropdown_edit(){
	   //var d1=$("#id");
	   //$("#idvalue").find('option').remove();
	   //document.getElementById("idvalue").innerHTML = "";
	   console.log("show");
	   var url=getinventoryUrl()+"/id"
	   console.log(url);
	   $.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	console.log(data);
	   	displaydropdown(data);
	   },
	   error: handleAjaxError
	});
}

function getinventoryList(){
    document.getElementById("inventory-form").reset();
	var url = getinventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayinventoryList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteinventory(id){
	var url = getinventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getinventoryList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
		var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
	getinventoryList();
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
	console.log("H");
		getinventoryList();

}

function uploadRows(){
	if (fileData.length>5000){
		alert("File Rows should be within 5000 rows");
		return;
	}
	console.log(fileData.length);
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
	var url = getinventoryUrl();

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

function displayinventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');

	$tbody.empty();
	for(var i in data){
		var e = data[i];
		console.log(e);
		var buttonHtml = ' <button onclick="displayEditinventory(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+'<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	$('#inventory-table').DataTable();
}

function displaydropdown(data){
	$('#idvalue').empty();
	var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#idvalue').append(p);
	for(var i in data){
		var e = data[i];
		console.log(e);
		var row = e.barcode;
		var p=$("<option />");
        p.html(row);
        p.val(e.product_id);
      $("#idvalue").append(p);
}
}

function displayEditinventory(id){
	var url = getinventoryUrl() + "/" + id;
	console.log(url,id);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayinventory(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){

	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	console.log(fileName);
    var f=fileName.split("\\");
	$('#inventoryFileName').html(f[f.length-1]);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayinventory(data){
	$("#idedit").attr("disabled","true");
	$("#idedit").val(data.id);
    $("#inventory-edit-form input[name=quantity]").val(data.quantity);	
	$('#edit-inventory-modal').modal('toggle');
}

function button(){
	var d=$('#idvalue :selected').text();
   $('#add-inventory').attr("disabled",(d=="None" || ($('#inventory-add-form input[name=quantity]').val().trim()=="")));
   }


//INITIALIZATION CODE
function init(){
	$('#add-inv').click(function(){
		$('#add-inventory-modal').modal({backdrop: 'static', keyboard: false});
        showdropdown();
    });
	$('#update-inventory').click(updateinventory);
	$('#add-inventory').click(addinventory);
	$('#refresh-data').click(getinventoryList);
	$('#upload-inventory-modal').on("hide.bs.modal",function(){getinventoryList();
		console.log("FSF");});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#add-inventory').attr("disabled",true);
	$('#inventory-add-form').on('input change',button);
    $('#inventoryFile').on('change', updateFileName)  

}

$(document).ready(init);
$(document).ready(getinventoryList);

