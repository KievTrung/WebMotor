<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Import</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js" ></script>
		<link href="<c:url value="/resources/adminCss/index.css"/>" rel="stylesheet" >
		<link href="<c:url value="/resources/adminCss/import.css"/>" rel="stylesheet" >
        <style>
            table{
                width: 100%;
                text-align: center;
                border-collapse: collapse;
                border: 1px solid black;
            }
            th{
                padding: 5px;
                color: white;
                background: black;
                font-weight: normal;
                border: 1px solid white;
            }
            td{
                padding: 10px 5px;
                border: 1px solid black;
            }
            td img{
                width: 150px;
                height: 150px;
                margin-right: 10px; 
            }
            .detail_{
                transition: 0.5s;
            }
            .detail_:hover{
                color: white;
                background-color: black;
            }
            .detail_ a:hover{
                color:white;
            }
        </style>
    </head>
	<body class="poppins-regular" onLoad="${msg}">
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

		<div class="container" style="margin-top: 50px;">
			<div class="row">
				<div class="col-1" style="padding: 30px;">
                    <table >
                        <tr>
                            <td style="width: 200px;">Number of active user</td>
                            <td style="width: 100px;">${user}</td>
                            <td style="width: 200px;">Number of active admin</td>
                            <td style="width: 100px;">${admin}</td>
                        </tr>
                    </table>
                </div>
				<div class="col-1" style="padding: 30px; margin-top: 10px;">
                    <form id="form" action="modifyVat" method="post">
                        <span>Current VAT </span>
                        <input type="number" name="vat" value="${vat}" step=".01"/>
                        <a id="myBtn1" class="btn">Modify VAT</a>
                    </form>
                </div>
				<div class="col-1" style="padding: 30px; margin-top: 10px;">
                    <form id="form" action="calRevenue" method="post">
                        <span>Begin date </span>
                        <input type="date" name="start" value="2024-05-07"/><br>
                        <span style="margin-right: 13px;">End date</span>
                        <input type="date" name="end" value="2024-06-11"/>
                        <input type="submit" class="btn" value="Caculate revenue">
                    </form>
                    <div id="myModal1" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close1">&times;</span>
                            <p>Confirm save?</p><br>
                            <input form="form" type="submit" class="btn" value="Save">
                        </div>
                    </div>
				</div>
			</div>
            
            <h3 class="title">Revenue</h3>
            <table>
                <tr>
                    <th style="width: 200px;">Total Import</th>
                    <th style="width: 200px;">Total Export</th>
                    <th style="width: 200px;">Balance</th>
                </tr>
                <tr>
                    <td ><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${totalImport}"/></td>
                    <td ><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${totalExport}"/></td>
                    <td ><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${totalExport - totalImport}"/></td>
                </tr>
            </table>
            <h3 class="title">Export</h3>
            <table>
                <tr>
                    <th style="width: 200px;">Id</th>
                    <th style="width: 200px;">Date</th>
                    <th style="width: 200px;">User</th>
                    <th style="width: 200px;">VAT</th>
                    <th style="width: 200px;">Detail</th>
                </tr>
                <c:forEach var="bill" items="${exportBills}">
	                <tr>
	                    <td >${bill.soHoaDon}</td>
	                    <td >${bill.ngayTao}</td>
	                    <td class="detail_">
	                    	<a href="userDetail?name=${bill.userName}">${bill.userName}</a>
	                    </td>
	                    <td >${bill.vat}</td>
	                    <td class="detail_">
	                    	<a href="billDetail?id=${bill.soHoaDon}&type=Export">Detail</a>
	                    </td>
	                </tr>
                </c:forEach>
            </table>
            <h3 class="title">Import</h3>
            <table>
                <tr>
                    <th style="width: 200px;">Id</th>
                    <th style="width: 200px;">Date</th>
                    <th style="width: 200px;">User</th>
                    <th style="width: 200px;">Detail</th>
                </tr>
                <tr>
                <c:forEach var="bill" items="${importBills}">
	                <tr>
	                    <td >${bill.soHoaDon}</td>
	                    <td >${bill.ngayTao}</td>
	                    <td class="detail_">
	                    	<a href="userDetail?name=${bill.userName}">${bill.userName}</a>
	                    </td>
	                    <td class="detail_">
	                    	<a href="billDetail?id=${bill.soHoaDon}&type=Import">Detail</a>
	                    </td>
	                </tr>
                </c:forEach>
                </tr>
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
        
            var other = document.getElementById("hello"); 

            function enableTextBox(value){
                if(value == "other"){
                    other.style.display = "block";
                }
                else{
                    other.value = "";
                    other.style.display = "none";
                }
            }

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



