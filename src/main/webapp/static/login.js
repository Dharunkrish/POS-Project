	$(document).ready(function(){
document.getElementById("login-form").reset();
$('#login-form').on("input change",button);
button();
})

function button(){
  let pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
   $('#loginbtn').attr("disabled",(($('#login-form input[name=email]').val().trim()=="") || ($('#login-form input[name=password]').val().trim()=="") || (!(input.value.match(pattern)))));
   }

function check(){
let pattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
              console.log("k");
              if(input.value === ""){
                emailIcon.classList.replace("bx-check-circle", "bx-envelope");
                emailIcon.style.color = "#b4b4b4";
                $('#loginbtn').attr("disabled",false)
              }
              else if(input.value.match(pattern)){
                emailIcon.classList.replace("bx-envelope", "bx-check-circle");
                emailIcon.style.color = "#4bb543"
                $('#loginbtn').attr("disabled",false)

              }
              else{
              emailIcon.classList.replace("bx-check-circle", "bx-envelope");
              emailIcon.style.color = "#de0611"
              $('#loginbtn').attr("disabled",true)
            }

            }
$(document).ready(t);
function t(){
  const form=document.querySelector("form");
  input=form.querySelector("#email")
    password=form.querySelector("#password")

    emailIcon=form.querySelector(".bx-envelope")
    passwordIcon=form.querySelector(".bx-hide")

    warning=form.querySelector(".warning")
  input.addEventListener("keyup",check);
  passwordIcon.addEventListener("click",function(){
   if (password.type==="text"){
       passwordIcon.classList.replace("bx-show","bx-hide");
   password.type="password";
   }
   else{
   passwordIcon.classList.replace("bx-hide","bx-show");
   password.type="text";
  }
})}
