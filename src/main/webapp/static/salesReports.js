function getbrandUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}




function getReportsUrl(){
  var baseUrl = $("meta[name=baseUrl]").attr("content")
  return baseUrl + "/api/reports";
}

function invoiceUrl(){
var baseUrl = $("meta[name=baseUrl]").attr("content");
return baseUrl + "/api/sales-report/pdf"
}


function showdropdown(){
       console.log("show");
       var url=getbrandUrl();
       console.log(url);
       $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
        displaydropdown(data);
       },
       error: handleAjaxError
    });
}

function displaydropdown(data){
    var b=new Set();
    var c=new Set();
    $('#brandInputReports').empty();
    $('#categoryInputReports').empty();
    var p=$("<option />");
    p.html("Select");
    p.val("none");
        var q=$("<option />");
    q.html("Select");
    q.val("none");
    $('#categoryInputReports').append(q);
    $('#brandInputReports').append(p);
    for(var i in data){
        var e = data[i];
        if (!b.has(e.brand)){
                  var br = e.brand;
                  var p=$("<option />");
                  p.html(br);
                  p.val(br);
                  $("#brandInputReports").append(p);
                  b.add(e.brand);
        }
        if (!c.has(e.category)){
                  var cat = e.category;
                  var q=$("<option />")
                  q.html(cat);
                  q.val(cat)
                  $("#categoryInputReports").append(q);
                  c.add(e.category);
        }
}
      $("#brandInputReports").selectpicker('refresh');
      $("#brandInputReports").val($('#brandInputReports option:first').val());
      $("#brandInputReports").selectpicker('refresh');
      $("#categoryInputReports").selectpicker('refresh');
      $("#categoryInputReports").val($('#categoryInputReports option:first').val());
      $("#categoryInputReports").selectpicker('refresh');
}


function salesReport(){
    let toDate = new Date(document.getElementById("toDate").value.trim());
    let fromDate = new Date(document.getElementById("fromDate").value.trim());
    let brand = document.getElementById("brandInputReports").value.trim();
    let category = document.getElementById("categoryInputReports").value.trim();
    var json=JSON.stringify({"to": toDate.toISOString(),"from": fromDate.toISOString(), "brand":brand, "category":category})
    var url = getReportsUrl() + "/salesReport";
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
    console.log(data);
    for(var i in data){
        var e = data[i];
        var row = '<tr>'
        + '<td>' + e.brand + '</td>'
        + '<td>' + e.category + '</td>'
        + '<td>'  + e.count + '</td>'
        + '<td>'  + e.revenue + '</td>'
        + '</tr>';
        $tbody.append(row);
        totalQuantity += e.count;
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

function downloadPDF() {
    var url = invoiceUrl();
    let toDate = new Date(document.getElementById("toDate").value.trim());
    let fromDate = new Date(document.getElementById("fromDate").value.trim());
    let brand = document.getElementById("brandInputReports").value.trim();
    let category = document.getElementById("categoryInputReports").value.trim();
    var json=JSON.stringify({"to": toDate.toISOString(),"from": fromDate.toISOString(), "brand":brand, "category":category})
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
        link.download="Sales_Report_" + new Date() + ".pdf";
        link.click();
       },
       error: function(response){
         toastr.options.timeOut = 0;
    toastr.error("Brand "+brand+" and Category "+category+" combination does not exist");    
       }
    });
}

function init() {
    $('#salesReportBtn').click(function(){salesReport(); });
    var date = new Date();
    var today = new Date(new Date().setDate(date.getDate() ));
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
    document.getElementById("fromDate").max=(today);
    $('#fromDate').on("change",function(){document.getElementById("toDate").min=$('#fromDate').val()
})
    $('#salesbutton').click(salesReport);
    $("#download").click(downloadPDF);
}



$(document).ready(init);
$(document).ready(showdropdown);
$(document).ready(salesReport);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#rep-nav").addClass("active");
});
