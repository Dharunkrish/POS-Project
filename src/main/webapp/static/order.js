var o_Id=0;
var quantity_v=0;
var ordeer=new Set();
;
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
json.price=parseFloat(json.price).toFixed(2)
json['id']=len();
const items=JSON.parse(sessionStorage.getItem("itemlist"));
for (var i in items){
	if (items[i].barcode==json.barcode.trim()){
		toastr.error("Order for product already exist");
		return;
	}}
var url=Url()+ '/supervisor/check';
var data1=JSON.stringify(json);
$.ajax({
	   url: url,
	   type: 'POST',
	   data: data1,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
	   		json["name"]=data.name;
	   		items.push(json);
            sessionStorage.setItem("itemlist",JSON.stringify(items));
            document.getElementById("order-form").reset();    
            showtable();
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
var url=Url()+ '/supervisor/check';
var json = toJson($form);
$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
			var d=JSON.parse(json);
			d["name"]=data.name;
		    var items=JSON.parse(sessionStorage.getItem("itemlist"));
			items[parseInt(d.id)]=(d);
			sessionStorage.setItem("itemlist",JSON.stringify(items));
			edittoadditems();
			showtable();
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
var url=Url()+ '/supervisor/'+id;
$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json1,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
         getedititem(o_Id);
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
var url = Url() + "/supervisor/" + id;
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
	var url=orderurl()+"/supervisor";
	$.ajax({
		url: url,
		type: 'POST',
		data : items,
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){

			getorder();
			$('#add-inventory-modal').modal("toggle");
			 clearstorage();
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
	var url=orderurl()+"/supervisor/"+o_Id;
	if (ordeer.has($("#add-order-form input[name=barcode").val())){
		toastr.error("Order for product already exist")
		return;
	}
	$.ajax({
		url: url,
		type: 'POST',
		data : items,
		headers: {
			'Content-Type':'application/json'
		},
		success: function(data){
			getedititem(o_Id);
			 document.getElementById("add-order-form").reset();    
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
 addtoedititems();
}

function displayEditorder(data){
 $("#edit-view-form input[name=barcode]").val(data.barcode);
 $("#edit-view-form input[name=quantity]").val(data.quantity);
 $("#edit-view-form input[name=price]").val(data.price);
 $("#edit-view-form input[name=id]").val(data.id);
 addtoeditorder();
}

function showtable(){
	var data=JSON.parse(sessionStorage.getItem("itemlist"));
	var $tbody = $('#item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = (data[i]);
		var buttonHtml = ' <button button="button" class="btn-sm btn-outline-info" onclick="displayEdititem(' + i + ')"><i class="fa-solid fa-pen-to-square"></i></button>'
		buttonHtml+=' <button button="button" class="btn-sm btn-outline-info" onclick="deleteitem(' + i + ')"><i class="fa fa-trash" aria-hidden="true"></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + (e.price*e.quantity).toFixed(2) + '</td>'
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
		var buttonHtml1 = ' <button type="button" class="btn btn-outline-info btn-sm" onclick="getitem(' + e.id +')">view</button>'
		if (e.invoiceGenerated===true){
			var buttonHtml2=' <button type="button" class="btn btn-outline-info btn-sm" onclick="downloadPDF(' + e.id + ')">Download Invoice</button>'
            var buttonHtml3=' <button type="button" id="orderedit'+e.id+'" onclick="gm(' + e.id + ')" class="btn btn-primary-outline-info btn-sm" disabled='+ e.invoiceGenerated +'><i class="fa-solid fa-pen-to-square"></i></button>'
		}
		else{
			var buttonHtml2=' <button type="button" class="btn btn-outline-info btn-sm" onclick="downloadPDF(' + e.id + ')">Generate Invoice</button>'
			var buttonHtml3=' <button type="button" id="orderedit'+e.id+'" onclick="gm(' + e.id + ')" class="btn btn-primary-outline-info btn-sm"><i class="fa-solid fa-pen-to-square"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.time + '</td>'
		+ '<td>' + buttonHtml1 +'</td>'
		+ '<td>' + buttonHtml3 +'</td>'
		+ '<td>' + buttonHtml2 +'</td>';
        $tbody.append(row);
	}
	    if (getRole()==="operator"){
	    	 var ta=$('#order-table').DataTable({"columnDefs": [
        { "targets": [2,3], "searchable": false },
                { 'visible': false, 'targets': [3] }
    ],
        autoWidth: true,
    lengthMenu: [[10, 20, -1], [10, 20, 'All']]});
	    }
	    else{
 var ta=$('#order-table').DataTable({"columnDefs": [
        { "targets": [2,3], "searchable": false }    ],
        autoWidth: true,
    lengthMenu: [[10, 20, -1], [10, 20, 'All']]});
	    }

    new $.fn.dataTable.FixedHeader(ta);

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
		+ '<td>' + (e.price*e.quantity).toFixed(2) + '</td>'
        $tbody.append(row);
	}
}

function displayeditOrderitem(data){
    var $tbody = $('#edit-item-table').find('tbody');
	$tbody.empty();
	var j=0;
	ordeer=new Set()
	if (data.length===1){
		var e = (data[0]);
		ordeer.add(e.barcode)
		var buttonHtml = ' <button button="button" class="btn-sm btn-outline-info" onclick="getitemid(' + data[0].id + ')"><i class="fa-solid fa-pen-to-square"></i></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + (e.price*e.quantity).toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
        $tbody.append(row);
	}
	else{
	for(var i in data){
		var e = (data[i]);
        ordeer.add(e.barcode)
		var buttonHtml = ' <button button="button" class="btn-sm btn-outline-info" onclick="getitemid(' + data[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button>'
		buttonHtml += ' <button button="button" class="btn-sm btn-outline-info" onclick="deleteitemid(' + data[i].id + ')"><i class="fa fa-trash" aria-hidden="true"></i></button>'
		var row = '<tr>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + (e.price*e.quantity).toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
        $tbody.append(row);
	}
}
edittoaddorder();				 
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
      	getorder();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function edittoadditems(){
		 $('#add-div').css("display", "block");
		 $('#beditdiv').css("display", "none");
		 $('#addfooter').css("display", "block");
		 $('#editfooter').css("display", "none");
		 $('#add-inventory-modal').find('.modal-title').text("Add Order")
}

function addtoedititems(){
		 $('#add-inventory-modal').find('.modal-title').text("Edit Order")
		 $('#add-div').css("display", "none");
		 $('#beditdiv').css("display", "block");
		 $('#addfooter').css("display", "none");
		 $('#editfooter').css("display", "block");
}

function addtoeditorder(){
		 $('#whole').css("display", "none");
	     $('#editdiv').css("display", "block"); 
	     $('#aftereditfooter').css("display", "block"); 
}

function edittoaddorder(){
	 $('#editdiv').css("display", "none");
				  $('#aftereditfooter').css("display", "none");
               $('#whole').css("display", "block");
}

function button_disabing(){
   $('#add-items').attr("disabled",(($('#order-form input[name=barcode]').val().trim()=="" || ($('#order-form input[name=quantity]').val().trim()=="") || ($('#order-form input[name=price]').val().trim()==""))));
   }

function clearstorage(){
	sessionStorage.clear();
	const items=[];
	sessionStorage.setItem("itemlist",JSON.stringify(items));
}

function filter(e){
    var startDate = $('#from_date').val();
     var endDate = $('#to_date').val();
     $.fn.dataTableExt.afnFiltering.length = 0;
    filterByDate(1, startDate, endDate);
    $('#order-table').dataTable().fnDraw();
}
  
var filterByDate = function(column, startDate, endDate) {
   var start = normalizeDate(startDate);
    var end = normalizeDate(endDate);
		$.fn.dataTableExt.afnFiltering.push(
		   	function( oSettings, aData, iDataIndex ) {
		   	  var rowDate = aData[column].slice(0,10);
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

var normalizeDate = function(dateString) {
  var date = new Date(dateString);
  var normalized = date.getFullYear() + '-' + (("0" + (date.getMonth() + 1)).slice(-2)) + '-' + ("0" + date.getDate()).slice(-2);
  return normalized;
}

function gm(id){
	$('#view-edit-modal').modal('toggle');
	getedititem(id);
}

function refresh(){
        $('#from_date').val("")
        $('#to_date').val("")
        $.fn.dataTableExt.afnFiltering.length = 0;
        getorder();
}

function FilterInitialise(){
	var date = new Date();
    var today = new Date(new Date().setDate(date.getDate()));
    var last = new Date(new Date().setDate(date.getDate() - 365));
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); 
    var yyyy = today.getFullYear();
    today = yyyy + "-" + mm + "-" + dd;

    dd = String(last.getDate()).padStart(2, '0');
    mm = String(last.getMonth() + 1).padStart(2, '0'); 
    yyyy = last.getFullYear();
    last = yyyy + "-" + mm + "-" + dd;
       $('#to_date').val(today);
    $('#from_date').val(last);
    document.getElementById("from_date").min=last;
    document.getElementById("from_date").max=today;
    document.getElementById("to_date").max=today;
    filter();
    $('#from_date').on("input",function(){
      document.getElementById("to_date").min=$('#from_date').val();
    })
}

function init(){
	clearstorage();
	$("#add-ord").click(function(){
		$('#add-inventory-modal').modal({backdrop: 'static', keyboard: false});
		edittoadditems();
		showtable();
	});
    FilterInitialise()
	$("#add-items").click(additems);
	$("#edit-button").click(updateitem);
	$("#cancel").click(edittoadditems	);
	$("#editcancel").click(function(){
	$('#whole').css("display", "block");
    $('#editdiv').css("display", "none"); 
    $('#aftereditfooter').css("display", "none")})
    $("#add-edit-items").click(addedititem)
	$("#edit-btn").click(edititem);
	$('#add-items').attr("disabled",true);
	$('#order-form').on('input change',button_disabing);
    $('#submit').click(addorder)
    $("#clear").click(clearstorage);
    $("#closesign").click(clearstorage);
    $('#filter').on('click', filter);
    $("#refresh-data").click(refresh);
    if (getRole()==="operator"){
    	$("#add-ord").css("display","none");
    }
}

$(document).ready(init);
$(document).ready(getorder);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#ord-nav").addClass("active");
});

  
