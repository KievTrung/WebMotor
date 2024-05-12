<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Place order</title>
    
    <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/13e4082d8b.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" >
    <link href="<c:url value="/resources/css/order.css"/>" rel="stylesheet" >	
</head>
<body class="poppins-regular" onload="${onload}">
    <div class="container" >
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
        </div>
    </div>
    <div class="background" style="background-image: url(<c:url value="/resources/images/login.jpg" />)">
        <div class="main">
            <div class="card">
                <div class="card-body">
                    <form:form id="form" action="placeOrder" modelAttribute="metaInfo" method="post">
                        <table>
                            <tbody>
                                <tr>
                                    <td>Order id:</td>
                                    <td>:</td>
                                    <td>${donHang.orderId}</td>
                                </tr>
                        
                                <tr>
                                    <td>Order date</td>
                                    <td>:</td>
                                    <td><fmt:formatDate type="date" dateStyle="long" value="${donHang.date}" /></td>
                                </tr>
                                <tr>
                                    <td>Payment method</td>
                                    <td>:</td>
                                    <td>
                                        <select id="paymentMethod" onclick="enableVisa()" style="font-size: 14px;" name="paymentMethod">
                                            <option value="NA" ${donHang.paymentMethod eq "NA" ? "selected" : ""}>N/A</option>
                                            <option value="VISA" ${donHang.paymentMethod eq "VISA" ? "selected" : ""}>VISA</option>
                                            <option value="INPLACE" ${donHang.paymentMethod eq "INPLACE" ? "selected" : ""}>At place</option>
                                        </select>
                                        <br>
                                        <div style="font-size: 14px; color: red;">${errorMsg}</div>
                                    </td>
                                </tr>
                                <tr id="tbChange3" style="visibility: collapse;">
                                    <td></td>
                                    <td></td>
                                    <td  class="tb">
                                        <form:input
                                        		path="cardName"
                                                id="cardName"
                                                placeholder="Name (uppercase)"
                                                style="margin-top: 20px; margin-bottom: 2px"/>
                                        <br>
                                        <form:errors path="cardName" element="div" style="font-size: 14px; color: red;"/>
                                    </td>
                                </tr>
                                <tr id="tbChange3_" style="visibility: collapse;">
                                    <td></td>
                                    <td></td>
                                    <td  class="tb">
                                    	<form:input
                                        		path="cardNumber"
                                                id="cardNumber"
                                                placeholder="xxxx xxxx xxxx xxxx"/>
                                      	<br>
                                      	 <form:errors path="cardNumber" element="div" style="font-size: 14px; color: red;"/>
                                    </td>
                                </tr>
                                <tr id="tbChange3__" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
										<form:select path="month" items="${month}" id="month" class="date"/>
										<form:select path="year" items="${year}" style="margin-left: 10px;" id="year" class="date"/>
                                    	<br>
                                    	<form:errors path="year" element="div" style="font-size: 14px; color: red;"/>
                                    </td>
								</tr>
								<tr id="tbChange3___" style="visibility: collapse;">
									<td></td>
									<td></td>
									<td class="tb">
                                        <form:input  
	                                        id="cvv"
	                                        path="cvv" 
	                                        maxlength="3"
	                                        placeholder="cvv" />
	                                    <br>
	                                    <form:errors path="cvv" element="div" style="font-size: 14px; color: red;"/>
                                    </td>
								</tr>
                                <tr>
                                    <td>Address</td>
                                    <td>:</td>
                                    <td>
                                    	<form:input 
                                            placeholder="Type new address" 
                                            path="address" 
                                            value="${empty tempAddress ? account.diaChi : tempAddress}" 
                                            style="width: 50%; font-size: 14px; width: 250px" />
                                        <br>
                                      	<form:errors path="address" element="div" style="font-size: 14px; color: red;"/>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form:form>
                    <div id="myModal1" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close1">&times;</span>
                            <p>Confirm place order?</p><br>
                            <input form="form" type="submit" class="btn_" value="Confirm" onclick="checkValue()"/>
                        </div>
                    </div>
                    <div id="myModal2" class="modal">
                        <!-- Modal content -->
                        <div class="modal-content">
                            <span class="close2">&times;</span>
                            <p>Cancel order?</p><br>
                            <a class="btn" href="cancelOrder">Confirm</a>
                        </div>
                    </div>
                    <a id="myBtn1" class="btn_" style="margin-top: 30px;">Place order</a>
                    <a id="myBtn2" class="btn_" >Cancel</a>
                    <div class="detail">
                        <p>Cart products: </p>
                        <table style="width: 610px;">
                            <tr>
                                <th style="width: 300px;">Product</th>
                                <th style="width: 100px;">Quantity</th>
                                <th >SubTotal</th>
                            </tr>
                            <c:set var="total" value="0"/>
                            <c:forEach var="item" items="${donHang.list}">
                            	<c:set var="total" value="${total + item.price * item.amount * 1000 * (1 + vat)}"/>
	                            <tr>
	                                <td>
	                                    <img src="<c:url value="/resources/vehicles/${item.picture}"/>" >
	                                    <p>${item.name}</p>
	                                    <p><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price * 1000}"/></p>
	                                </td>
	                                <td>${item.amount}</td>
	                                <td><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price * item.amount * 1000}"/></td>
	                            </tr>  
	                            <tr>
	                                <th colspan="2">SubTotal + <fmt:formatNumber type="percent" value="${vat}"/> VAT</th>
                                	<th ><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${item.price * item.amount * 1000 * (1 + vat)}"/></th>
	                            </tr>                          
                            </c:forEach>
                            <tr>
                            	<th colspan="2">Total + <fmt:formatNumber type="percent" value="${vat}"/> VAT</th>
                                <th><fmt:formatNumber type="CURRENCY" currencySymbol="VND " maxFractionDigits="0" value="${total}"/></th>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
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
    <script>
	    var tbChange3 = document.getElementById("tbChange3");
	    var tbChange3_ = document.getElementById("tbChange3_");
	    var tbChange3__ = document.getElementById("tbChange3__");
	    var tbChange3___ = document.getElementById("tbChange3___");
	    var tbChange4 = document.getElementById("tbChange4");
	
	    var paymentMethod = document.getElementById("paymentMethod");        
	    var cardNumber =  document.getElementById("cardNumber");
	    var cardName =  document.getElementById("cardName");
	    var month =  document.getElementById("month");
	    var year =  document.getElementById("year");
	    var cvv =  document.getElementById("cvv");
	
	    function displayError(){
	    	paymentMethod.selectedIndex = "1";
	    	enableVisa();
	    }
	
	    function checkValue(){
	        var value = paymentMethod.options[paymentMethod.selectedIndex].value;
	        if(value != "VISA"){
	            cardNumber.value = "0000 0000 0000 0000";
	            cardName.value="NULL";
	            cvv.value = "000";
	        }
	    }
	    
	    function enableVisa(){
	        var value = paymentMethod.options[paymentMethod.selectedIndex].value;
	        if(value == "VISA"){
	            tbChange3.style.visibility = "visible";
	            tbChange3_.style.visibility = "visible";
	            tbChange3__.style.visibility = "visible";
	            tbChange3___.style.visibility = "visible";
	        }
	        else{
	            tbChange3.style.visibility = "collapse";
	            tbChange3_.style.visibility = "collapse";
	            tbChange3__.style.visibility = "collapse";
	            tbChange3___.style.visibility = "collapse";
	        }
	    }
	    
	    function change4(){
	        if(tbChange4.style.visibility == "collapse"){
	            tbChange4.style.visibility = "visible";
	        }
	        else{
	            tbChange4.style.visibility = "collapse";
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

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal1) {
                modal1.style.display = "none";
            }
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
            if (event.target == modal2) {
                modal2.style.display = "none";
            }
        }
    </script>
</body>
</html>