<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Products</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" >
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
<script src="https://kit.fontawesome.com/13e4082d8b.js"></script>
</head>
<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />
<link rel="stylesheet" href="<c:url value="/resources/css/email.css"/>" />
<body class="poppins-regular">
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
		</div>
	</div>
	<!-- account -->
	<div class="account-page-custom" style="background-image: url(<c:url value="/resources/images/login.jpg" />)">
		<div class="container">
			<div class="form-container-custom" style="height: 220px;">
				<div class="form-btn">
					<div style="color: red; font-size: 14px;">${msg}</div>
					<form action="sendEmail" method="post">
						<input type="email" placeholder="Type email" name="email" value="${email}">
						<button type="submit" class="btn">Send authentication email</button>
					</form>
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
						<img src="<c:url value="/resources/images/app-store.png" />"> 
						<img src="<c:url value="/resources/images/play-store.png" />">
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
</body>
</html>

