function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getbrandUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}

function invoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content");
return baseUrl + "/api/report/pdf"
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
       var url=getbrandUrl();
       $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
        displaydropdown(data);
       },
       error: handleAjaxError
    });
}

function showcategorydd(){
       var url=getbrandUrl()+'/category/'+brand;
       $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
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
    var json=JSON.stringify({"to": toDate.toISOString(),"from": fromDate.toISOString()})
        var url = getReportsUrl() + "/daySalesReport";

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
    var ta=$('#SalesReport-table').DataTable({pageLength : 6,
        autoWidth: true,
    lengthMenu: [[6, 10, 20, -1], [6, 10, 20, 'All']]});
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
    new $.fn.dataTable.FixedHeader(ta);

}

function downloadPDF() {
    var url = invoiceUrl();
    let toDate = new Date(document.getElementById("toDate").value.trim());
    let fromDate = new Date(document.getElementById("fromDate").value.trim());
    var json=JSON.stringify({"to": toDate.toISOString(),"from": fromDate.toISOString()})
    $.ajax({
       url: url,
       type: 'POST',
        data:json,
         headers: {
                'Content-Type': 'application/json'
               },
        xhrFields: {
        responseType: 'blob'
     },
       success: function(blob) {
        var link=document.createElement('a');
        link.href=window.URL.createObjectURL(blob);
        link.download="Report_" + new Date() + ".pdf";
        link.click();
       },
       error: function(response){
            handleAjaxError(response);
       }
    });
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
    $("#download").click(downloadPDF);
}


$(document).ready(init);
$(document).ready(salesReport);
$(document).ready(showdropdown);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#rep-nav").addClass("active");
});