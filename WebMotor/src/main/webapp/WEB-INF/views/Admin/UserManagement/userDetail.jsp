<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>user detail</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" ></script>
		
		<link href="<c:url value="/resources/adminCss/index.css"/>" rel="stylesheet" >
		<link href="<c:url value="/resources/adminCss/userDetail.css"/>" rel="stylesheet" >
		<style>
        	.detail_, a{
			    transition: 0.5s;
			}
			.detail_:hover, .detail_ a:hover{
			    color: white;
			    background-color: black;
			}
        </style>
	</head>
	<body class="poppins-regular" onload="${msg}">
			<div class="container">
				<div class="navbar">
					<div>
						<h1>Revzone</h1>
					</div>
					<nav>
						<ul>
							<li><a href="index">Home</a></li>
							<li><a href="user">Users</a></li>
							<li><a href="product">Products</a></li>
							<li><a href="bill">Imports/Exports</a></li>
							<li><a href="profile">${empty account ? "Account" : account.loginName}</a></li>
						</ul>
					</nav>
				</div>
			</div>

            <div class="container" style="margin-top: 10px;">
            <h3 class="title">User information</h3>
			<div class="row">
				<div class="col-1" style="padding: 20px">
					<table>
                        <tr>
                            <td>Name</td>
                            <td>:</td>
                            <td>${user.loginName}</td>
                        </tr>
                        <tr>
                            <td>Phone</td>
                            <td>:</td>
                            <td>${user.phone}</td>
                        </tr>
                        <tr>
                            <td>Email</td>
                            <td>:</td>
                            <td>${user.email}</td>
                        </tr>
                        <tr>
                            <td>Address</td>
                            <td>:</td>
                            <td>${user.diaChi}</td>
                        </tr>
                        <tr>
                            <td>Admin</td>
                            <td>:</td>
                            <td>${user.isAdmin() ? 'Yes' : 'No'}</td>
                        </tr>
                        <tr>
                            <td>Active</td>
                            <td>:</td>
                            <td>${user.isActive() ? 'Yes' : 'No'}</td>
                        </tr>

                    </table>
	                  <div id="myModal2" class="modal">
	                      <div class="modal-content">
	                          <span class="close2">&times;</span>
	                          <p>Confirm change to ${user.isAdmin() ? 'user' : 'admin'}?</p><br>
	                         <c:if test="${user.isAdmin()}">
		                         <a class="btn" href="changeToUser?id=${user.id}&name=${user.loginName}">Confirm</a>
		                  </c:if>
	                         <c:if test="${not user.isAdmin()}">
	                          <a class="btn" href="changeToAdmin?id=${user.id}&name=${user.loginName}">Confirm</a>
		                  </c:if>
	                     </div>
	                  </div>                    
	                  <div id="myModal1" class="modal">
	                      <!-- Modal content -->
	                      <div class="modal-content">
	                          <span class="close1">&times;</span>
	                          <p>Confirm ${user.isActive() ? 'Deactivate' : 'Activate'} user?</p><br>
	                          <c:if test="${user.isActive()}">
	                           <a class="btn" href="deactivateUser?id=${user.id}&name=${user.loginName}">Confirm</a>
		                  </c:if>
	                          <c:if test="${not user.isActive()}">
		                          <a class="btn" href="activateUser?id=${user.id}&name=${user.loginName}">Confirm</a>
		                   </c:if>
	                      </div>
	                  </div>
	                   
	                  <a id="myBtn1" class="btn" >${user.isActive() ? 'Deactivate' : 'Activate'} user</a>
	                  <a id="myBtn2" class="btn" >Change to ${user.isAdmin() ? 'user' : 'admin'}</a>
                    
                    
                    <c:if test="${not user.isAdmin()}">
	                    <a class="btn" href="cart?id=${user.id}">Cart</a>
                    </c:if>
				</div>
			</div>
        
            <h3 class="title">Import</h3>
            <table class="cusTable">
                <tr>
                    <th style="width: 200px;">Id</th>
                    <th style="width: 200px;">Date</th>
                    <th style="width: 200px;">Detail</th>
                </tr>
                <c:forEach var="bill" items="${importBills}">
	                <tr>
	                    <td >${bill.soHoaDon}</td>
	                    <td ><fmt:formatDate type="date" dateStyle="long" value="${bill.ngayTao}" /></td>
	                    <td class="detail_">
	                    	<a href="billDetail?id=${bill.soHoaDon}&type=Import">Detail</a>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
            <h3 class="title">Export</h3>
            <table class="cusTable">
                <tr>
                    <th style="width: 200px;">Id</th>
                    <th style="width: 200px;">Date</th>
                    <th style="width: 200px;">Detail</th>
                </tr>
                <c:forEach var="bill" items="${exportBills}">
	                <tr>
	                    <td >${bill.soHoaDon}</td>
	                    <td ><fmt:formatDate type="date" dateStyle="long" value="${bill.ngayTao}" /></td>
	                    <td class="detail_">
	                    	<a href="billDetail?id=${bill.soHoaDon}&type=Export">Detail</a>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
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
                if (event.target == modal1) {
                    modal1.style.display = "none";
                }
                if (event.target == modal2) {
                    modal2.style.display = "none";
                }
            }
        </script>
	</body>
</html>

