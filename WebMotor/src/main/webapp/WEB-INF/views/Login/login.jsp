<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Products</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
	</head>
	<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>"/>
	<body class="poppins-regular" onLoad="${onLoad}">
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
        <div class="account-page" style="background-image: url(<c:url value="/resources/images/login.jpg" />)">
            <div class="container">
                    <div class="form-container">
                        <div class="form-btn">
                            <span onclick="login()">Login</span>
                            <span onclick="register()">Register</span>
                            <hr id="indicator">
                            <form:form action="login" id="loginForm" method="post" modelAttribute="user">
	                            <div class="error">${msgLogin}</div>
                                <form:input type="text" placeholder="Username" path="username"/>
                                <form:errors path="username" class="error" element="div"/>
                                <form:input type="password" placeholder="Password" path="password"/>
                                <form:errors path="password" class="error" element="div"/>
                                <button type="submit" class="btn">Login</button>
                                <a href="forgetPassword">Forgot password?</a>
                            </form:form>
                            <form:form action="register" id="registerForm" method="post" modelAttribute="user">
                            	<div class="error">${msgRegister}</div>
                                <form:input type="text" placeholder="Username" path="username"/>
                                <form:errors path="username" class="error" element="div"/>
                                <form:input type="password" placeholder="Password" path="password"/>
                                <form:errors path="password" class="error" element="div"/>
                                <form:input type="text" placeholder="Email (Optional)" path="email"/>
                                <form:errors path="email" class="error" element="div"/>
                                <button type="submit" class="btn">Register</button>
                            </form:form>
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
        <script>
            var LoginForm = document.getElementById("loginForm");
            var RegisterForm = document.getElementById("registerForm");
            var Indicator = document.getElementById("indicator");

            function register(){
                RegisterForm.style.transform = "translateX(0px)";
                LoginForm.style.transform = "translateX(0px)";
                Indicator.style.transform = "translateX(105px)";
            }
            function login(){
                RegisterForm.style.transform = "translateX(-320px)";
                LoginForm.style.transform = "translateX(-330px)";
                Indicator.style.transform = "translateX(0px)";
            }
        </script>
	</body>
</html>

