<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>WebMotor</title>
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
	<script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
	<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" >
	<link href="<c:url value="/resources/css/index.css"/>" rel="stylesheet" >	
</head>
<body class="poppins-regular">
	<!-- header -->
	<div class="header" style="background-image: url(<c:url value="/resources/vehicles/${indexProduct.picture}"/>)">
		<div class="container">
			<div class="navbar">
				<div>
					<h1>Revzone</h1>
				</div>
				<nav>
					<ul>
						<li><a href="index">Home</a></li>
						<li><a href="product">Products</a></li>
						<li><a href="<c:url value="/profile"/>">${empty account ? "Account" : account.loginName}</a></li>
					</ul>
				</nav>
				<a href="cart?id=${account.id}">
					<i class="fa-solid fa-cart-shopping" style="color: black;"></i>
				</a>
				<div class="items" style="visibility:${empty account || items eq 0 ? "collapse" : "visible"};">${items}</div>
			</div>
			<div class="row">
				<div class="col-2">
					<div class="box" style="width: 250px">
						<h2>${indexProduct.name}</h2>
						<h3>${indexProduct.description}</h3>
						<a href="productDetail?code=${indexProduct.code}" class="btn">Explore now &#10146;</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- categories -->
	<div class="categories">
		<div class="small-container">
			<h2 class="title">Categories</h2>
			<c:forEach var="list" items="${categoryList}">
				<div class="row">
					<c:forEach var="item" items="${list}">
						<a href="category?category=${item.type}">
							<div class="col-4 category">
									<img src="<c:url value="/resources/vehicles/${item.picture}"/>">
								${item.type}
							</div>
						</a>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
	</div>
	<!-- product -->
	<div class="small-container">
		<h2 class="title">Feature Products</h2>
		<c:forEach var="list" items="${featureList}">
			<div class="row">
				<c:forEach var="item" items="${list}">
					<a href="productDetail?code=${item.code}">
						<div class="col-4 feature">
								<img src="<c:url value="/resources/vehicles/${item.picture}"/>">
							${item.name}
							<p><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price * 1000}"/></p>
						</div>
					</a>
				</c:forEach>
			</div>
		</c:forEach>
	</div>
	<!-- offer -->
	<div class="offer">
		<div class="small-container">
			<h2 class="title_">Special offer</h2>
			<div class="row">
				<div class="col-2">
					<div class="image-border">
						<img src="<c:url value="/resources/vehicles/${specialProduct.picture}"/>" class="offer-image">
					</div>
				</div>
				<div class="col-2">
					<h1>${specialProduct.name}</h1>
					<p style="font-weight: 600;"> 
						${specialProduct.description}
					</p> 
					<a class="btn" href="productDetail?code=${specialProduct.code}">Explore now &#10146;</a>
				</div>
			</div>
		</div>
	</div>
	<!-- news -->
	<div class="news">
		<div class="small-container">
			<h2 class="title">Best seller</h2>
			<div class="row">
				<c:forEach var="item" items="${bestSeller}">
					<div class="col-4">
						<img src="images/new-1.jpg">
						<p>news heading</p>
						<a class="btn">More info &#10146;</a>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<!-- brands -->
	<div class="brands">
		<h2 class="title">Special coporating</h2>
		<div class="small-container">
			<div class="row">
				<div class="col-5">
					<img src="<c:url value="/resources/images/brand0.jpg"/>">
				</div>
				<div class="col-5">
					<img src="<c:url value="/resources/images/brand1.jpg"/>">
				</div>
				<div class="col-5">
					<img src="<c:url value="/resources/images/brand2.jpg"/>">
				</div>
				<div class="col-5">
					<img src="<c:url value="/resources/images/brand3.jpg"/>">
				</div>
				<div class="col-5">
					<img src="<c:url value="/resources/images/brand4.jpg"/>">
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
						<img src="<c:url value="/resources/images/play-store.png"/>">
						<img src="<c:url value="/resources/images/app-store.png"/>">
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

