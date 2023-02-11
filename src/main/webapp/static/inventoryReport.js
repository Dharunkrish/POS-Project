
function getReportsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports";
}

function invoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/inventory-report/pdf";
}


function salesReport(){
    var url = getReportsUrl() + "/inventoryReport";
    $.ajax({
        contentType: 'application/json',
        url: url,
        type: 'GET',
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
    console.log(data);
    for(var i in data){
        var e = data[i];
        var row = '<tr>'
        + '<td>' + e.brand + '</td>'
        + '<td>' + e.category + '</td>'
        + '<td>'  + e.count + '</td>'
        + '</tr>';
        $tbody.append(row);
        totalQuantity += e.count;
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
            + '</tr>';
    $tbody.append(row);
}

function downloadPdf() {
    var url = invoiceUrl();
    $.ajax({
       url: url,
       type: 'POST',
        xhrFields: {
        responseType: 'blob'
     },
       success: function(blob) {
        var link=document.createElement('a');
        link.href=window.URL.createObjectURL(blob);
        link.download="Inventory_Report_" + new Date() + ".pdf";
        link.click();
       },
       error: function(response){
            handleAjaxError(response);
       }
    });
}

function init() {
    $('#salesReportBtn').click(function(){salesReport(); });
    $("#download").click(downloadPdf);
}



$(document).ready(init);
$(document).ready(salesReport);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#rep-nav").addClass("active");
});