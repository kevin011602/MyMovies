<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.SQLException" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>

<%@ page import="model.Raccolta" %>

<%@ page import="db.RaccoltaDAO" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Area utente MyMovies</title>
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
		<link type="text/css" rel="stylesheet" href="css/userArea.css">
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script src="js/userArea.js"></script>
	</head>
	<body>
	
		<%@ include file="header.jsp"%>
	
	  	<main>
	  	
		  	<!-- Tasto per creare una raccolta personalizzata -->
		  	<!-- L'utente inserisce dei film contenuti nel catalogo -->
		  	<div class="pannello">
		  		<h2>Raccolte</h2>
		  	</div>
		  	
		  	<div id="AggiungiRaccolta">
			    
			    <button id="mostraForm" onclick="toggleModulo()">+</button>
			
			    <div id="nomeRaccoltaForm">
					<form id="formRaccolta" action="<%=request.getContextPath()%>/CollectionControlServlet?action=create" method="post">
					    <input type="text" id="nomeRaccolta" placeholder="Inserisci il nome della raccolta..." name="nome" required>
					    <button type="submit">Crea</button>
					</form>
			    </div>
		    </div>
		    
		    <div id="Raccolte"> <%
				Collection<Raccolta> raccolte;
				RaccoltaDAO raccoltaDAO = new RaccoltaDAO(dataSource);
				try {
					raccolte = raccoltaDAO.doRetrieveAll("nome");
				} catch (SQLException e) {
					e.printStackTrace();
					raccolte = new ArrayList<>();
				}
				
				// Salviamo tutte le raccolte trovate
				if (raccolte.isEmpty()) { %>
					<p>Non hai nessuna raccolta.</p>
				<% }
				else {
					// Stampiamo tutte le raccolta
					for (Raccolta raccolta : raccolte) { %>
		           		<div class="Raccolta">
		           			<div id="name">
				            	<h3>
				            		 <!-- Link per accedere a una raccolta: -->
				                	<a href="<%=request.getContextPath()%>/raccolta.jsp?nomeRaccolta=<%=raccolta.getNome()%>&username=<%=raccolta.getNomeUtente()%>">
										<%=raccolta.getNome()%>
				                    </a>
				                </h3>
			                </div>
			                <!-- Tasto per eliminare una raccolta: -->
							<a id="deleteButton" data-nome-raccolta="<%=raccolta.getNome()%>">
							    <img id="delete" src="<%=request.getContextPath()%>/imgs/delete.png" alt="Elimina">
							</a>
					  	</div> <%
					}
				}%>
		    </div>
	  	
	    	<div class="pannello">
	      		<h2>Statistiche</h2>
	      		<!-- 
	      			PULSANTE CARICA STATISTICHE:
	      			DATA CREAZIONE ACCOUNT
	      			NUMERO FILM INSERITI
	      			NUMERO ORE TOTALI
	      			TOP 3 GENERI PREDOMINANTI
	      		 -->
	    	</div>
	  	</main>
	</body>
</html>