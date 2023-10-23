import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import model.Contiene;
import model.Film;
import model.Catalogo;
	
import db.ContieneDAO;
import db.FilmDAO;
import db.CatalogoDAO;

import org.json.JSONObject;

@WebServlet("/AddMovieToMyCatalogueServlet")
public class AddMovieToMyCatalogueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String error = "";
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		
	    String codiceCatalogo = request.getParameter("codiceCatalogo");
	    int codiceFilm = Integer.parseInt(request.getParameter("codiceFilm"));
	    
	    System.out.println(codiceFilm);
	    
	    try {
	        Catalogo catalogo = new Catalogo();
	        CatalogoDAO catalogoDAO = new CatalogoDAO(ds);
	        catalogo = catalogoDAO.doRetrieveByKey(codiceCatalogo);

	        Film film = new Film();
	        FilmDAO filmDAO = new FilmDAO(ds);
	        film = filmDAO.doRetrieveByKey(codiceFilm);

	        Contiene contiene = new Contiene();
	        ContieneDAO contieneDAO = new ContieneDAO(ds);
	        contiene.setCatalogo(catalogo);
	        contiene.setFilm(film);
	        contieneDAO.doSave(contiene);
	        
	        System.out.println("Associazione effettuata con successo");
	        
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        
	     // Creare un oggetto JSON con i dati del film aggiunto
	        String copertinaBase64 = Base64.getEncoder().encodeToString(film.getCopertina());
	        
	        JSONObject filmJSON = new JSONObject();
	        filmJSON.put("copertina", copertinaBase64);
	        filmJSON.put("titolo", film.getTitolo());
	        filmJSON.put("anno", film.getAnno());

	        // Restituisci il JSON come risposta
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(filmJSON.toString());
	        
	    } catch (SQLException e) {
	    	e.printStackTrace();
			error += "Impossibile creare l'associazione";
			request.setAttribute("error", error);
			System.out.println("errore AddMovieToMyCatalogueServlet");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/catalogo.jsp");
			dispatcher.forward(request, response);
			return;
	    }
	}
}
