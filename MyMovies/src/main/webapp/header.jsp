<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="javax.sql.DataSource" %>
<%@ page import="model.Utente" %>

<% 
	Utente utente = (Utente) session.getAttribute("utente");
	DataSource dataSource = (DataSource) getServletContext().getAttribute("DataSource");
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
		<link type="text/css" rel="stylesheet" href="css/header.css">
		<script src="js/header.js"></script>
	</head>
	<body>
		<header>
			<h1>Ciao <%=utente.getUsername()%>!</h1>
			<nav>
				<a href="userArea.jsp" id="areaUtenteLink">Area utente</a>
				<a href="catalogo.jsp" id="catalogoLink">Catalogo</a>
				<a href="info.jsp" id="infoLink">About...</a>
				<a id="logout" href="<%= request.getContextPath()%>/LogoutServlet">Scollegati</a>
			</nav>
		</header>
	</body>
</html>