<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Bill</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js"></script>
	</head>
	<link href="<c:url value="/resources/adminCss/index.css"/>" rel="stylesheet" >
	<link href="<c:url value="/resources/adminCss/bills.css"/>" rel="stylesheet" >
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
							<li><a href="profile">${empty account ? "Account" : account.loginName}</a></li>
						</ul>
					</nav>
				</div>
			</div>
		<!-- product -->
		<div class="container">
			<div class="row row-2">
				<h2>Bills</h2>
		<!--------------------FILTERING ------------------------------->
				<form id="form2" action="billFilter" method="post">
					<div class="filter">
						<input type="text" class="text" placeholder="Type code" name="soHoaDon">
						<select name="sort">
							<option value="LOW2HIGH" ${oldest}>Oldest</option>
							<option value="HIGH2LOW" ${latest}>Latest</option>
						</select>
						<select name="type">
							<option value="All" ${All}>All</option>
							<option value="Import" ${Import}>Import</option>
							<option value="Export" ${Export}>Export</option>
						</select>
						<input form="form2" type="submit" class="custom-btn" value="Search">
                        <a href="import" class="custom-btn" style="font-size: small;">Import</a>
					</div>
				</form>
			</div>
            <!--------------------PAGINATION ------------------------------->
            <table>
                <tr>
                    <th style="width: 200px;">Id</th>
                    <th style="width: 200px;">Date</th>
                    <th style="width: 200px;">Type</th>
                    <th style="width: 200px;">User</th>
                    <th style="width: 200px;">Detail</th>
                </tr>
               	<c:forEach var="bill" items="${bills}">
	                <tr>
	                    <td >${bill.soHoaDon}</td>
	                    <td >${bill.ngayTao}</td>
	                    <td >${bill.type}</td>
	                    <td class="detail_">
	                    	<a href="userDetail?name=${bill.userName}">${bill.userName}</a>
	                    </td>
	                    <td class="detail_">
	                    	<a href="billDetail?id=${bill.soHoaDon}&type=${bill.type}">Detail</a>
	                    </td>
	                </tr>
               	</c:forEach>
            </table>
			<div style="font-size: 20px; color: gray; text-align: center; margin-top: 20px; visibility: ${numberOfBill eq 0 ? 'visible' : 'hidden'};">No result</div>
			<!--------------------PAGE INDEXING ---------------------------->
			<div class="page-btn" style="visibility: ${numberOfBill eq 0 ? 'hidden'  : 'visible'};">
				<c:forEach var="index" begin="0" end="${numberOfBillPage - 1}">
					<c:if test="${index eq billPageIndex}">					
						<a href="billPageIndex?i=${index}">
							<span style="background-color: black; color: white;">${index}</span>
						</a>
					</c:if>
					<c:if test="${index ne billPageIndex}">					
						<a href="billPageIndex?i=${index}"> 
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

