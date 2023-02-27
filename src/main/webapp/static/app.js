
//HELPER METHOD

function getRole(){
    var role= $("meta[name=Role]").attr("content");
    if (role==="operator"){
        $("#admin-nav").hide();
        return role;
    }
}

function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function toJsonobject($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data1 = {};
    for(s in serialized){
        data1[serialized[s]['name']] = serialized[s]['value']
    }
    return data1;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
    toastr.options.timeOut = 0;
    toastr.error(response.message);
}

function readFileData(file, callback){
  var config = {
    header: true,
    delimiter: "\t",
    skipEmptyLines: "greedy",
    complete: function(results) {
      callback(results);
      } 
  }
  Papa.parse(file, config);
}

function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	 console.log("s",arr)
  
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}


toastr.options = {
  "closeButton": true,
  "debug": false,
  "progressBar": true,
  "positionClass": "toast-top-full-width",
  "showDuration": "none",
  "hideDuration": "none",
  "timeOut": "none",
  "extendedTimeOut": "none",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut",
  "font-size":  "15px",
  "preventDuplicates": "true"
};

