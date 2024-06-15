<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Products</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js"></script>
	<link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" />
	<link rel="stylesheet" href="<c:url value="/resources/css/productDetail.css"/>" />	
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
							<li><a href="product">Products</a></li>
							<li><a href="profile">${empty account ? "Account" : account.loginName}</a></li>
						</ul>
					</nav>
					<a href="cart?id=${account.id}">
						<i class="fa-solid fa-cart-shopping" style="color: black;"></i>
					</a>
					<div class="items" style="visibility:${empty account || items eq 0 ? 'collapse' : 'visible'};">${items}</div>
				</div>
			</div>
		<!----------------------------PRODUCT DETAIL-------------------------------------->
		<div class="small-container single-product">
			<div class="row">
				<div class="col-2">
					<img src="<c:url value="/resources/vehicles/${product.picture}"/>" width="100%" id="product-img">
					<div class="small-img-row" id="">
						<c:forEach var="item" items="${productPictures}">
							<div class="small-img-col">
								<img src="<c:url value="/resources/vehicles/${item}"/>" width="100%" class="small-img">
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="col-2">
					<h1>${product.name}</h1>
					<h4><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.price}"/></h4>
					<p style="color: red; font-size: 14px;">${errorMsg}</p>
					<form id="form" action="addCart" method="post">
						<input value="${account.id}" name="id" style="display: none;">
						<input value="${product.code}" name="product" style="display: none;">	
						<input type="number" 
								min="1" 
								value="1" 
								name="amount" 
								style="width: 20%" 
								onkeypress="return (event.charCode !=8 && event.charCode ==0 || (event.charCode >= 48 && event.charCode <= 57))">			
						<br>
						<c:if test="${product.amount > 0}">
							<p style="font-size: 16px">Stocks: ${product.amount}</p>
						</c:if>
					</form>
					<div style="display: flex;">
						<c:if test="${product.amount > 0}">
							<input form="form" type="submit" class="btn-custom" style="width: 150px; font-size:16px" value="Add to cart"> 
						</c:if>
						<c:if test="${product.amount == 0}">
							<h4 style="color: red;">Out of stocks</h4>
						</c:if>
					</div>
					<h3>Product detail:</h3>
					<br>
					<p>${product.description}</p>
				</div>
			</div>
		</div>
		<div class="small-container">
			<div class="row row-2">
				<h2>Related products</h2>
			</div>
		</div>
		<br>
		<br>
		<!----------------------------RELATED PRODUCTS -------------------------------------->
		<div class="small-container">
			<div class="row">
				<c:forEach var="item" items="${relateList}">
					<div class="col-4 feature">
						<a href="productDetail?code=${item.code}">
								<img src="<c:url value="/resources/vehicles/${item.picture}"/>" >
						</a>
						${item.name}
						<p><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price}"/></p>
					</div>
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
		<script>
			var productImg = document.getElementById("product-img")
			var smallImg = document.getElementsByClassName("small-img")

			smallImg[0].onclick = function(){
				productImg.src = smallImg[0].src;
			}
			smallImg[1].onclick = function(){
				productImg.src = smallImg[1].src;
			}
			smallImg[2].onclick = function(){
				productImg.src = smallImg[2].src;
			}
			smallImg[3].onclick = function(){
				productImg.src = smallImg[3].src;
			}
		</script>
	</body>
</html>