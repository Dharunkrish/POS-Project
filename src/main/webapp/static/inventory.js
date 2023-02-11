var glob_id=0;
function getinventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addinventory(event){
	//Set the values to update
	var $form = $("#inventory-add-form");
    $('#add-inventory-modal').modal('toggle');
	var json = toJson($form);
    document.getElementById("inventory-add-form").reset();    
	var url = getinventoryUrl()+"/supervisor";
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
	var id = $("#inventory-edit-form input[name=productid]").val();	
	var b = $("#inventory-edit-form input[name=quantity]").val();	
	var url = getinventoryUrl() + "/supervisor/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
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
	   var url=getinventoryUrl()+"/id"
	   $.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	displaydropdown(data);
	   },
	   error: handleAjaxError
	});
}

function showdropdown_edit(){
	   var url=getinventoryUrl()+"/id"
	   $.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
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
	var url = getinventoryUrl() + "/supervisor/" + id;

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
	getinventoryList();
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
	$('#inventory-table').dataTable().fnClearTable();
    $('#inventory-table').dataTable().fnDestroy();
	var $tbody = $('#inventory-table').find('tbody');

	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button type="button" class="btn-sm btn-outline-info" onclick="displayEditinventory(' + e.id + ')"><i class="fa-solid fa-pen-to-square"></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+'<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if (getRole()==="operator"){
	$('#inventory-table').DataTable({
  columnDefs: [
    {
        className: 'dt-center'
    },                 { 'visible': false, 'targets': [3] }
] } );
}
else{
	$('#inventory-table').DataTable();
}
}

function displaydropdown(data){
	$('#idvalue').empty();
	var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#idvalue').append(p);

	for(var i in data){
		var e = data[i];
		var row = e.barcode;
		var p=$("<option />");
        p.html(row);
        p.val(e.product_id);
      $("#idvalue").append(p);
}
$("#idvalue").selectpicker('refresh');
$("#idvalue").val($('#idvalue option:first').val());
$("#idvalue").selectpicker('refresh');
}

function displayEditinventory(id){
	var url = getinventoryUrl() + "/" + id;
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
    var f=fileName.split("\\");
	$('#inventoryFileName').html(f[f.length-1]);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayinventory(data){
	$("#barcodeedit").attr("disabled","true");
	$("#barcodeedit").val(data.barcode);
	$("#inventory-edit-form input[name=productid]").val(data.id);
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
	$('#upload-inventory-modal').on("hide.bs.modal",function(){getinventoryList()});
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#add-inventory').attr("disabled",true);
	$('#inventory-add-form').on('input change',button);
    $('#inventoryFile').on('change', updateFileName)  
    if (getRole()==="operator"){
    	$("#add-inv").css("display","none");
    	$("#upload-data").css("display","none");
    }
}

$(document).ready(init);
$(document).ready(getinventoryList);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#inv-nav").addClass("active");
});

