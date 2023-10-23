<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="javax.sql.DataSource" %>
<%@page import="model.Film"%>
<%@page import="db.FilmDAO"%>

<%
	DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
   	String codiceFilm = request.getParameter("codiceFilm");
	Film film = new Film();
	FilmDAO filmDAO = new FilmDAO(ds);
	film = filmDAO.doRetrieveByKey(Integer.parseInt(codiceFilm));
%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title><%=film.getTitolo()%> MyMovies</title>
		<link rel="icon" type="image/x-icon" href="/imgs/favicon.ico">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/film.css">
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		
		<script>
    		var contextPath = '<%= request.getContextPath() %>';
		</script>
		
	</head>
	<body>
		
		<%@ include file="header.jsp"%>
		
		<div id="MovieContainer">
			<div id="PosterContainer">
				<!-- locandina -->
				<img id="poster" src="<%= request.getContextPath() %>/ProcessMoviePosterServlet?code=<%= film.getCodice() %>"/>
			</div>
			
			<!-- Stelline recensioni -->
			<div id=Stars>
				
			</div>
			
			<div id="MovieInfoContainer">
			
				<div id="modify">
			  		<img id="modifyImage" src="<%= request.getContextPath() %>/imgs/modifica.png" alt="Modifica">
				</div>
				
				<div id="overlay" class="overlay">
				    <div id="editForm" class="edit-form">
				        <h2>Modifica Film</h2>
				        <form id="movieEditForm" action="<%=request.getContextPath()%>/MovieControlServlet?action=update" enctype="multipart/form-data" method="POST" accept-charset="UTF-8">
				            <!-- Campo nascosto per il codice del film -->
    						<input type="hidden" name="code" value="<%=film.getCodice()%>">
				            
				            <label for="copertina">Locandina del film</label>
				            <input type="file" id="posterForm" name="copertina" accept="image/*">
				            
				            <label for="titolo">Titolo</label>
				            <input type="text" id="titolo" name="titolo" value="<%=film.getTitolo()%>" required><br>
				
				            <label for="anno">Anno</label>
				            <input type="number" id="anno" name="anno" value="<%=film.getAnno()%>" min="1888" required><br>
				
				            <label for="durata">Durata (minuti)</label>
				            <input type="number" id="durata" name="durata" value="<%=film.getDurata()%>" min="0" required><br>
							
							<label for="genere">Genere</label>
							<select id="genere" name="genere" required>
							    <option value="" disabled>Seleziona un genere</option>
							    <optgroup label="A">
							        <option value="Animazione" <%=film.getGenere().equals("Animazione") ? "selected" : "" %>>Film d'animazione</option>
							        <option value="Avventura" <%=film.getGenere().equals("Avventura") ? "selected" : "" %>>Film d'avventura</option>
							        <option value="Azione" <%=film.getGenere().equals("Azione") ? "selected" : "" %>>Film d'azione</option>
							    </optgroup>
							    <optgroup label="B">
							        <option value="Biografici" <%=film.getGenere().equals("Biografici") ? "selected" : "" %>>Film biografici</option>
							        <option value="Blaxploitation" <%=film.getGenere().equals("Blaxploitation") ? "selected" : "" %>>Film blaxploitation</option>
							    </optgroup>
							    <optgroup label="C">
							        <option value="Catastrofici" <%=film.getGenere().equals("Catastrofici") ? "selected" : "" %>>Film catastrofici</option>
							        <option value="Comici" <%=film.getGenere().equals("Comici") ? "selected" : "" %>>Film comici</option>
							        <option value="Commedia" <%=film.getGenere().equals("Commedia") ? "selected" : "" %>>Film commedia</option>
							        <option value="Crossover" <%=film.getGenere().equals("Crossover") ? "selected" : "" %>>Film crossover</option>
							    </optgroup>
							    <optgroup label="D">
							        <option value="Documentari" <%=film.getGenere().equals("Documentari") ? "selected" : "" %>>Film documentari</option>
							        <option value="Drammatici" <%=film.getGenere().equals("Drammatici") ? "selected" : "" %>>Film drammatici</option>
							    </optgroup>
							    <optgroup label="E">
							        <option value="Epici" <%=film.getGenere().equals("Epici") ? "selected" : "" %>>Film epici</option>
							        <option value="Erotici" <%=film.getGenere().equals("Erotici") ? "selected" : "" %>>Film erotici</option>
							        <option value="Exploitation" <%=film.getGenere().equals("Exploitation") ? "selected" : "" %>>Film d'exploitation</option>
							    </optgroup>
							    <optgroup label="F">
							        <option value="Falso documentario" <%=film.getGenere().equals("Falso documentario") ? "selected" : "" %>>Film in stile falso documentario</option>
							        <option value="Fantascienza" <%=film.getGenere().equals("Fantascienza") ? "selected" : "" %>>Film di fantascienza</option>
							        <option value="Fantastici" <%=film.getGenere().equals("Fantastici") ? "selected" : "" %>>Film fantastici</option>
							        <option value="Found footage" <%=film.getGenere().equals("Found footage") ? "selected" : "" %>>Film found footage</option>
							    </optgroup>
							    <optgroup label="G">
							        <option value="Gangster" <%=film.getGenere().equals("Gangster") ? "selected" : "" %>>Film di gangster</option>
							        <option value="Gialli" <%=film.getGenere().equals("Gialli") ? "selected" : "" %>>Film gialli</option>
							        <option value="Giudiziari" <%=film.getGenere().equals("Giudiziari") ? "selected" : "" %>>Film giudiziari</option>
							        <option value="Grotteschi" <%=film.getGenere().equals("Grotteschi") ? "selected" : "" %>>Film grotteschi</option>
							        <option value="Guerra" <%=film.getGenere().equals("Guerra") ? "selected" : "" %>>Film di guerra</option>
							    </optgroup>
							    <optgroup label="H">
							        <option value="Horror" <%=film.getGenere().equals("Horror") ? "selected" : "" %>>Film horror</option>
							    </optgroup>
							    <optgroup label="M">
							        <option value="Melò" <%=film.getGenere().equals("Melò") ? "selected" : "" %>>Film melò</option>
							        <option value="Mitologici" <%=film.getGenere().equals("Mitologici") ? "selected" : "" %>>Film mitologici</option>
							        <option value="Mockbuster" <%=film.getGenere().equals("Mockbuster") ? "selected" : "" %>>Film Mockbuster</option>
							        <option value="Mondo movie" <%=film.getGenere().equals("Mondo movie") ? "selected" : "" %>>Mondo movie</option>
							        <option value="Musicali" <%=film.getGenere().equals("Musicali") ? "selected" : "" %>>Film musicali</option>
							    </optgroup>
							    <optgroup label="N">
							        <option value="Nazisploitation" <%=film.getGenere().equals("Nazisploitation") ? "selected" : "" %>>Film nazisploitation</option>
							        <option value="Noir" <%=film.getGenere().equals("Noir") ? "selected" : "" %>>Film noir</option>
							    </optgroup>
							    <optgroup label="O">
							        <option value="Opera" <%=film.getGenere().equals("Opera") ? "selected" : "" %>>Film-opera</option>
							    </optgroup>
							    <optgroup label="P">
							        <option value="Parodia" <%=film.getGenere().equals("Parodia") ? "selected" : "" %>>Film parodia</option>
							        <option value="Peplum" <%=film.getGenere().equals("Peplum") ? "selected" : "" %>>Film peplum</option>
							        <option value="Polizieschi" <%=film.getGenere().equals("Polizieschi") ? "selected" : "" %>>Film polizieschi</option>
							        <option value="Pornografici" <%=film.getGenere().equals("Pornografici") ? "selected" : "" %>>Film pornografici</option>
							        <option value="Propaganda" <%=film.getGenere().equals("Propaganda") ? "selected" : "" %>>Film di propaganda</option>
							    </optgroup>
							    <optgroup label="R">
							        <option value="Ragazzi" <%=film.getGenere().equals("Ragazzi") ? "selected" : "" %>>Film per ragazzi</option>
							        <option value="Road movie" <%=film.getGenere().equals("Road movie") ? "selected" : "" %>>Road movie</option>
							    </optgroup>
							    <optgroup label="S">
							        <option value="Satirici" <%=film.getGenere().equals("Satirici") ? "selected" : "" %>>Film satirici</option>
							        <option value="Sentimentali" <%=film.getGenere().equals("Sentimentali") ? "selected" : "" %>>Film sentimentali</option>
							        <option value="Sexploitation" <%=film.getGenere().equals("Sexploitation") ? "selected" : "" %>>Film sexploitation</option>
							        <option value="Sperimentali" <%=film.getGenere().equals("Sperimentali") ? "selected" : "" %>>Film sperimentali e d'avanguardia</option>
							        <option value="Spionaggio" <%=film.getGenere().equals("Spionaggio") ? "selected" : "" %>>Film di spionaggio</option>
							        <option value="Splatter" <%=film.getGenere().equals("Splatter") ? "selected" : "" %>>Film splatter</option>
							        <option value="Storici" <%=film.getGenere().equals("Storici") ? "selected" : "" %>>Film storici</option>
							    </optgroup>
							    <optgroup label="T">
							        <option value="Thriller" <%=film.getGenere().equals("Thriller") ? "selected" : "" %>>Film thriller</option>
							    </optgroup>
							    <optgroup label="W">
							        <option value="Western" <%=film.getGenere().equals("Western") ? "selected" : "" %>>Film western</option>
							    </optgroup>
							</select>
				
				            <label for="trama">Trama:</label>
				            <textarea id="trama" name="trama" required><%=film.getTrama()%></textarea><br>
				
				            <input type="submit" value="Salva Modifiche">
				        </form>
				    </div>
				</div>
				
				<script src="js/film.js"></script>
			
				<p id="titolo">Titolo: <%=film.getTitolo()%></p>
				<p id="anno">Anno di uscita: <a href="<%= request.getContextPath()%>/CatalogueServlet?fromYear=<%=film.getAnno()%>&toYear=<%=film.getAnno()%>"><%=film.getAnno()%></a></p>
				<p id="genere">Genere: <a href="<%= request.getContextPath()%>/CatalogueServlet?genere=<%=film.getGenere()%>"><%=film.getGenere()%></a></p>
				<p id="durata">Durata: <%=film.getDurata()%> minuti</p>
				<div id="MoviePlotContainer">
				    <p style="font-weight: bold;">Trama:</p>
				    <p id="trama"><%=film.getTrama()%></p>
				</div>
				
			</div>
		</div>
	</body>
</html>