/* Questa servlet si occupa della registrazione dell'utente */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import db.UtenteDAO;
import db.CatalogoDAO;
import model.Utente;
import model.Catalogo;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		password = toHash(password);
		
		Utente utente = new Utente();
		utente.setUsername(username);
		utente.setPassword(password);
		
		String error = "";
		
		/*String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		try {
			if (!matcher.find()) {
				int numberForError = 10 / 0;
			}
			
			String usernameRegex =  "^\\w+$";
			pattern = Pattern.compile(usernameRegex);
			matcher = pattern.matcher(username);
			if (!matcher.find()) {
				int numberForError = 10 / 0;
			}
		} catch (ArithmeticException e) {
			System.err.println(e);
			error += "Registrazione fallita";
			request.setAttribute("error", error);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/signup.jsp");
			dispatcher.forward(request, response);
			return;
		} */
		
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		UtenteDAO dao = new UtenteDAO(ds);
		try {
			dao.doSave(utente);
		} catch (SQLException e) {
			System.err.println(e);
			response.sendRedirect(request.getContextPath() + "/signup.jsp");
			return;
		}
		
		CatalogoDAO catalogoDAO = new CatalogoDAO(ds);
		Catalogo catalogo = new Catalogo();
		
		try {
			utente = dao.doRetrieveByKey(username);
			
			catalogo.setUtente(utente);
			catalogo.setNumFilm(0);
			catalogoDAO.doSave(catalogo);
		} catch (SQLException e) {
			System.err.println(e);
		}
		
		response.sendRedirect(request.getContextPath() + "/userArea.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

	private String toHash(String password)
	{
		String hashString = null;
		try
		{
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			hashString = "";
			for (int i=0; i<hash.length; i++)
			{
				hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3);
			}
		}
		catch (java.security.NoSuchAlgorithmException e)
		{
			System.err.println(e);
		}
		
		return hashString;
	}
}
