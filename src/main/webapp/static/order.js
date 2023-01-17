

function additems(){
var $form = $("#order-form");
console.log($('#order-form'));
var json = toJson($form);
const items=JSON.parse(sessionStorage.getItem("itemlist"));
console.log(typeof(items));
console.log(Array.isArray(items));
console.log(json);
items.push(json);
sessionStorage.setItem("itemlist",JSON.stringify(items));
console.log(items);
showtable();
}

function showtable(){
	var data=JSON.parse(sessionStorage.getItem("itemlist"));
	console.log(data);
	var $tbody = $('#item-table').find('tbody');

	$tbody.empty();
	for(var i in data){
		var e = JSON.parse(data[i]);
		console.log(e);
		var buttonHtml = ' <button onclick="displayEdititem(' + e.id + ')">edit</button>'
		buttonHtml+=' <button onclick="deleteitem(' + e.id + ')">delete</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + "Hello" + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.price + '</td>'
		+ '<td>' + buttonHtml +'</td>';
        $tbody.append(row);
	}
}


function init(){
	$("#add-ord").click(function(){
		$("#add-inventory-modal").modal("toggle");
		const items=[];
		sessionStorage.clear();
		sessionStorage.setItem("itemlist",JSON.stringify(items));
		console.log(sessionStorage.getItem("itemlist"));
	})

	$("#add-items").click(additems);

}
$(document).ready(init);

