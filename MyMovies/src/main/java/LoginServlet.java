/* Questa servlet gestice l'autenticazione da parte dell'utente.
 * Carica i dati dal database e ne verifica la correttezza rispetto ai dati inseriti.
 * Infine memorizza all'interno della sessione tutte le informazioni necessarie. */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import model.Utente;
import db.UtenteDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		/* Controllo se l'utente è già autenticato */
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		if (utente != null) {
			response.sendRedirect(request.getContextPath() + "/userArea.jsp");
			return;
		}
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String error = "";
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		
		try {
			UtenteDAO utenteDAO = new UtenteDAO(ds);
			utente = utenteDAO.doRetrieveByKey(username);
			password = toHash(password);
			if (utente.getUsername().equals(username))
			{
				if (utente.getPassword().equals(password))
				{	
					System.out.println("Log in effettuato con successo");
					session.setAttribute("utente", utente);
					response.sendRedirect(request.getContextPath() + "/userArea.jsp");
				}
				else 
				{
					if (error.equals("")) {
						error += "Username o password errati";
					}
					System.out.println("password errata");
					System.out.println(utente.getPassword());
					System.out.println(password);
					request.setAttribute("error", error);
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			} else {
				if (error.equals("")) {
					error += "Username o password errati";
				}
				System.out.println("Username errato");
				request.setAttribute("error", error);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			System.err.println(e);
			if (error.equals("")) {
				error += "Username o password errati";
			}
			System.out.println("Username o password errati");
			request.setAttribute("error", error);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

	private String toHash(String password)
	{
		String hashString = null;
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			hashString = "";
			for (int i=0; i<hash.length; i++) {
				hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3);
			}
		} catch (java.security.NoSuchAlgorithmException e) {
			System.err.println(e);
		}
		
		return hashString;
	}
}
