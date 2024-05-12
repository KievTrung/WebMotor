<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Profile Page</title>

<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
<script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/css/profile.css"/>" />
</head>
<body class="poppins-regular" onload="${onLoad}">
	<div class="container">
		<div class="navbar">
			<div>
				<h1>Revzone</h1>
			</div>
			<nav>
				<ul>
					<li><a href="index">Home</a></li>
					<li><a href="product">Products</a></li>
				</ul>
			</nav>
			<a href="cart?id=${account.id}">
				<i class="fa-solid fa-cart-shopping" style="color: black;"></i>
			</a>
			<div class="items" style="visibility:${empty account || items eq 0 ? "collapse" : "visible"};">${items}</div>
		</div>
	</div>
	<div class="background" style="background-image: url(<c:url value="/resources/images/login.jpg"/>);">
		<div class="main">
			<div class="card">
				<div class="card-body">
					<form:form id="form" action="profile" modelAttribute="accountChange" method="post">
						<table>
							<tbody>
								<tr>
									<td>Login name</td>
									<td>:</td>
									<td>${account.loginName}</td>
									<td><i onclick="changeLogin()"
										class="fa-regular fa-pen-to-square"></i></td>
								</tr>
								<tr id="tbChange1" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<form:input path="loginName"  placeholder="Type new login name"/>
										<br>
										<form:errors path="loginName" element="div" style="text-align: center; color: red;"/>
									</td>
								</tr>
								<tr>
									<td>Password</td>
									<td>:</td>
									<td>********</td>
									<td><i onclick="changePassword()"
										class="fa-regular fa-pen-to-square"></i></td>
								</tr>
								<tr id="tbChange2" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb"><input type="password" name="old" placeholder="Type old password"/></td>
								</tr>
								<tr id="tbChange2_" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb"><input type="password" name="new" placeholder="Type new password" style="margin-top: 5px;"></td>
								</tr>
								<tr id="tbChange2__" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<input type="password" name="renew" placeholder="Retype new password" style="margin-top: 5px;">
										<br>
										<form:errors path="password" element="div" style="text-align: center; color: red;"/>
										<div style="margin-top: 10px; text-align: center; text-decoration: underline; color: gray;">
											<a href="forgetPassword">Forget password</a>
										</div>
									</td>
								</tr>
								<tr>
									<td>Address</td>
									<td>:</td>
									<td>${account.diaChi}</td>
									<td><i onclick="changeAddress()"
										class="fa-regular fa-pen-to-square"></i></td>
								</tr>
								<tr id="tbChange3" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<form:input path="diaChi" placeholder="Type new address"/>
										<br>
										<form:errors path="diaChi" element="div" style="text-align: center; color: red;"/>
									</td>
								</tr>
								<tr>
									<td>Phone</td>
									<td>:</td>
									<td>${account.phone}</td>
									<td><i onclick="changePhone()"
										class="fa-regular fa-pen-to-square"></i></td>
								</tr>
								<tr id="tbChange4" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<form:input type="text" path="phone" placeholder="Type new phone number"/>
										<br>
										<form:errors path="phone" element="div" style="text-align: center; color: red;"/>
									</td>
								</tr>
								<tr>
									<td>Email</td>
									<td>:</td>
									<td>${account.email}</td>
									<td><i onclick="changeEmail()"
										class="fa-regular fa-pen-to-square"></i></td>
								</tr>
								<tr id="tbChange5" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<form:input type="email" path="email" placeholder="Type new email"/>
										<br>
										<form:errors path="email" element="div" style="text-align: center; color: red;"/>
									</td>
								</tr>
							</tbody>
						</table>
					</form:form>
					<div id="myModal1" class="modal">
						<!-- Modal content -->
						<div class="modal-content">
							<span class="close1">&times;</span>
							<p>Confirm save change?</p>
							<br> <input form="form" type="submit" class="btn_" value="Confirm" />
						</div>
					</div>
					<div id="myModal2" class="modal">
						<!-- Modal content -->
						<div class="modal-content">
							<span class="close2">&times;</span>
							<p>Confirm log out?</p>
							<br> <a href="logout" class="btn_">Confirm</a>
						</div>
					</div>
					<a id="myBtn1" class="btn_" style="margin-top: 30px;">Save change</a> 
					<a id="myBtn2" class="btn_">Log out</a>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<div class="container">
			<div class="row">
				<div class="footer-col-1">
					<h3>Dowload our app</h3>
					<p>Dowload app for android and mobile phone</p>
					<div class="app-logo">
						<img src="<c:url value="/resources/images/app-store.png"/>">
						<img src="<c:url value="/resources/images/play-store.png"/>">
					</div>
				</div>
				<div class="footer-col-2">
					<h1>Revzone</h1>
					<p>Reving your dreams</p>
				</div>
				<div class="footer-col-3">
					<h3>Follow us</h3>
					<ul>
						<li>Facebook</li>
						<li>X</li>
						<li>Instagram</li>
						<li>Youtube</li>
					</ul>
				</div>
			</div>
			<hr>
			<p class="copyright">Copyright all rights reserved</p>
		</div>
	</div>
	<script type="text/javascript">
var tbChange1 = document.getElementById("tbChange1");
var tbChange2 = document.getElementById("tbChange2");
var tbChange2_ = document.getElementById("tbChange2_");
var tbChange2__ = document.getElementById("tbChange2__");
var tbChange3 = document.getElementById("tbChange3");
var tbChange4 = document.getElementById("tbChange4");
var tbChange5 = document.getElementById("tbChange5");

function changeLogin() {
	if (tbChange1.style.visibility == "collapse") {
		tbChange1.style.visibility = "visible";
	} else {
		tbChange1.style.visibility = "collapse";
	}
}

function changePassword() {
	if (tbChange2.style.visibility == "collapse") {
		tbChange2.style.visibility = "visible";
		tbChange2_.style.visibility = "visible";
		tbChange2__.style.visibility = "visible";
	} else {
		tbChange2.style.visibility = "collapse";
		tbChange2_.style.visibility = "collapse";
		tbChange2__.style.visibility = "collapse";
	}
}
function changeAddress() {
	if (tbChange3.style.visibility == "collapse") {
		tbChange3.style.visibility = "visible";
	} else {
		tbChange3.style.visibility = "collapse";
	}
}
function changePhone() {
	if (tbChange4.style.visibility == "collapse") {
		tbChange4.style.visibility = "visible";
	} else {
		tbChange4.style.visibility = "collapse";
	}
}
function changeEmail() {
	if (tbChange5.style.visibility == "collapse") {
		tbChange5.style.visibility = "visible";
	} else {
		tbChange5.style.visibility = "collapse";
	}
}

// Get the modal
var modal1 = document.getElementById("myModal1");

// Get the button that opens the modal
var btn1 = document.getElementById("myBtn1");

// Get the <span> element that closes the modal
var span1 = document.getElementsByClassName("close1")[0];

// When the user clicks the button, open the modal 
btn1.onclick = function() {
	modal1.style.display = "block";
}

span1.onclick = function() {
	modal1.style.display = "none";
}

// Get the modal
var modal2 = document.getElementById("myModal2");

// Get the button that opens the modal
var btn2 = document.getElementById("myBtn2");

// Get the <span> element that closes the modal
var span2 = document.getElementsByClassName("close2")[0];

// When the user clicks the button, open the modal 
btn2.onclick = function() {
	modal2.style.display = "block";
}

span2.onclick = function() {
	modal2.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if (event.target == modal2) {
		modal2.style.display = "none";
	}
	if (event.target == modal1) {
		modal1.style.display = "none";
	}
}
</script>
</body>
</html>