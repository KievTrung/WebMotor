<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Bill detail</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" ></script>
		
		<link href="<c:url value="/resources/adminCss/index.css"/>" rel="stylesheet" >
		<link href="<c:url value="/resources/adminCss/billDetail.css"/>" rel="stylesheet" >
	</head>
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
						<li><a href="user">Users</a></li>
						<li><a href="bill">Imports/Exports</a></li>
						<li><a href="profile">${empty account ? "Account" : account.loginName}</a></li>
					</ul>
				</nav>
			</div>
		</div>
		<!-- cart -->
        <div class="container cart-page" style="margin-top: 50px;">
            <h3 class="title">${bill.type}</h3>
            <c:set var="total" value="0" />
            <c:if test="${bill.type eq 'Import'}">
	            <table>
	                <tr>
	                    <th style="width: 120px;">ID</th>
	                    <th style="width: 120px;">Date</th>
	                    <th style="width: 120px;">User</th>
	                    <th style="width: 400px;">Vehicles</th>
	                    <th style="width: 100px;">Quantity</th>
	                    <th>SubTotal</th>
	                </tr>
	                <tr>
	                    <td rowspan="${bill.list.size() + 1}">${bill.soHoaDon}</td>
	                    <td rowspan="${bill.list.size() + 1}">${bill.ngayTao}</td>
	                    <td rowspan="${bill.list.size() + 1}" class="detail"> 
	                    	<a href="">
		                    	${bill.userName}
	                    	</a>
	                    </td>
	                </tr>
	                <c:forEach var="product" items="${bill.list}">
		                <c:set var="total" value="${total + product.amount * product.price}"/>
		                <tr>
		                    <td class="detail">
		                        <a  href="adminProductDetail?id=${product.code}">
		                            <img src="<c:url value="/resources/vehicles/${product.picture}"/>">
		                        </a>
		                        <p>${product.name}</p>
		                    </td>
		                    <td>${product.amount}</td>
		                    <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.price}"/></td>
		                </tr>
	                </c:forEach>
		            <tr>
		               <th colspan="5">Total</th>
		               <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${total}"/></th>
		            </tr>
	            </table>
            </c:if>
            <!------------------------------ Export ------------------------ -->
            <c:if test="${bill.type eq 'Export'}">
	            <table>
	                <tr>
	                    <th style="width: 100px;">ID</th>
	                    <th style="width: 120px;">Date</th>
	                    <th style="width: 150px;">User</th>
	                    <th style="width: 100px;">Payment</th>
	                    <th style="width: 100px;">State</th>
	                    <th style="width: 250px;">Vehicles</th>
	                    <th style="width: 50px;">Quantity</th>
	                    <th style="width: 200px;">Price</th>
	                </tr>
	                <tr>
	                    <td rowspan="${bill.list.size()*2 + 1}">${bill.soHoaDon}</td>
	                    <td rowspan="${bill.list.size()*2 + 1}">${bill.ngayTao}</td>
	                    <td rowspan="${bill.list.size()*2 + 1}" class="detail">
	                    	<a href="userDetail?name=${bill.userName}">${bill.userName}</a>
	                    </td>
	                    <td rowspan="${bill.list.size()*2 + 1}">${bill.paymentMethod}</td>
	                    <td rowspan="${bill.list.size()*2 + 1}">
	                    	${bill.trangThaiThanhToan eq true ? "Paid": "Unpaid"}
	                    	<c:if test="${bill.paymentMethod eq 'INPLACE' }">
		                    	<a class="btn" href="changeState?id=${bill.soHoaDon}&val=${bill.trangThaiThanhToan}">
		                    		${bill.trangThaiThanhToan eq true ? "Unpaid": "Paid"}
		                    	</a>
	                    	</c:if>
	                    </td>
	                </tr>
	                <c:forEach var="product" items="${bill.list}">
	                	<c:set var="total" value="${total + product.amount * product.price}"/>
		                <tr>
		                    <td class="detail">
		                        <a  href="adminProductDetail?id=${product.code}">
		                            <img src="<c:url value="/resources/vehicles/${product.picture}"/>">
		                        </a>
		                        <p>${product.name}</p>
		                    </td>
		                    <td>${product.amount}</td>
		                    <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.price}"/></td>
		                </tr>
		                <tr>
		                    <th colspan="2">SubTotal + ${bill.vat/100} % VAT</th>
		                    <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.amount * product.price  * (1 + bill.vat/100)}"/></th>
		                </tr>
	                </c:forEach>
	                <tr>
	                   <th colspan="7">Total</th>
		               <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${total * (1 + bill.vat/100)}"/></th>
	                </tr>
	            </table>
            </c:if>
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

