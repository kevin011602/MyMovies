import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.OutputStream;
import java.sql.SQLException;

import db.FilmDAO;

@WebServlet("/ProcessMoviePosterServlet")
public class ProcessMoviePosterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        int code = Integer.parseInt(request.getParameter("code"));
        
        // Usa il codice per recuperare l'immagine BLOB dal database (usando FilmDAO)
        FilmDAO filmDAO = new FilmDAO(ds);
        byte[] imageData = null;
		try {
			imageData = filmDAO.doRetrieveByKey(code).getCopertina();
		} catch (SQLException e) {
			e.printStackTrace();
		}

        // Imposta il tipo di contenuto dell'immagine
        response.setContentType("image/jpeg");

        // Scrivi i dati dell'immagine nella risposta HTTP
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(imageData);
        outputStream.close();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
