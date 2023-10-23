<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="javax.sql.DataSource" %>
<%@page import="model.Raccolta"%>
<%@page import="model.Utente"%>
<%@page import="model.Include"%>
<%@page import="model.Film"%>
<%@page import="db.FilmDAO"%>
<%@page import="db.IncludeDAO"%>
<%@page import="db.RaccoltaDAO"%>

<%
   	String nomeRaccolta = request.getParameter("nomeRaccolta");
	String username = request.getParameter("username");

	System.out.println("raccolta.jsp: " + nomeRaccolta + ", " + username);
	
	Raccolta raccolta = new Raccolta();
	
	DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
	RaccoltaDAO raccoltaDAO = new RaccoltaDAO(ds);
	
	raccolta = raccoltaDAO.doRetrieveByKey(nomeRaccolta, username);
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title><%=raccolta.getNome()%> MyMovies</title>
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
		<link type="text/css" rel="stylesheet" href="css/raccolta.css">
	</head>
	<body>
	    <%@ include file="header.jsp"%>
	    
	    <div id="raccoltaContainer">
	    
	    	<div id="titoloEmodifica">
	        	<h1><%=raccolta.getNome()%></h1>
				<img id="modifyImage" src="<%= request.getContextPath() %>/imgs/modifica.png" alt="Modifica">
			</div>
			
			<div id="overlay">
				<div id="modificaNome">
					<form id="form-modifica-raccolta" action="<%=request.getContextPath()%>/CollectionControlServlet?action=update" method="POST">
						<input type="hidden" id="vecchioNomeRaccolta" name="oldName" value="<%=raccolta.getNome()%>">
						<label for="newName">Nuovo nome della Raccolta</label>
				        <input type="text" id="nuovoNomeRaccolta" name="newName" placeholder="Inserisci il nuovo nome...">
						<input type="submit" value="Salva Modifiche">
					</form>
				</div>
			</div>
			<script src="js/raccolta.js"></script>
			
	        <div id="codiceFilmContainer">
	        	<form id="form-inserimento-film" action="<%=request.getContextPath()%>/CollectionControlServlet?action=addMovie" method="POST">
		            <input type="hidden" id="nomeRaccolta" name="nomeRaccolta" value="<%=raccolta.getNome()%>">
		            <label for="codiceFilm">Codice del Film</label>
		            <input type="text" id="codiceFilm" name="codiceFilm" placeholder="Inserisci il codice del film">
		            <button>Aggiungi</button>
	            </form>
	        </div>
	        
	        <div id="listaFilm">
	        	
	        	<%
		            Collection<Include> includeCollection = new ArrayList<Include>();
					IncludeDAO includeDAO = new IncludeDAO(ds);
		            
					includeCollection = includeDAO.doRetrieveAllByKey(nomeRaccolta, username);
					
					if(includeCollection.isEmpty()) {
						%><p>Nessun film inserito</p><%
					}
					
					else {
						for(Include include : includeCollection) { 
						
							Film film = new Film();
							FilmDAO filmDAO = new FilmDAO(ds);
							
							film = filmDAO.doRetrieveByKey(include.getCodiceFilm());
							
							 %>
						
							<div class="Film">
		           			<!-- Copertina del film: -->
		           			<img id="copertina" src="<%= request.getContextPath() %>/ProcessMoviePosterServlet?code=<%= film.getCodice() %>"/>
		           			
		           			<div class="overlay">
		           			
			            	<h3>
			            		<!-- Titolo + anno: -->
			                	<a class="link" href="<%=request.getContextPath()%>/film.jsp?codiceFilm=<%=film.getCodice()%>">
									<%=film.getTitolo() + " (" + film.getAnno() + ")"%>
			                    </a>
			                </h3>
			                
			                <!-- Tasto per eliminare un film: -->
							<a href="<%=request.getContextPath()%>/CollectionControlServlet?action=removeMovie&film=<%=film.getCodice()%>&nome=<%=raccolta.getNome()%>">
							  <img id="delete" src="<%=request.getContextPath()%>/imgs/delete.png" alt="Elimina">
							</a>
			                
						  	</div>

			         	</div> <%
						}
					}
	        	
	        	%>
	        	
	        </div>
	        
	    </div>
	</body>
</html>

