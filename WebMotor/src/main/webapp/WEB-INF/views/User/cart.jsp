<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>My cart</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
	<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" >
	<link href="<c:url value="/resources/css/cart.css"/>" rel="stylesheet" >	
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
			</div>
		</div>
		<!-- cart -->
		<div style="text-align: center; color: red; font-size: 14px; margin-top: 10px; margin-bottom: 10px;">${errorOrderMsg}</div>
        <div class="small-container cart-page">
            <table class="cart-table">
                <tr>
                    <th>Products</th>
                    <th>Quantity</th>
                    <th>Subtotal</th>
                </tr>
                <c:set var="subTotal" value="0"/>
                <c:forEach var="item" items="${cartItems}">
                	<c:set var="subTotal" value="${subTotal + item.amount * item.price * 1000}"/>
	                <tr>
	                    <td>
	                        <div class="cart-info">
	                            <img src="<c:url value="/resources/vehicles/${item.picture}"/>">
	                            <div>
	                                <p>${item.name}</p>
	                                <small>
	                                	Price: <fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price * 1000}"/>
	                                </small>
	                                <br>
	                                <a href="remove?productId=${item.code}">Remove</a>
	                            </div>
	                        </div>
	                    </td>
	                    <td>
	                    	<c:if test="${item.code eq itemError}">	                    	
			                    <p style="color: red; font-size: 14px;">${errorMsg}</p>
	                    	</c:if>
	                        <form action="changeAmount?itemCode=${item.code}" method="post">
	                            <input style="width: 20%; font-size: 17px;"
	                            value="${item.amount}"
	                            name="amount"
	                            type="number" 
	                            min="1" 
	                            onkeypress="return (event.charCode == 13 || event.charCode != 8 && event.charCode == 0 || (event.charCode >= 48 && event.charCode <= 57))">
	                        </form>
	                    </td>
	                    <td>
	                    	<fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.amount * item.price * 1000}"/>
	                    	<br>
	                    	<p>
	                    		SubTotal + <fmt:formatNumber type="percent" value="${vat}"/> VAT:
	                    		<fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.amount * item.price * 1000 * (1 + vat)}"/>
	                    	</p>
	                    </td>
	                </tr>                	
                </c:forEach>
            </table>
            <div class="total-price">
                <table>
                    <tr>
                        <td>Subtotal</td>
                        <td>
                        	<fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${subTotal}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>VAT</td>
                        <td>
                        	<fmt:formatNumber type="percent" value="${vat}"/>
                        </td>
                    </tr>
                    <tr  style="border-bottom: 3px solid black;">
                        <td>Total</td>
                        <td>
                        	<fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${subTotal * (1 + vat)}"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="${subTotal eq 0 ? 2 : 0}">
                        	<a href="history" class="btn" style="width: 100%; border: 0px; text-align: center;">Order history</a>
                        </td>
                        <td style="display: ${subTotal eq 0 ? "none" : "block"};">
                        	<a href="createOrder" class="btn" style="width: 100%; border: 0px; text-align: center;">Create order</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <!-- footer -->
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

