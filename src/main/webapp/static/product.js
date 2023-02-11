  data_arr=new Array();
var edit=0;
function getproductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getbrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addproduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var jso = toJsonobject($form);
	if (isNaN(jso.mrp)){
		alert("MRP must be a number");
		return;
	}
	var json=JSON.stringify(jso);
	var url = getproductUrl()+"/supervisor";
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getproductList();  
	   		$("#add-product-modal").modal('toggle');
	   },
	   error: handleAjaxError
	});
	return false;
}

function updateproduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var product_id=$("#product-edit-form input[name=product_id]").val();
	var url = getproductUrl() + "/supervisor/" + product_id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getproductList();   
	   },
	   error: handleAjaxError
	});

	return false;
}


function getproductList(){
	var url = getproductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayproductList(data);  
	   },
	   error: handleAjaxError
	});
}


function showdropdown(){
	   var url=getbrandUrl()
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
	   var url=getbrandUrl()
	   $.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	displaydropdownedit(data);

	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
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
	var url = getproductUrl();

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

function displayproductList(data){
	$('#product-table').dataTable().fnClearTable();
    $('#product-table').dataTable().fnDestroy();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		//var buttonHtml = '<button onclick="deleteproduct(' + e.product_id + ')">delete</button>'
		var buttonHtml = ' <button type="button" class="btn-sm btn-outline-info" onclick="displayEditproduct(' + e.product_id + ')"><i class="fa-solid fa-pen-to-square"></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.barcode + '</td>'
		+ '<td>'  + e.mrp + '</td>'
        + '<td>'  + data_arr[e.brand_Category_id-1].brand + "/" +  data_arr[e.brand_Category_id-1].category +'</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	if (getRole()==="operator"){
			$('#product-table').DataTable({
  columnDefs: [
    {
        className: 'dt-center'
    },                 { 'visible': false, 'targets': [4] }
] } );
}
else{
	$('#product-table').DataTable();
}
}

function displaydropdownedit(data){
	$('#editBrandid').empty();
	var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#editBrandid').append(p);
	for(var i in data){
		var e = data[i].brand + "/" + data[i].category;
		var p=$("<option />");
        p.html(e);
        p.val(data[i].id);
        data_arr.push(data[i])
      $("#editBrandid").append(p);
}
$("#editBrandid").val(edit);	

}


function displaydropdown(data){
	$('#inputBrandid').empty();
	var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#inputBrandid').append(p);
    window.data=[]
	for(var i in data){
		var e = data[i].brand + "/" + data[i].category;
		var p=$("<option />");
        p.html(e);
        p.val(parseInt(data[i].id));
        data_arr.push(data[i])
      $("#inputBrandid").append(p);
}
$("#inputBrandid").selectpicker('refresh');
$("#inputBrandid").val($('#inputBrandid option:first').val());
$("#inputBrandid").selectpicker('refresh');
getproductList();
}

function displayEditproduct(product_id){
    showdropdown_edit();
	var url = getproductUrl() + "/" + product_id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	    displayproduct(data);   

	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	if (errorData.length>0){
		$('#download-errors').attr("disabled",false);
	}
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	var f=fileName.split("\\");
	$('#productFilecategory').html(f[f.length-1]);
}

function displayUploadData(){
 	resetUploadDialog(); 	
 	$("#download-errors").attr("disabled",true);
	 $('#process-data').attr("disabled",true);
	$('#upload-product-modal').modal('toggle');
}

function displayproduct(data){
	var id=""+(data.brand_Category_id);
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=mrp]").val(data.mrp);	
	$("#product-edit-form input[name=product_id]").val(data.product_id);	
    edit=data.brand_Category_id;
    $("#editBrandid").val(edit);
	$('#edit-product-modal').modal('toggle');
}

function button(){
	var d=$('#inputBrandid :selected').text();
   $('#add-product').attr("disabled",(d=="None" || ($('#product-form input[name=barcode]').val().trim()=="")) || ($('#product-form input[name=name]').val().trim()=="")||($('#product-form input[name=mrp]').val().trim()==""));
   }

//INITIALIZATION CODE
function init(){
	$('#add-pro').click(function(){
		document.getElementById("product-form").reset();
	    $('#add-product-modal').modal('toggle');
	});
	$('#upload-product-modal').on("hide.bs.modal",function(){getproductList();});
	$('#add-product').attr("disabled",true);
	$('#product-form').on("input change",button);
	$('#add-product').click(addproduct);
	$('#update-product').click(updateproduct);
	$('#refresh-data').click(getproductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
      $('#productFile').on('change', function(){
    $('#process-data').attr("disabled",false);
    	updateFileName();});
       if (getRole()==="operator"){
    	$("#add-pro").css("display","none");
    	 $("#upload-data").css("display","none");
    }
}
$(document).ready(showdropdown);
$(document).ready(init);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#prod-nav").addClass("active");
});


