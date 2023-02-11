
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/user";
}

//BUTTON ACTIONS
function addUser(event){
	//Set the values to update
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});

	return false;
}

function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);   
	   },
	   error: handleAjaxError
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();    
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayUserList(data){
	console.log('Printing user data');
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		if (e.role==="supervisor"){
			var buttonHtml = ' <button type="button" class="btn-sm btn-outline-info" onclick="displayEditUser(' + e.id + ')"><i class="fa-solid fa-pen-to-square"></i></button>';
		}
		else{
		var buttonHtml = ' <button type="button" class="btn-sm btn-outline-info" onclick="displayEditUser(' + e.id + ')" style="margin-right:5px"><i class="fa-solid fa-pen-to-square"></i></button>'
		buttonHtml += '<button type="button" class="btn-sm btn-outline-info" onclick="deleteUser(' + e.id + ')" ><i class="fa fa-trash" aria-hidden="true"></i></button>'

	}
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>' +e.role  + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}

}


//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	$('#refresh-data').click(getUserList);
}

$(document).ready(init);
$(document).ready(getUserList);
$(document).ready(function(){
   $(".active").removeClass("active");
   $("#admin-nav").addClass("active");
});

