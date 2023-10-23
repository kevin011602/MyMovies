<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
	String error = (String) request.getAttribute("error");
	if (error == null)
		error = "";
%>

<!DOCTYPE html>
<html lang="IT">
	<head>
		<meta charset="ISO-8859-1">
		<title>Registrati</title>
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
		<link type="text/css" rel="stylesheet" href="css/signup.css">
	</head>
	<body>
	    <form action="<%= request.getContextPath()%>/RegistrationServlet" method="POST">
	        <div class="inputBox">
	        	<i>Username</i>
	            <input type="text" name="username" maxlength="20" placeholder="Inserisci il tuo nome utente" required oninput="checkUsername(this, 'errorMessageUsername', '#fba304')">
	            <p class="error-message" id="errorMessageUsername"></p>
	        </div>
	        
	        <div class="inputBox">
	            <i>Password</i>
	            <input type="password" name="password" placeholder="Inserisci una password" required maxlength="20">
	        </div>
	        
	        <div class="links">
	            <a>Hai già un account?</a>
	            <a href="login.jsp">Accedi</a>
	        </div>
	        
	        <input type="submit" value="Registrati" class="submit-button">
	    </form>
	</body>
</html>