var o_Id=0;
var quantity_v=0;
function Url(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/orderitem";
}


function orderurl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	return baseUrl + "/api/order"
}

function invoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content");
return baseUrl + "/api/invoice"
}


function len(){
	return JSON.parse(sessionStorage.getItem("itemlist")).length;
}


function additems(){
var $form = $("#order-form");
var json = toJsonobject($form);
json['id']=len();
const items=JSON.parse(sessionStorage.getItem("itemlist"));
 document.getElementById("order-form").reset();    

for (var i in items){
	if (items[i].barcode==json.barcode.trim()){
		alert("Order for product already exist");
		return;
	}}
var url=Url()+ '/check';
var data1=JSON.stringify(json);
$.ajax({
	   url: url,
	   type: 'POST',
	   data: data1,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
	   	if (data.is_p===2){
	   	alert("Inventory has less product than requested");
	   	return;
	   }
       else if (data.is_p===0){
	   	alert("Product with given barcode does not exist");
	   	return;
	   	}
	   	else{
	   		json["name"]=data.name;
	   		items.push(json);
            sessionStorage.setItem("itemlist",JSON.stringify(items));
            showtable();
	   	}
	   },
	   error: handleAjaxError
	});
}

function getitem(id){
	o_Id=id;
	var url=orderurl()+"/"+id;
	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			$('#view-order-modal').modal('toggle');
			displayOrderitem(data);
		},
	  error: handleAjaxError
	});
}

function getItem(){
	var url=orderurl()+"/"+o_Id;
	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){;
			displayOrderitem(data);
		},
	  error: handleAjaxError
	});
}

function getitemid(id){
	var url=Url()+"/"+id;
	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			displayEditorder(data);
		},
	  error: handleAjaxError
	});
}


function edititem(){
var $form = $("#edit-form");
var url=Url()+ '/check';
var json = toJson($form);
$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
		   	if (data.is_p===2){
		   	alert("Inventory has less product than requested");
		   	return;
	   }
       else if (data.is_p===0){
		   	alert("Product with given barcode does not exist");
		   	return;
	   	}
	   	else{
	   		const items=JSON.parse(sessionStorage.getItem("itemlist"));
			var d=JSON.parse(json);
			d["name"]=data.name;
			items[parseInt(d.id)]=(d);
			sessionStorage.setItem("itemlist",JSON.stringify(items));
			$("#edit-modal").modal("toggle");
			showtable();
	   	}
	   },
	   error: handleAjaxError
	});
}

function updateitem(){
var $form = $("#edit-view-form");
var json = toJsonobject($form);
json['old_q']=quantity_v;
json1=JSON.stringify(json);
var id=$("#edit-view-form input[name=id]").val();
var url=Url()+ '/'+id;
$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json1,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
		if (data.is_p===2){
		   	alert("Insufficient inventory for the requested product");
		   	return;
	   }
       else if (data.is_p===0){
		   	alert("Product with given barcode does not exist");
		   	return;
	   }
	   else{
			$("#view-edit-modal").modal("toggle");
            getItem();
	   	}
	   },
	   error: handleAjaxError
	});

}


function deleteitem(i){
const items=JSON.parse(sessionStorage.getItem("itemlist"));
items.splice(i,1);
sessionStorage.setItem("itemlist",JSON.stringify(items));
showtable();
}

function addorder(){
	var items=(sessionStorage.getItem("itemlist"));
	var url=orderurl();
	$.ajax({
		url: url,
		type: 'POST',
		data : items,
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			if (data.is_p===2){
				alert("Insufficient inventory for the requested product");
				return;}
			else{
			getorder();
			$('#add-inventory-modal').modal("toggle");
		}
		},
	  error: handleAjaxError
	});
}

function getorder(){
	var url=orderurl();
	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			displayOrder(data);
		},
	  error: handleAjaxError
	});
}



function displayEdititem(i){
var item=JSON.parse(sessionStorage.getItem("itemlist"));
var data=(item[i]);
 $("#edit-form input[name=barcode]").val(data.barcode);
  $("#edit-form input[name=quantity]").val(data.quantity);
 $("#edit-form input[name=price]").val(data.price);
 $("#edit-form input[name=id]").val(i);
 $("#edit-modal").modal("toggle");
}

function displayEditorder(data){
 $("#edit-view-form input[name=barcode]").val(data.barcode);
  $("#edit-view-form input[name=quantity]").val(data.quantity);
 $("#edit-view-form input[name=price]").val(data.price);
 $("#edit-view-form input[name=id]").val(data.id);
 quantity_v=data.quantity;
 $("#view-edit-modal").modal("toggle");
}

function showtable(){
	var data=JSON.parse(sessionStorage.getItem("itemlist"));
	var $tbody = $('#item-table').find('tbody');
	$tbody.empty();
	var j=0;
	for(var i in data){
		var e = (data[i]);
		var buttonHtml = ' <button onclick="displayEdititem(' + i + ')">edit</button>'
		buttonHtml+=' <button onclick="deleteitem(' + i + ')">delete</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.price*e.quantity + '</td>'
		+ '<td>' + buttonHtml +'</td>';
        $tbody.append(row);
	}
}

function displayOrder(data){
	$('#order-table').dataTable().fnClearTable();
    $('#order-table').dataTable().fnDestroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = (data[i]);
		e.time=e.time.replace('T',"   ");
		var buttonHtml1 = ' <button type="button" class="btn-sm btn-primary" onclick="getitem(' + e.id + ')">view</button>'
		var buttonHtml2=' <button type="button" class="btn-sm btn-primary" onclick="downloadPDF(' + e.id + ')">Invoice Generation</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.time + '</td>'
		+ '<td>' + buttonHtml1 +'</td>'
		+ '<td>' + buttonHtml2 +'</td>';
        $tbody.append(row);
	}
	$('#order-table').DataTable({"columnDefs": [
        { "targets": [0,2,3], "searchable": false },
        { "width": "10%", "targets": 0 },
        { "width": "20%", "targets": [2,3] }
    ],
pageLength : 8,
    lengthMenu: [[8, 10, 20, -1], [8, 10, 20, 'All']]});
}


function displayOrderitem(data){
    var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	var j=0;
	for(var i in data){
		var e = (data[i]);

		var buttonHtml = ' <button onclick="getitemid(' + data[i].id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.price*e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
        $tbody.append(row);
	}
}


function downloadPDF(id) {
	var url = invoiceUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	    xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {

      	var link=document.createElement('a');
      	link.href=window.URL.createObjectURL(blob);
      	link.download="Invoice_" + id +"_"+ new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function button(){
   $('#add-items').attr("disabled",(($('#order-form input[name=barcode]').val().trim()=="" || ($('#order-form input[name=quantity]').val().trim()=="") || ($('#order-form input[name=price]').val().trim()==""))));
   }

function clearstorage(){
	sessionStorage.clear();
	const items=[];
	sessionStorage.setItem("itemlist",JSON.stringify(items));
}
function init(){
	clearstorage();
	$("#add-ord").click(function(){
		$('#add-inventory-modal').modal({backdrop: 'static', keyboard: false});
		showtable();
	});

	$("#add-items").click(additems);
	$("#edit-btn").click(edititem);
	$("#edit-button").click(updateitem);
	$('#add-items').attr("disabled",true);
	$('#order-form').on('input change',button);
    $('#submit').click(addorder)
    $("#clear").click(clearstorage);
    $("#closesign").click(clearstorage);
     $("#refresh-data").click(getorder);
    $("#submit").click(function(){
    	clearstorage();

    })
}
$(document).ready(init);
$(document).ready(getorder);
