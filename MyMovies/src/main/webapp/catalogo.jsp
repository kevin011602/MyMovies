<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.SQLException" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>

<%@ page import="model.Contiene" %>
<%@ page import="model.Catalogo" %>
<%@ page import="model.Film" %>
<%@ page import="model.Utente" %>

<%@ page import="db.FilmDAO" %>
<%@ page import="db.ContieneDAO" %>

<%
	Collection<?> films = (Collection<?>) request.getAttribute("movies");
	if(films == null) {
		response.sendRedirect(request.getContextPath()+"/CatalogueServlet");
		return;
	}
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Catalogo MyMovies</title>
		<link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/catalogo.css">
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script src="js/catalogo.js"></script>
	</head>
	<body>
	
		<%@ include file="header.jsp"%>	
		
			<div id="Container">
			<div id="funzionalità">
			
				 <p style="color: #adadad; font-size: 12px;"><%=films.size()%> risultati</p>
				<!-- Tasto per inserire un nuovo film -->
				<button id="mostra-form"></button>
				
				<div id="modulo-inserimento">
					<form id="form-inserimento-film" action="<%=request.getContextPath()%>/MovieControlServlet?action=add" enctype="multipart/form-data" method="POST" onsubmit="return checkGenreSelection();">
						<div class="MovieData">
						    <label for="poster">Locandina del Film</label>
						    <input type="file" id="poster" name="copertina" accept="image/*">
						    <button type="button" id="rimuovi-foto" class="hidden">Annulla</button>
						</div>
						
						<div class="MovieData">
						    <label for="title">Titolo</label>
						    <input type="text" id="title" name="titolo" placeholder="Titolo" required>
						</div>
						
						<div class="MovieData">
						    <label for="anno">Anno</label>
						    <input type="number" id="anno" name="anno" placeholder="Anno" min="1888">
						</div>
						
						<div class="MovieData">
						    <label for="durata">Durata (minuti)</label>
						    <input type="number" id="durata" name="durata" placeholder="Durata" min="0">
						</div>
						
						<div class="MovieData">
						    <label for="genere">Genere</label>
						    <select id="genere" name="genere">
						    <option value="" disabled selected>Seleziona un genere</option>
						    <optgroup label="A">
						            <option value="Animazione">Film d'animazione</option>
						            <option value="Avventura">Film d'avventura</option>
						            <option value="Azione">Film d'azione</option>
						        </optgroup>
						        <optgroup label="B">
						            <option value="Biografici">Film biografici</option>
						            <option value="Blaxploitation">Film blaxploitation</option>
						        </optgroup>
						        <optgroup label="C">
						            <option value="Catastrofici">Film catastrofici</option>
						            <option value="Comici">Film comici</option>
						            <option value="Commedia">Film commedia</option>
						            <option value="Crossover">Film crossover</option>
						        </optgroup>
						        <optgroup label="D">
						            <option value="Documentari">Film documentari</option>
						            <option value="Drammatici">Film drammatici</option>
						        </optgroup>
						        <optgroup label="E">
						            <option value="Epici">Film epici</option>
						            <option value="Erotici">Film erotici</option>
						            <option value="Exploitation">Film d'exploitation</option>
						        </optgroup>
						        <optgroup label="F">
						            <option value="Falso documentario">Film in stile falso documentario</option>
						            <option value="Fantascienza">Film di fantascienza</option>
						            <option value="Fantastici">Film fantastici</option>
						            <option value="Found footage">Film found footage</option>
						        </optgroup>
						        <optgroup label="G">
						            <option value="Gangster">Film di gangster</option>
						            <option value="Gialli">Film gialli</option>
						            <option value="Giudiziari">Film giudiziari</option>
						            <option value="Grotteschi">Film grotteschi</option>
						            <option value="Guerra">Film di guerra</option>
						        </optgroup>
						        <optgroup label="H">
						            <option value="Horror">Film horror</option>
						        </optgroup>
						        <optgroup label="M">
						            <option value="Melò">Film melò</option>
						            <option value="Mitologici">Film mitologici</option>
						            <option value="Mockbuster">Film Mockbuster</option>
						            <option value="Mondo movie">Mondo movie</option>
						            <option value="Musicali">Film musicali</option>
						        </optgroup>
						        <optgroup label="N">
						            <option value="Nazisploitation">Film nazisploitation</option>
						            <option value="Noir">Film noir</option>
						        </optgroup>
						        <optgroup label="O">
						            <option value="Opera">Film-opera</option>
						        </optgroup>
						        <optgroup label="P">
						            <option value="Parodia">Film parodia</option>
						            <option value="Peplum">Film peplum</option>
						            <option value="Polizieschi">Film polizieschi</option>
						            <option value="Pornografici">Film pornografici</option>
						            <option value="Propaganda">Film di propaganda</option>
						        </optgroup>
						        <optgroup label="R">
						            <option value="Ragazzi">Film per ragazzi</option>
						            <option value="Road movie">Road movie</option>
						        </optgroup>
						        <optgroup label="S">
						            <option value="Satirici">Film satirici</option>
						            <option value="Sentimentali">Film sentimentali</option>
						            <option value="Sexploitation">Film sexploitation</option>
						            <option value="Sperimentali">Film sperimentali e d'avanguardia</option>
						            <option value="Spionaggio">Film di spionaggio</option>
						            <option value="Splatter">Film splatter</option>
						            <option value="Storici">Film storici</option>
						        </optgroup>
						        <optgroup label="T">
						            <option value="Thriller">Film thriller</option>
						        </optgroup>
						        <optgroup label="W">
						            <option value="Western">Film western</option>
						        </optgroup>
						    </select>
						</div>
						
						<div class="MovieData">
						    <label for="trama">Trama:</label>
						    <textarea id="trama" name="trama" placeholder="Breve descrizione del film"></textarea>
						</div>
						<button>Aggiungi</button>
					</form>
				</div>
				
				<!-- BARRA DI RICERCA -->
				<div id="barra-di-ricerca">
					<label for="durata">Ricerca</label>
					<form id="ricerca-form">
				        <input type="text" id="ricerca" name="searchBar" placeholder="Cerca un film..." />
				        <button type="submit">Cerca</button>
				    </form>
				</div>
				
				<!-- FILTRI -->
				<div id="filtri">
					<label>Filtri</label>
					
					<!-- Ricerca per genere -->
					<div id="genere" class="filtro">
						<label>Genere</label>
						<form id="filtroForm" action="<%= request.getContextPath()%>/CatalogueServlet" method="GET">
		    				<select id="genere" name="genere"> <!-- Cambiamo il nome dell'attributo a "filter" -->
								<option value="" disabled selected>Seleziona un genere</option>
								<optgroup label="A">
									<option value="Animazione">Film d'animazione</option>
							       	<option value="Avventura">Film d'avventura</option>
							     	<option value="Azione">Film d'azione</option>
								</optgroup>
							        <optgroup label="B">
							            <option value="Biografici">Film biografici</option>
							            <option value="Blaxploitation">Film blaxploitation</option>
							        </optgroup>
							        <optgroup label="C">
							            <option value="Catastrofici">Film catastrofici</option>
							            <option value="Comici">Film comici</option>
							            <option value="Commedia">Film commedia</option>
							            <option value="Crossover">Film crossover</option>
							        </optgroup>
							        <optgroup label="D">
							            <option value="Documentari">Film documentari</option>
							            <option value="Drammatici">Film drammatici</option>
							        </optgroup>
							        <optgroup label="E">
							            <option value="Epici">Film epici</option>
							            <option value="Erotici">Film erotici</option>
							            <option value="Exploitation">Film d'exploitation</option>
							        </optgroup>
							        <optgroup label="F">
							            <option value="Falso documentario">Film in stile falso documentario</option>
							            <option value="Fantascienza">Film di fantascienza</option>
							            <option value="Fantastici">Film fantastici</option>
							            <option value="Found footage">Film found footage</option>
							        </optgroup>
							        <optgroup label="G">
							            <option value="Gangster">Film di gangster</option>
							            <option value="Gialli">Film gialli</option>
							            <option value="Giudiziari">Film giudiziari</option>
							            <option value="Grotteschi">Film grotteschi</option>
							            <option value="Guerra">Film di guerra</option>
							        </optgroup>
							        <optgroup label="H">
							            <option value="Horror">Film horror</option>
							        </optgroup>
							        <optgroup label="M">
							            <option value="Melò">Film melò</option>
							            <option value="Mitologici">Film mitologici</option>
							            <option value="Mockbuster">Film Mockbuster</option>
							            <option value="Mondo movie">Mondo movie</option>
							            <option value="Musicali">Film musicali</option>
							        </optgroup>
							        <optgroup label="N">
							            <option value="Nazisploitation">Film nazisploitation</option>
							            <option value="Noir">Film noir</option>
							        </optgroup>
							        <optgroup label="O">
							            <option value="Opera">Film-opera</option>
							        </optgroup>
							        <optgroup label="P">
							            <option value="Parodia">Film parodia</option>
							            <option value="Peplum">Film peplum</option>
							            <option value="Polizieschi">Film polizieschi</option>
							            <option value="Pornografici">Film pornografici</option>
							            <option value="Propaganda">Film di propaganda</option>
							        </optgroup>
							        <optgroup label="R">
							            <option value="Ragazzi">Film per ragazzi</option>
							            <option value="Road movie">Road movie</option>
							        </optgroup>
							        <optgroup label="S">
							            <option value="Satirici">Film satirici</option>
							            <option value="Sentimentali">Film sentimentali</option>
							            <option value="Sexploitation">Film sexploitation</option>
							            <option value="Sperimentali">Film sperimentali e d'avanguardia</option>
							            <option value="Spionaggio">Film di spionaggio</option>
							            <option value="Splatter">Film splatter</option>
							            <option value="Storici">Film storici</option>
							        </optgroup>
							        <optgroup label="T">
							            <option value="Thriller">Film thriller</option>
							        </optgroup>
							        <optgroup label="W">
							            <option value="Western">Film western</option>
							        </optgroup>
							</select>
							
							<div class="pulsantiFiltri-container">
						    	<button class="pulsantiFiltri">Applica</button>
						    	<button class="pulsantiFiltri">Ripristina</button>
		  					</div>
	  					</form>
					</div>
					
					<!-- Ricerca per anno di uscita -->
					<div id="anno" class="filtro">
						<label>Anno</label>
						<form action="<%= request.getContextPath()%>/CatalogueServlet" method="GET">
					        <input type="number" id="filtro-anno-inizio" name="fromYear" placeholder="Anno inizio">
					        <input type="number" id="filtro-anno-fine" name="toYear" placeholder="Anno fine">
					        <div class="pulsantiFiltri-container">
					            <button class="pulsantiFiltri">Applica</button>
					            <button type="reset" class="pulsantiFiltri">Ripristina</button>
					        </div>
						</form>
					</div>
					
					<!-- Ricerca per durata -->
					<div id="durata" class="filtro">
						<label>Durata</label>
						<form action="<%= request.getContextPath()%>/CatalogueServlet" method="GET">
					        <input type="number" id="filtro-durata-min" name="fromMin" placeholder="Durata minima (minuti)">
					        <input type="number" id="filtro-durata-max" name="toMin" placeholder="Durata massima (minuti)">
					
					        <div class="pulsantiFiltri-container">
					            <button class="pulsantiFiltri">Applica</button>
					            <button type="reset" class="pulsantiFiltri">Ripristina</button>
					        </div>
						</form>
					</div>
					
				</div>
				
			</div>
			
			<!-- Servlet per caricare dal database i film dell'utente -->
			<div id="FilmCatalogo">
				<%
				// Controlliamo se non sono stati trovati film
			    if (films.isEmpty()) { %>
		            <p>Il catalogo è vuoto.</p>
		        <% }
				
				// Se invece sono stati trovati film, mostra a schermo ogni film
			    else {
			    	Iterator<?> it = films.iterator();
					while(it.hasNext()) {
					    Film film = (Film) it.next(); %>
					    
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
								<a href="<%=request.getContextPath()%>/MovieControlServlet?action=delete&code=<%= film.getCodice()%>">
								  <img id="delete" src="<%=request.getContextPath()%>/imgs/delete.png" alt="Elimina">
								</a>
						  	</div>
			         	</div> <%
				 	}
				}%>
		    </div>
	    </div>
	</body>
</html>