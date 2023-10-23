import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.json.JSONObject;

import db.FilmDAO;
import db.RaccoltaDAO;
import db.IncludeDAO;
import model.Raccolta;
import model.Utente;
import model.Film;
import model.Include;

@WebServlet("/CollectionControlServlet")
public class CollectionControlServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = (String) request.getParameter("action");
		String error = "";
		String message = "";
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		
		if (action.equals("create")) {
		    String nome = request.getParameter("nome");
		    HttpSession session = request.getSession();
		    Utente utente = (Utente) session.getAttribute("utente");
		    String nomeUtente = utente.getUsername();
		    
		    System.out.println(nome + ", " + nomeUtente);
		    
		    boolean areGoodValues = true;
		    areGoodValues = checkParameter(nome);
		    
		    if (!areGoodValues) {
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write("{\"success\": false}");
		        return;
		    }
		    
		    Raccolta raccolta = new Raccolta();
		    raccolta.setNome(nome);
		    raccolta.setNomeUtente(nomeUtente);
		        
		    RaccoltaDAO raccoltaDAO = new RaccoltaDAO(ds);
		    
		    try {
		        raccoltaDAO.doSave(raccolta);
		        // Assumi che l'operazione di salvataggio sia riuscita
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write("{\"success\": true, \"nomeRaccolta\": \"" + nome + "\"}");
		    } catch(SQLException e) {
		        System.err.println(e);
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write("{\"success\": false}");
		    }
		}

		
		if (action.equals("update")) {
			// Modifica del nome
			
			// nomeUtente + nomeRaccolta
		    HttpSession session = request.getSession();
		    Utente utente = (Utente) session.getAttribute("utente");
		    String nomeUtente = utente.getUsername();
		    String oldName = request.getParameter("oldName");
		    String newName = request.getParameter("newName");
			
		    if(nomeUtente == null || newName == null || newName.equals("")) {
		    	System.out.println("CollectioControlServlet, update collection, error nome or nomeutente");
		    	response.sendRedirect(request.getContextPath() + "/userArea.jsp");
		    	return;
		    }
		    
		    RaccoltaDAO dao = new RaccoltaDAO(ds);
			Raccolta raccolta = new Raccolta();
			
			try {
				raccolta = dao.doRetrieveByKey(oldName, nomeUtente);
			} catch(SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");
				return;
			}
			
			JSONObject json = new JSONObject();
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			
			try {
				if (!(oldName.equals(newName))) {
					dao.doUpdate(nomeUtente, "nome", oldName, newName);
				}
			} catch (SQLException e) {
				System.err.println(e);
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");
				return;				
			}
			
			json.put("newName", newName);
			out.print(json.toString());
			
			System.out.println("Aggiornamento effettuato con successo");
		}
		
		if (action.equals("delete")) {
			String nome = request.getParameter("nome");
			RaccoltaDAO dao = new RaccoltaDAO(ds);
			HttpSession session = request.getSession();
			Utente utente = (Utente) session.getAttribute("utente");
			String nomeUtente = utente.getUsername();
			
			System.out.println(nome + ", " + nomeUtente);
			
			try {
				dao.doDelete(nome, nomeUtente);
			} catch(SQLException e) {
				System.err.println(e);
				System.out.println("Problemi con l'eliminazione della raccolta");
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");		
			}
		    
		    response.sendRedirect(request.getContextPath() + "/userArea.jsp");
		}
		
		if (action.equals("addMovie")) {
			HttpSession session = request.getSession();
			Utente utente = (Utente) session.getAttribute("utente");
			if (utente == null) {
				System.out.println("Non è stato possibile recuperare il nome utente");
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");
				return;
			}
			
			String nomeRaccolta = request.getParameter("nomeRaccolta");
			
			boolean areGoodValues = true;
			areGoodValues = checkParameter(nomeRaccolta);
			
			if (!areGoodValues) {
				error += "Impossibile aggiungere il film";
				request.setAttribute("error", error);
				System.out.println("errore nomeRaccolta");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/userArea.jsp");
				dispatcher.forward(request, response);
				return;				
			}
			
			int codiceFilm;
			try {
				codiceFilm = Integer.parseInt(request.getParameter("codiceFilm"));
			} catch (NumberFormatException e) {
				System.err.println(e);
				error += "Impossibile aggiungere il film";
				request.setAttribute("error", error);
				System.out.println("errore codiceFilm");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/userArea.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			Include include = new Include();
			include.setIdRaccolta(nomeRaccolta);
			include.setCodiceFilm(codiceFilm);
			include.setUsername(utente.getUsername());
			
			IncludeDAO includeDAO = new IncludeDAO(ds);
			
			try {
				includeDAO.doSave(include);
			} catch(SQLException e) {
				System.err.println(e);
				error += "Impossibile aggiungere il film alla raccolta";
				request.setAttribute("error", error);
				System.out.println("Impossibile aggiungere il film alla raccolta");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/userArea.jsp");
				dispatcher.forward(request, response);
				return;
			}
			
			message += "Prodotto aggiunto con successo";
			request.setAttribute("message", message);
			System.out.println("Prodotto aggiunto con successo alla raccolta)");
		    RequestDispatcher dispatcher = request.getRequestDispatcher("/userArea.jsp");
		    dispatcher.forward(request, response);
		}
		
		if (action.equals("removeMovie")) {
			HttpSession session = request.getSession();
			Utente utente = (Utente) session.getAttribute("utente");
			if (utente == null) {
				System.out.println("Non è stato possibile recuperare il nome utente");
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");
				return;
			}
			int codiceFilm = Integer.parseInt(request.getParameter("film"));
			String nomeRaccolta = (String) request.getParameter("nome");
			
			IncludeDAO includeDAO = new IncludeDAO(ds);
			try {
				includeDAO.doDelete(nomeRaccolta, codiceFilm, utente.getUsername());
			} catch (SQLException e) {
				System.err.println(e);
				System.out.println("Problemi con l'eliminazione del film nella raccolta");
				response.sendRedirect(request.getContextPath() + "/userArea.jsp");
			}
			
			System.out.println("Il film è stato eliminato dalla raccolta");
		    RequestDispatcher dispatcher = request.getRequestDispatcher("/userArea.jsp");
		    dispatcher.forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
