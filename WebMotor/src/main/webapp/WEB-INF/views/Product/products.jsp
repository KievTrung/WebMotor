<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Products</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />
	<link rel="stylesheet" href="<c:url value="/resources/css/product.css"/>" />	
	</head>
	<body class="poppins-regular">
			<div class="container" style="box-shadow: 0 0 30px 0 rgba(0, 0, 0, 0.1);">
				<div class="navbar">
					<div>
						<h1>Revzone</h1>
					</div>
					<nav>
						<ul>
							<li><a href="index">Home</a></li>
							<li><a href="<c:url value="/profile"/>">${empty account ? "Account" : account.loginName}</a></li>
						</ul>
					</nav>
					<a href="cart?id=${account.id}">
						<i class="fa-solid fa-cart-shopping" style="color: black;"></i>
					</a>
					<div class="items" style="visibility:${empty account || items eq 0 ? "collapse" : "visible"};">${items}</div>
				</div>
			</div>
		<!-- product -->
		<div class="small-container">
			<div class="row row-2">
		<!--------------------FILTERING ------------------------------->
				<h2>${vehicleType} products</h2>
				<form action="filter" method="post">
					<div class="filter">
						<input type="text" class="text" name="key" placeholder="Type keyword" value="${keyword}">
						<select name="type">
							<c:forEach var="item" items="${productTypeList}">
								<option ${vehicleType eq item ? "selected" : ""} >${item}</option>
							</c:forEach>
						</select>
						<select name="sort">
							<option value="LOW2HIGH" ${l2h}>low to high</option>
							<option value="HIGH2LOW" ${h2l}>high to low</option>
							<option value="A2Z" ${a2z}>A to Z</option>
							<option value="Z2A" ${z2a}>Z to A</option>
						</select>
						<input type="submit" class="custom-btn" value="Search">
					</div>
				</form>
			</div>
			<!--------------------PAGINATION ------------------------------->
			<div style="font-size: 20px; color: gray; text-align: center; visibility: ${numberOfRecords eq 0 ? "visible"  : "collapse"};">
				No product found
			</div>
			<c:forEach var="row" items="${productList}">
					<div class="row">
						<c:forEach var="col" items="${row}">
							<a href="productDetail?code=${col.code}">
								<div class="col-4 feature">
									<img src="<c:url value="/resources/vehicles/${col.picture}"/>">
									${col.name}
									<p><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${col.price * 1000}"/></p>
								</div>
							</a>
						</c:forEach>
					</div>
			</c:forEach>
			<!--------------------PAGE INDEXING ---------------------------->
			<div class="page-btn" style="visibility: ${numberOfRecords eq 0 ? "hidden"  : "visible"};">
				<c:forEach var="index" begin="0" end="${numberOfPage - 1}">
					<c:if test="${index eq pageIndex}">					
						<a href="pageIndex?i=${index}">
							<span style="background-color: black; color: white;">${index}</span>
						</a>
					</c:if>
					<c:if test="${index ne pageIndex}">					
						<a href="pageIndex?i=${index}"> 
							<span>${index}</span> 
						</a>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="footer">
			<div class="container">
				<div class="row">
					<div class="footer-col-1">
						<h3>Dowload our app</h3>
						<p>Dowload app for android and mobile phone</p>
						<div class="app-logo">
							<img src="images/app-store.png">
							<img src="images/play-store.png">
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

