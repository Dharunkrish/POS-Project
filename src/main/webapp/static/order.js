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
			$('#inventory-less-alert').css('display', 'block'); 
			$("#inventory-less-alert").fadeOut(3000);	   	
	   }
       else if (data.is_p===0){
       	$('#no-product-alert').css('display', 'block'); 
			$("#no-product-alert").fadeOut(3000);	
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
	var url=orderurl()+"/"+o_Id;
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

function getedititem(id){
	o_Id=id;
	var url=orderurl()+"/"+o_Id;
	$.ajax({
		url: url,
		type: 'GET',
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			displayeditOrderitem(data);
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
         getedititem(o_Id);
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

function deleteitemid(id){
var url = Url() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getedititem(o_Id);  
	   },
	   error: handleAjaxError
	});
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
			$('#add-success-alert').css('display', 'block'); 
			$("#add-success-alert").fadeOut(3000);
			    	clearstorage();

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

function addedititem(){
	var $form = $("#add-order-form");
    var items = toJson($form);
	var url=orderurl()+"/"+o_Id;
	$.ajax({
		url: url,
		type: 'POST',
		data : items,
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			if (data.is_p===0){

			}
			if (data.is_p===2){
				alert("Insufficient inventory for the requested product");
				return;}
			else{
			getedititem(o_Id);
		}
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
 $('#add-div').css("display", "none");
 $('#beditdiv').css("display", "block");
   $('#addfooter').css("display", "none");
  $('#editfooter').css("display", "block");
}

function displayEditorder(data){
 $("#edit-view-form input[name=barcode]").val(data.barcode);
  $("#edit-view-form input[name=quantity]").val(data.quantity);
 $("#edit-view-form input[name=price]").val(data.price);
 $("#edit-view-form input[name=id]").val(data.id);
 quantity_v=data.quantity;
   $('#whole').css("display", "none");
  $('#editdiv').css("display", "block"); 
    $('#aftereditfooter').css("display", "block"); 

}

function showtable(){
	var data=JSON.parse(sessionStorage.getItem("itemlist"));
	var $tbody = $('#item-table').find('tbody');
	$tbody.empty();
	var j=0;
	for(var i in data){
		var e = (data[i]);
		var buttonHtml = ' <button button="button" class="btn-sm btn-primary" onclick="displayEdititem(' + i + ')"><i class="fa-solid fa-pen-to-square"></i></button>'
		buttonHtml+=' <button button="button" class="btn-sm btn-primary" onclick="deleteitem(' + i + ')"><i class="fa fa-trash" aria-hidden="true"></button>'
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
	$('#from_date').val(data[0].time.slice(0,11))
	console.log(data[0].time.slice(0,11))
	for(var i in data){
		var e = (data[i]);
		e.time=e.time.replace('T',"   ");
		if (e.invoiceGenerated===true){
            var buttonHtml3=' <button type="button" id="orderedit'+e.id+'" onclick="gm(' + e.id + ')" class="btn btn-primary" disabled='+ e.invoiceGenerated +'><i class="fa-solid fa-pen-to-square"></i></button>'
            var buttonHtml2=' <button type="button" class="btn btn-primary" onclick="downloadPDF(' + e.id + ')">Download Invoice</button>'

		}
		else{
			var buttonHtml3=' <button type="button" id="orderedit'+e.id+'" onclick="gm(' + e.id + ')" class="btn btn-primary"><i class="fa-solid fa-pen-to-square"></i></button>'
			var buttonHtml2=' <button type="button" class="btn btn-primary" onclick="downloadPDF(' + e.id + ')">Invoice Generation</button>'
		}
		var buttonHtml1 = ' <button type="button" class="btn btn-primary" onclick="getitem(' + e.id +')">view</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.time + '</td>'
		+ '<td>' + buttonHtml1 +'</td>'
		+ '<td>' + buttonHtml3 +'</td>'
		+ '<td>' + buttonHtml2 +'</td>';
        $tbody.append(row);
	}
	 	$tableSel=$('#order-table').DataTable({"columnDefs": [
        { "targets": [0,2,3], "searchable": false }
    ],
        pageLength : 8,
        autoWidth: true,

    lengthMenu: [[8, 10, 20, -1], [8, 10, 20, 'All']]});
	 $("#order-table_wrapper").css("padding-left","0");
	 $("#order-table_wrapper").css("margin-left","0");

}

function displayOrderitem(data){
    var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	var j=0;
	for(var i in data){
		var e = (data[i]);
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.price*e.quantity + '</td>'
        $tbody.append(row);
	}
}

function displayeditOrderitem(data){
	console.log(data);
    var $tbody = $('#edit-item-table').find('tbody');
	$tbody.empty();
	var j=0;
	for(var i in data){
		var e = (data[i]);
		var buttonHtml = ' <button button="button" class="btn-sm btn-outline-info" onclick="getitemid(' + data[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button>'
		buttonHtml += ' <button button="button" class="btn-sm btn-outline-info" onclick="deleteitemid(' + data[i].id + ')"><i class="fa fa-trash" aria-hidden="true"></i></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.price*e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
        $tbody.append(row);
	}
				  $('#editdiv').css("display", "none");
				  $('#aftereditfooter').css("display", "none");
               $('#whole').css("display", "block");
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
      	$('#orderedit'+id+'').attr("disabled",true);
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

  $('#filter').on('click', function(e){
    e.preventDefault();
    var startDate = $('#from_date').val();
     var endDate = $('#to_date').val();
     $.fn.dataTableExt.afnFiltering.length = 0;
    filterByDate(1, startDate, endDate); // We call our filter function
    $('#order-table').dataTable().fnDraw(); // Manually redraw the table after filtering
  });
  
var filterByDate = function(column, startDate, endDate) {
  // Custom filter syntax requires pushing the new filter to the global filter array
   var start = normalizeDate(startDate);
    var end = normalizeDate(endDate);
		$.fn.dataTableExt.afnFiltering.push(
		   	function( oSettings, aData, iDataIndex ) {
		   	  var rowDate = aData[column].slice(0,10);
          // If our date from the row is between the start and end
          if (start <= rowDate && rowDate <= end) {
            return true;
          } else if (rowDate >= start && end === '' && start !== ''){
            return true;
          } else if (rowDate <= end && start === '' && end !== ''){
            return true;
          } else {
            return false;
          }
        }
		);
	};

// converts date strings to a Date object, then normalized into a YYYYMMMDD format (ex: 20131220). Makes comparing dates easier. ex: 20131220 > 20121220
var normalizeDate = function(dateString) {
  var date = new Date(dateString);
  var normalized = date.getFullYear() + '-' + (("0" + (date.getMonth() + 1)).slice(-2)) + '-' + ("0" + date.getDate()).slice(-2);
  console.log(normalized);
  return normalized;
}

function gm(id){
	$('#view-edit-modal').modal('toggle');
	getedititem(id);

}

function init(){
	clearstorage();
	$("#add-ord").click(function(){
		$('#add-div').css("display", "block");
		console.log("h");
        $('#beditdiv').css("display", "none");
        $('#addfooter').css("display", "block");
        $('#editfooter').css("display", "none");
		$('#add-inventory-modal').find('.modal-title').text("Add Order")
		$('#add-inventory-modal').modal({backdrop: 'static', keyboard: false});
		showtable();
	});
	$("#add-items").click(additems);
	$("#edit-button").click(function(){
         updateitem();
	});
    $("#add-edit-items").click(addedititem)
	$("#edit-btn").click(edititem);
	$('#add-items').attr("disabled",true);
	$('#order-form').on('input change',button);
    $('#submit').click(addorder)
    $("#clear").click(clearstorage);
    $("#closesign").click(clearstorage);
     $("#refresh-data").click(getorder);

}
$(document).ready(init);
$(document).ready(getorder);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#ord-nav").addClass("active");
});

  
