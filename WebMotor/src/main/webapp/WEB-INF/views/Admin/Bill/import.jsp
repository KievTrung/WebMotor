<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
		<link href="<c:url value="/resources/adminCss/billDetail.css"/>" rel="stylesheet" >
		 <style>
            select{
                min-width: 250px; height: 40px; padding-left: 10px; margin-bottom: 10px; font-size: 14px; margin-right: 10px;
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
							<li><a href="product">Products</a></li>
							<li><a href="user">Users</a></li>
							<li><a href="bill">Imports/Exports</a></li>
							<li><a href="profile">${empty account ? "Account" : account.loginName}</a></li>
						</ul>
					</nav>
				</div>
			</div>
		<!-- singer product detail -->
		<div class="container" style="margin-top: 10px;">
            <h3 class="title">Vehicle information</h3>
			<div class="row">
				<div class="col-1" style="padding: 30px;">
					<form:form id="form" action="addProduct" modelAttribute="product" method="post" enctype="multipart/form-data">
                        <form:input path="name" 
                        			placeholder="Type name or product code"
                        			readonly="${empty editMode ? 'false' : 'true' }"/>
                        <form:errors style="color: red;" path="name"/>
                        <br>
                        
                        <form:input type="number" 
									placeholder="Type price"
									path="price" 
									onkeypress="return (event.charCode !=8 && event.charCode ==0 || (event.charCode >= 48 && event.charCode <= 57))"/>	
						<form:errors style="color: red;" path="price"/>
						<br>
						
						<form:input type="text"
									path="type" 
									name="type"  
									placeholder="Type new category" 
									readonly="${product.isNew() eq true or empty editMode ? 'false' : 'true' }"/>
						<form:errors style="color: red;" path="type"/>
						<br>
						
						<select name="currenType" style="display:${product.isNew() eq true or empty editMode ? 'block' : 'none' };">
                            <option value="none" >Current category</option>
                            <c:forEach var="item" items="${type}">                            
	                            <option value="${item}" ${product.type eq item ? ' selected' : ''}>${item}</option>
                            </c:forEach>
                        </select>	
						
						<form:input type="number" 
									path="amount" 
									placeholder="Type quantity"
									onkeypress="return (event.charCode !=8 && event.charCode ==0 || (event.charCode >= 48 && event.charCode <= 57))" />	
						<form:errors style="color: red;" path="amount"/>
						<br>
                        
                        <input type="file" name="images" accept="image/png, image/jpeg" multiple class="btn" style="width: 250px; margin-right: 10px; margin-top: 0px; margin-bottom: 10px">                  
                        <span style="color: red;" >${uploadError}</span>
                        <br>

                        <form:textarea path="description" 
                        				cols="165" 
                        				rows="10" 
                        				style="resize: none; padding: 10px;" 
                        				placeholder="Type description (optional)"
                        				readonly="${product.isNew() eq true or empty editMode? 'false' : 'true' }"/>
                        <form:errors style="color: red;" path="description"/>
                        <br>
                        
                        
                        <a id="myBtn1" class="btn">${empty editMode ? "Add" : "Save"}</a>
	                    <a id="myBtn2" class="btn">${empty editMode ? "Import" : "Delete"}</a>
	                    <c:if test="${not empty editMode}">
	                    	<a class="btn" href="import">Cancel</a>
	                    </c:if>
                    </form:form>
                    <div id="myModal1" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close1">&times;</span>
                            <p>Confirm ${empty editMode ? "Add" : "Save"}?</p><br>
                            <input form="form" type="submit" class="btn" value="${empty editMode ? 'Add' : 'Save'}">
                        </div>
                    </div>
                    <div id="myModal2" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close2">&times;</span>
                            <p>Confirm ${empty editMode ? "Import" : "Delete"}?</p><br>
                            <c:if test="${empty editMode}">
	                            <a class="btn" href="createImport">Confirm</a>                            
                            </c:if>
                            <c:if test="${not empty editMode}">
	                            <a class="btn" href="deleteProduct?name=${product.name}">Confirm</a>                            
                            </c:if>
                        </div>
                    </div>
				</div>
			</div>
			<!------------------ picture----------------------- -->
            <h3 class="title">Pictures</h3>
			<div class="row">
	            <c:forEach var="item" items="${product.pics}">
					<div class="col-4">
						<img src="<c:url value='/resources/upload/${item}'/>" 
	                          onerror="this.onerror=null;this.src='<c:url value="/resources/vehicles/${item}"/>';"
	                          onclick="confirmDelete('<c:url value="deleteImageFromImportProduct?name=${item}&productIndex=${productIndex}"/>', '${item}')"/>
	                    <p>${item}</p>
					</div>
	            </c:forEach>
			</div>
			<!------------------ list----------------------- -->
            <h3 class="title">Imports list</h3>
            <table>
                <tr>
                    <th style="width: 400px;">Vehicles</th>
                    <th style="width: 100px;">Quantity</th>
                    <th style="width: 100px;">SubTotal</th>
                </tr>
                <c:set var="total" value="0" />
                <c:forEach var="product" items="${importList}" varStatus="status">
                	<c:set var="total" value="${total + product.amount * product.price }"/>
	                <tr>
	                    <td class="detail">
	                        <a  href="editProduct?index=${status.index}">
	                            <div class="info">
	                            	<img src="<c:url value="/resources/import/${product.picture}"/>" 
	                            		onerror="this.onerror=null;this.src='<c:url value="/resources/vehicles/${product.picture}"/>';"/>
	                                <div>
	                                    <table class="meta-info">
	                                        <tr>
	                                            <td>Name</td>
	                                            <td>:</td>
	                                            <td>${product.name}</td>
	                                        </tr>
	                                        <tr>
	                                            <td>Price</td>
	                                            <td>:</td>
	                                            <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.price }"/></td>
	                                        </tr>
	                                        <tr>
	                                            <td>Category</td>
	                                            <td>:</td>
	                                            <td>${product.type}</td>
	                                        </tr>
	                                    </table>
	                                </div>
	                            </div>
	                        </a>
	                    </td>
	                    <td>${product.amount}</td>
	                    <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${product.amount * product.price}"/></td>
	                </tr>
                </c:forEach>
                <tr>
                    <th colspan="2">Total</th>
                    <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${total}"/></th>
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
        	function confirmDelete(url, msg) {
	        	  if (confirm("Confirm delete : " + msg)) {
	        	    window.location.href = url;
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

