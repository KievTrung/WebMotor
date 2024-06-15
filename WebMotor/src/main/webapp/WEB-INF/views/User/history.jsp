<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>History</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" ></script>
	<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" >
	<link href="<c:url value="/resources/css/history.css"/>" rel="stylesheet" >	
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
						<li><a href="profile">${account.loginName}</a></li>
					</ul>
				</nav>
				<a href="cart?id=${account.id}">
					<i class="fa-solid fa-cart-shopping" style="color: black;"></i>
				</a>
				<div class="items" style="visibility:${items eq 0 ? 'collapse' : 'visible'};">${items}</div>
			</div>
		</div>
		<!-- cart -->
        <div class="small-container cart-page">
            <table>
                <tr>
                    <th style="width: 120px;">Order id</th>
                    <th style="width: 120px;">Date</th>
                    <th style="width: 170px;">Payment</th>
                    <th style="width: 150px;">State</th>
                    <th style="width: 400px;">Name</th>
                    <th style="width: 100px;">Quantity</th>
                    <th>SubTotal</th>
                </tr>
                <c:forEach var="bill" items="${exportBills}">
	                <tr>
	                    <td rowspan="${bill.list.size()*2 + 1}">${bill.soHoaDon}</td>
	                    <td rowspan="${bill.list.size()*2 + 1}"><fmt:formatDate type="date" dateStyle="long" value="${bill.ngayTao}" /></td>
	                    <td rowspan="${bill.list.size()*2 + 1}">${bill.hinhThucThanhToan}</td>
	                    <td rowspan="${bill.list.size()*2 + 1}">
	                    	${bill.trangThaiThanhToan eq true ? "Paid": "Unpaid"}
	                    	<br>
	                    	<c:if test="${bill.trangThaiThanhToan eq false}">
	                    		<div id="myModal${bill.soHoaDon}" class="modal">						
						        	<!-- confirm message  -->
						        	<div class="modal-content">
										<span class="close close${bill.soHoaDon}">&times;</span>
										<p>Confirm delete order?</p>
										<br> 
										<a class="btn" href="deleteOrder?orderId=${bill.soHoaDon}">Confirm</a>
									</div>
								</div>
	                    		<a id="myBtn${bill.soHoaDon}" class="btn">Delete</a>
	                    		<script type="text/javascript">	
									// When the user clicks the button, open the modal 
									document.getElementById("myBtn" + ${bill.soHoaDon}).onclick = function() {
											document.getElementById("myModal" + ${bill.soHoaDon}).style.display = "block";
									}
									
									document.getElementsByClassName("close" + ${bill.soHoaDon})[0].onclick = function() {
										document.getElementById("myModal" + ${bill.soHoaDon}).style.display = "none";
									}			
								</script>
	                    		<a class="btn" style="font-size: 14px; margin-top: 5px;" href="repay?orderId=${bill.soHoaDon}">Repay</a>
	                    	</c:if>
	                    </td>
	                </tr>
	                <c:set var="total" value="0" />
	                <c:forEach var="product" items="${bill.list}">
			            <c:set var="total" value="${total + product.amount * product.price  * (1 + bill.vat/100)}"/>
		                <tr>
		                    <td>
		                        <img src="<c:url value="/resources/vehicles/${product.picture}"/>">
		                        <p>${product.name}</p>
		                        <p>price : <fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.price }"/></p>
		                    </td>
		                    <td>${product.amount} </td>
		                    <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.amount * product.price }"/></td>
		                </tr>
		                <tr>
		                    <th colspan="2">SubTotal + ${bill.vat} % VAT</th>
		                    <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.amount * product.price  * (1 + bill.vat/100)}"/></th>
		                </tr>
	                </c:forEach>
	                <tr>
	                    <th colspan="6">Total + ${bill.vat} % VAT</th>
	                    <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${total}"/></th>
	                </tr>
                </c:forEach>
            </table>
        </div>
        <c:if test="${exportBills.isEmpty()}">
        	<div style="text-align: center; font-size: 20px; color: gray; margin-bottom: 150px;">No orders available</div>
        </c:if>
        <!-- footer -->
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

