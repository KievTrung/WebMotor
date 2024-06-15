<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title>Import</title>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" >
		<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
		<script src="https://kit.fontawesome.com/13e4082d8b.js"></script>
		<link href="<c:url value="/resources/adminCss/index.css"/>" rel="stylesheet" >
		<link href="<c:url value="/resources/adminCss/import.css"/>" rel="stylesheet" >
		<link href="<c:url value="/resources/adminCss/billDetail.css"/>" rel="stylesheet" >
        <style>
        	.detail_, a{
			    transition: 0.5s;
			}
			.detail_:hover, .detail_ a:hover{
			    color: white;
			    background-color: black;
			}
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
                    <h3>Stocks: ${product.amount}</h3><br>
					<form:form id="form" action="saveProduct" modelAttribute="product" method="post" enctype="multipart/form-data">
                        <form:input path="name" 
                        			placeholder="Type new name" 
                        			title="Product id"/>
                        <form:errors style="color: red;" path="name"/>
                        <br>
                        
                        <form:input type="number" 
									placeholder="Type price"
									path="price" 
									title="Product price"
									onkeypress="return (event.charCode !=8 && event.charCode ==0 || (event.charCode >= 48 && event.charCode <= 57))"/>	
						<form:errors style="color: red;" path="price"/>
						<br>
						
						<form:input type="text"
									path="type" 
									name="type"
									title="Product new category"
									placeholder="Type category" value=""/>
						<form:errors style="color: red;" path="type"/>
						<br>
						<select name="currenType">
                            <option value="none" >Current category</option>
                            <c:forEach var="item" items="${type}">                            
	                            <option value="${item}" ${product.type eq item ? 'selected' : ''}>${item}</option>
                            </c:forEach>
                        </select>
                        <br>
                        
                        <input type="file" name="images" accept="image/png, image/jpeg" multiple class="btn" style="width: 250px; margin-right: 10px; margin-top: 0px; margin-bottom: 10px">                  
                        <span style="color: red;" >${uploadError}</span>
                        <br>
						
                        <form:textarea path="description" 
                        				cols="165" 
                        				rows="10" 
                        				style="resize: none; padding: 10px;" 
                        				placeholder="Type description (optional)" />
                        <form:errors style="color: red;" path="description"/>
                        <br>
                        
                        <form:input path="code" style="display: none"/>
                        <form:input path="amount" style="display: none"/>
                        <form:input path="active" style="display: none"/>
                        
                        <a id="myBtn1" class="btn">Save</a>
	                    <a id="myBtn2" class="btn">${product.active eq 'true' ? 'Deactivate' : 'Activate'}</a>
	                    <c:if test="${product.active eq 'true'}">
		                    <a class="btn" href="setFrontPage?id=${product.code}">Set front page</a>
		                    <a class="btn" href="setSpecialOffer?id=${product.code}">Set special offer</a>
	                    </c:if>
                    </form:form>
                    
                    <div id="myModal1" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close1">&times;</span>
                            <p>Confirm Save?</p><br>
                            <input form="form" type="submit" class="btn" value="Save">
                        </div>
                    </div>
                    <div id="myModal2" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close2">&times;</span>
                            <p>Confirm ${product.active eq 'true' ? 'Deactivate' : 'Activate'}?</p><br>
                            <c:if test="${product.active eq 'true'}">
								<a class="btn" href="deactiveProduct?id=${product.code}">Confirm</a>                            
                            </c:if>
                            <c:if test="${product.active eq 'false'}">
								<a class="btn" href="activeProduct?id=${product.code}">Confirm</a>                            
                            </c:if>
                        </div>
                    </div>
				</div>
			</div>
            <!------------------ picture----------------------- -->
            <h3 class="title">Pictures</h3>
			<div class="row">
	            <c:forEach var="item" items="${productImages}">
					<div class="col-4">
						<img src="<c:url value='/resources/upload/${item}'/>" 
	                          onerror="this.onerror=null;this.src='<c:url value="/resources/vehicles/${item}"/>';"
	                          onclick="confirmDelete('<c:url value="deleteImageFromEditProduct?image=${item}&productId=${product.code}&quantity=${productImages.size()}"/>', '${item}')"/>
	                    <p>${item}</p>
					</div>
	            </c:forEach>
			</div>

            <!--------------------PAGINATION ------------------------------->
            <h3 class="title">Reference bills</h3>
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
			<div style="font-size: 20px; color: gray; text-align: center; margin-top: 20px; visibility: ${bills.size() eq 0 ? 'visible' : 'hidden'};">No result</div>
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

