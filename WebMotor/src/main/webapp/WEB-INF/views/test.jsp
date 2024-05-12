<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet" >
</head>
<body>	
	<form action="hello" >
		<select name="id">
			<form:options  items="${items}" />
		</select>
		<input type="submit" />
	</form>
</body>
</html>