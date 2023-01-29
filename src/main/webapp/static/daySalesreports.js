function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getbrandUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}




function getReportsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports";
}

function getInventoryReport(){
	var url = getInventoryUrl() + "/report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryReport(data);
	   },
	   error: handleAjaxError
	});
}


function showdropdown(){
       console.log("show");
       var url=getbrandUrl();
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

function showcategorydd(){
       console.log("show");
       var url=getbrandUrl()+'/category/'+brand;
       console.log(url);
       $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
        console.log(data);
        displaycategorydd(data);
       },
       error: handleAjaxError
    });
}

function displaydropdown(data){
    $('#idbrand').empty();
    var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#idbrand').append(p);
    for(var i in data){
        var e = data[i];
        console.log(e);
        var row = e.brand;
        var p=$("<option />");
        p.html(row);
        p.val(row);
      $("#idbrand").append(p);
}
}

function displaycategorydd(data){
    $('#idcategory').empty();
    var p=$("<option />");
    p.html("Select");
    p.val("none");
    $('#idcategory').append(p);
    for(var i in data){
        var e = data[i];
        console.log(e);
        var row = e.category;
        var p=$("<option />");
        p.html(row);
        p.val(row);
      $("#idcategory").append(p);
}
}

function salesReport(){
    let toDate = new Date(document.getElementById("toDate").value.trim());
    let fromDate = new Date(document.getElementById("fromDate").value.trim());
    console.log(toDate);
    console.log(fromDate);
    var url = getReportsUrl() + "/daySalesReport";
    var json=JSON.stringify({"to": toDate.toISOString(),"from": fromDate.toISOString()})
    $.ajax({
        contentType: 'application/json',
        url: url,
        type: 'POST',
        data:json,
         headers: {
        'Content-Type': 'application/json'
       },   
        success: function(data){
            displaySalesReport(data);
        },
        error: handleAjaxError
    });
}

function displaySalesReport(data) {
    var $tbody = $('#SalesReport-table').find('tbody');
    $tbody.empty();
    var totalQuantity = 0;
    var totalRevenue = 0;
    for(var i in data){
        var e = data[i];
        var row = '<tr>'
        + '<td>' + e.date + '</td>'
        + '<td>' + e.total_order + '</td>'
        + '<td>'  + e.total_item + '</td>'
        + '<td>'  + e.revenue + '</td>'
        + '</tr>';
        $tbody.append(row);
        totalQuantity += e.total_item;
        totalRevenue += e.revenue
    }
    $('#SalesReport-table').DataTable();
    var row = '<tr>'
            + '<td>' + '' + '</td>'
            + '<td>' + '' + '</td>'
            + '<td>'  + '' + '</td>'
            + '<td>'  + '' + '</td>'
            + '</tr>';
    $tbody.append(row);
    row = '<tr>'
            + '<td>' + 'Total'.bold() + '</td>'
            + '<td>' + '' + '</td>'
            + '<td>'  + totalQuantity + '</td>'
            + '<td>'  + totalRevenue + '</td>'
            + '</tr>';
    $tbody.append(row);
}


function init() {
    $('#salesReportBtn').click(function(){salesReport(); });
    $('#idbrand').click(showcategorydd);
    var date = new Date();
    var today = new Date(new Date().setDate(date.getDate() + 1));
    var last = new Date(new Date().setDate(date.getDate() - 30));
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();
    today = yyyy + "-" + mm + "-" + dd;

    dd = String(last.getDate()).padStart(2, '0');
    mm = String(last.getMonth() + 1).padStart(2, '0'); //January is 0!
    yyyy = last.getFullYear();
    last = yyyy + "-" + mm + "-" + dd;

    $('#toDate').val(today);
    $('#fromDate').val(last);
}


$(document).ready(init);
$(document).ready(salesReport);
$(document).ready(showdropdown);