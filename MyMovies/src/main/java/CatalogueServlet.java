import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import db.FilmDAO;
import model.Film;

@WebServlet("/CatalogueServlet")
public class CatalogueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
    	
    	Collection<Film> movies = null;
    	FilmDAO dao = new FilmDAO(ds);
    	String genere = request.getParameter("genere");
    	
		String fromYear = request.getParameter("fromYear");
		String toYear = request.getParameter("toYear");
		
		String fromMin = request.getParameter("fromMin");
		String toMin = request.getParameter("toMin");
    	
    	String valutazione = request.getParameter("valutazione");
    	
    	String searchBar = request.getParameter("searchBar");
    	
    	try {
    		// SE NON E' STATO SCELTO ALCUN FILTRO
    		if (genere == null &&
    			fromYear == null &&
    			toYear == null &&
    			fromMin == null &&
    			toMin == null &&
    			searchBar == null) {
    			movies = dao.doRetrieveAll(null);
    		}
    		else {
	    		// SE E' STATO APPLICATO IL FILTRO "GENERE"
	    		if (genere != null) {
		    		movies = dao.doRetrieveByGenre(genere);
		    	}
	    		
	    		// SE E' STATO APPLICATO IL FILTRO "ANNO"
	    		if (fromYear != null || toYear != null) {
		    		if(toYear == null) {
		    			movies = dao.doRetrieveByYear(fromYear, null);
		    		} else if(fromYear == null) {
		    			movies = dao.doRetrieveByYear(null, toYear);
		    		} else {
		    			movies = dao.doRetrieveByYear(fromYear, toYear);
		    		}
	    		}
	    		
	    		// SE E' STATO APPLICATO IL FILTRO "DURATA"
	    		if (fromMin != null || toMin != null) {
		    		if(toMin == null) {
		    			movies = dao.doRetrieveByDuration(fromMin, null);
		    		} else if(fromMin == null) {
		    			movies = dao.doRetrieveByDuration(null, toMin);
		    		} else {
		    			movies = dao.doRetrieveByDuration(fromMin, toMin);
		    		}
	    		}
	    		
	    		/* SE E' STATO APPLICATO IL FILTRO "VALUTAZIONE"
	    		if (valutazione != null) {
	    			String starsString = request.getParameter("stars");
	    			int stars = -1;
	    			if (starsString != null)
	    				stars = Integer.parseInt(starsString);
	    			
	    			movies = dao.doRetrieveByFilter(stars);
	    		} */
    		}
    		
    		// SE E' STATA EFFETTUATA UNA RICERCA
    		if (searchBar != null) {
    			if (searchBar.isEmpty()) {
    					movies = dao.doRetrieveAll(null);
    			}
    			
    			else {
    					String searchInput = searchBar.toLowerCase();
    					Collection<Film> moviesCollection = new LinkedList<>();
    					movies = dao.doRetrieveAll(null);
    					Iterator<Film> it = movies.iterator();
    					while (it.hasNext())
    					{
    						Film film = it.next();
    						String titoloFilm = ("" + film.getTitolo()).toLowerCase();
    						
    						if (titoloFilm.contains(searchInput))
    						{
    							moviesCollection.add(film);
    						}
    					}
    					
    					movies = moviesCollection;
    				}
    		}
    	}
    	catch(SQLException e) {
    		System.err.println(e);
    	}
    	
        request.setAttribute("movies", movies);

        RequestDispatcher dispatcher = request.getRequestDispatcher("catalogo.jsp");
        try  {
			dispatcher.forward(request, response);
		}  catch (ServletException e) {
        	System.err.println(e);
		} catch (IOException e) {
        	System.err.println(e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
