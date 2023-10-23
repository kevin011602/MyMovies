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
		<title>Accedi</title>
		<!-- Including CSS, JavaScript, ecc. -->
		<link rel="icon" type="image/x-icon" href="/imgs/favicon.ico">
		<link type="text/css" rel="stylesheet" href="css/login.css">
		<script type="text/javascript" src="js/login.js"></script>
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
	</head>
	<body>
		<form action="<%= request.getContextPath()%>/LoginServlet" method="POST">
			<!-- Campo in cui inserire l'username -->
			<div class="inputBox">
				<i>Username</i>
				<input type="text" name="username"  placeholder="Inserisci il tuo nome utente" required>
			</div>
			<!-- Campo in cui inserire la password -->
			<div class="inputBox">
				<i>Password</i>
				<input type="password" name="password" placeholder="Inserisci una password" required>
			</div>
			<!-- Link utili -->
			<div class="links">
				<a href="#">Password dimenticata</a>
				<a href="<%= request.getContextPath()%>/signup.jsp">Registrati</a>
			</div>
			<!-- Pulsante di invio form compilato -->
			<input type="submit" value="Accedi" class="submit-button">
		</form>
	</body>
</html>