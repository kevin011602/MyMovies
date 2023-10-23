/* Questa servlet effettua delle operazioni
 * sui film in base al valore passato
 * all'interno della query string
 * 
 * add: aggiungi un film al database
 * select: restiuisci tutti gli attributi di un film specifico del database
 * update: aggiorna uno degli attributi (o più) di un film del database
 * delete: cancella un film dal database*/

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStream;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.json.JSONObject;

import db.FilmDAO;
import model.Film;
import model.Utente;

@WebServlet("/MovieControlServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
					maxFileSize = 1024 * 1024 * 10, // 10MB
					maxRequestSize = 1024 * 1024 * 50) // 50MB
public class MovieControlServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String action = (String) request.getParameter("action");
		String error = "";
		String message = "";
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		
		if (action.equals("add")) {
			String titolo = request.getParameter("titolo");
			String annoString = request.getParameter("anno");
			String durataString = request.getParameter("durata");
			String genere = request.getParameter("genere");
			String trama = request.getParameter("trama");
			
			boolean areGoodValues = true;
			areGoodValues = checkParameter(titolo);
			areGoodValues = checkParameter(annoString);
			areGoodValues = checkParameter(durataString);
			areGoodValues = checkParameter(genere);
			areGoodValues = checkParameter(trama);
			
			if (!areGoodValues) {
				error += "Impossibile aggiungere il film";
				request.setAttribute("error", error);
				System.out.println("errore 1");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/catalogo.jsp");
				dispatcher.forward(request, response);
				return;				
			}
			
			int anno;
			int durata;
			try {
				anno = Integer.parseInt(request.getParameter("anno"));
				durata = Integer.parseInt(request.getParameter("durata"));
			} catch (NumberFormatException e) {
				System.err.println(e);
				error += "Impossibile aggiungere il film";
				request.setAttribute("error", error);
				System.out.println("errore 2");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/catalogo.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
	        // Leggi l'immagine come InputStream
	        Part filePart = request.getPart("copertina");
	        InputStream inputStream = filePart.getInputStream();
	        // Leggi i dati binari dell'immagine
	        byte[] copertina = inputStream.readAllBytes();
	        // Chiudi l'input stream
	        inputStream.close();
			
			Film film = new Film();
			film.setTitolo(titolo);
			film.setAnno(anno);
			film.setDurata(durata);
			film.setGenere(genere);
			film.setTrama(trama);
			film.setCopertina(copertina);
				
			FilmDAO filmDAO = new FilmDAO(ds);
		
			int codiceFilm = -1;
			
			try {
				// Salviamo il film e ne ricaviamo il codice auto-generato
				codiceFilm = filmDAO.doSave(film);
			} catch(SQLException e) {
				System.err.println(e);
				error += "Impossibile aggiungere il prodotto";
				request.setAttribute("error", error);
				System.out.println("errore 3");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/catalogo.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			message += "Prodotto aggiunto con successo";
			request.setAttribute("message", message);
			System.out.println("Prodotto aggiunto con successo (al database)");
			
			/* Ora è necessario creare l'associazione Catalogo -> Contiene -> Film
			 * Sfrutteremo la servlet @AddMovieToMyCatalogueServlet */
			
			// Otteniamo il codice del catalogo
			String codiceCatalogo = null;
			
			// accediamo all'attributo utente e da lì accediamo al suo username
			HttpSession session = request.getSession();
			Utente utente = (Utente) session.getAttribute("utente");
			if (utente != null) {
			    codiceCatalogo = utente.getUsername();
			} else {
				System.out.println("Non è stato possibile recuperare il nome utente e quindi il codice del catalogo");
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			
			// Chiamiamo la servlet @AddMovieToMyCatalogueServlet
			String redirectURL = request.getContextPath() + "/AddMovieToMyCatalogueServlet?codiceCatalogo=" + codiceCatalogo + "&codiceFilm=" + codiceFilm;
			response.sendRedirect(redirectURL);
		}
		
		else if (action.equals("select")) {
			String codeString = request.getParameter("code");
			int code = -1;
			if (codeString != null && !codeString.trim().equals(""))
				code = Integer.parseInt(codeString);
			
			FilmDAO dao = new FilmDAO(ds);
			Film film;
			try {
				film = dao.doRetrieveByKey(code);
			} catch(SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			
			JSONObject json = new JSONObject();
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			json.put("codice", film.getCodice());
			json.put("titolo", film.getTitolo());
			json.put("anno", film.getAnno());
			json.put("durata", film.getDurata());
			json.put("genere", film.getGenere());
			json.put("trama", film.getTrama());
			json.put("copertina", film.getCopertina());
			out.print(json.toString());
		}
		
		else if (action.equals("update")) {
			String codeString = request.getParameter("code");
			if (codeString == null || codeString.equals("")) {
				System.out.println("MovieControlServlet, update movie, error codeString");
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			
			int code = Integer.parseInt(codeString);
			FilmDAO dao = new FilmDAO(ds);
			Film film;
			
			try {
				film = dao.doRetrieveByKey(code);
			} catch (SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			
			String titolo = request.getParameter("titolo");
			String annoString = request.getParameter("anno");
			String durataString = request.getParameter("durata");
			String genere = request.getParameter("genere");
			String trama = request.getParameter("trama");
			
			// Leggi l'immagine come InputStream
	        Part filePart = request.getPart("copertina");
	        InputStream inputStream = filePart.getInputStream();
	        // Leggi i dati binari dell'immagine
	        byte[] copertina = inputStream.readAllBytes();
	        // Chiudi l'input stream
	        inputStream.close();
			
			if (annoString == null || annoString.equals("")) {
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			int anno = Integer.parseInt(annoString);
			
			if (durataString == null || durataString.equals("")) {
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;
			}
			int durata = Integer.parseInt(durataString);
			
			JSONObject json = new JSONObject();
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			try {
				if (!(film.getTitolo().equals(titolo))) {
					dao.doUpdate(code, "titolo", titolo);
				}
				if (film.getAnno() != anno) {
					dao.doUpdate(code, "anno", annoString);
				}
				if (film.getDurata() != durata) {
					dao.doUpdate(code, "durata", durataString);
				}
				if (!(film.getGenere().equals(genere))) {
					dao.doUpdate(code, "genere", genere);
				}
				if (!(film.getTrama().equals(trama))) {
					dao.doUpdate(code, "trama", trama);
				}
				if (film.getCopertina() != copertina) {
					dao.doUpdatePicture(code, "copertina", copertina);
				}
			} catch (SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
				return;				
			}
			
		    json.put("codice", code);
		    json.put("titolo", titolo);
		    json.put("anno", anno);
		    json.put("durata", durata);
		    json.put("genere", genere);
		    json.put("trama", trama);
		    json.put("copertina", copertina);

		    // Scrivi i dati JSON come risposta
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json.toString());

		    System.out.println("Aggiornamento effettuato con successo");
		}
		
		else if (action.equals("delete")) {
			int code = Integer.parseInt(request.getParameter("code"));
			FilmDAO dao = new FilmDAO(ds);
			try {
				dao.doDelete(code);
			} catch(SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/catalogo.jsp");		
			}
			response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

	
	private boolean checkParameter(String value)
	{
		if (value != null && !value.trim().equals("")) {
			return true;
		}
		
		return false;
	}
}
