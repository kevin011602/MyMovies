

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Utente;

@WebServlet("/UserRedirectServlet")
public class UserRedirectServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute("utente");
		
		// Caso in cui l'utente abbia già effettuato l'accesso
		if (utente != null) {
			response.sendRedirect(request.getContextPath() + "/userArea.jsp");
		}
		
		// Caso in cui l'utente non abbia effettuato l'accesso
		else {
			response.sendRedirect(request.getContextPath() + "/login.jsp");		
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}