package db;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.apache.catalina.ssi.SSIFlastmod;

import model.Film;
import utils.Constants;
// Eventualmente implementare qui le recensioni

public class FilmDAO 
{
	private DataSource ds = null;
	
	public FilmDAO(DataSource ds)
	{
		this.ds = ds;
	}
	
	public synchronized int doSave(Film film) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    int codiceFilm = -1; // Inizializziamo con un valore che indica "non trovato"

	    String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_FILM + 
	            " (titolo, anno, durata, genere, trama, copertina)"
	            + " VALUES (?, ?, ?, ?, ?, ?)";

	    try {
	        connection = ds.getConnection();
	        preparedStatement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
	        preparedStatement.setString(1, film.getTitolo());
	        preparedStatement.setInt(2, film.getAnno());
	        preparedStatement.setInt(3, film.getDurata());
	        preparedStatement.setString(4, film.getGenere());
	        preparedStatement.setString(5, film.getTrama());
	        preparedStatement.setBytes(6, film.getCopertina());

	        int rowsInserted = preparedStatement.executeUpdate();
	        if (rowsInserted == 0) {
	            throw new SQLException("L'inserimento non ha avuto successo, nessuna riga inserita.");
	        }

	        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            codiceFilm = generatedKeys.getInt(1); // Supponendo che il codice del film sia il primo valore generato
	        } else {
	            throw new SQLException("Impossibile ottenere il codice del film generato automaticamente.");
	        }
	    } finally {
	        try {
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	        } finally {
	            if (connection != null) {
	                connection.close();
	            }
	        }
	    }

	    return codiceFilm;
	}
	
	public synchronized boolean doDelete(int code) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_FILM + " WHERE codice = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setInt(1, code);
			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return (result != 0);
	}
	
	public synchronized boolean doUpdate(int code, String columnName, String columnValue) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String updateSQL = "";
		
		try {
			connection = ds.getConnection();
			
			if (columnName.equals("titolo")) {
				updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET titolo = ? WHERE codice = ?";
				preparedStatement = connection.prepareStatement(updateSQL);
				preparedStatement.setString(1, columnValue);
			}
			
			else if (columnName.equals("anno")) {
				int anno = Integer.parseInt(columnValue);
				updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET anno = ? WHERE codice = ?";
				preparedStatement = connection.prepareStatement(updateSQL);
				preparedStatement.setInt(1, anno);
			}
			
			else if (columnName.equals("durata")) {
				int durata = Integer.parseInt(columnValue);
				updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET durata = ? WHERE codice = ?";
				preparedStatement = connection.prepareStatement(updateSQL);
				preparedStatement.setInt(1, durata);
			}
			
			else if (columnName.equals("genere")) {
				updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET genere = ? WHERE codice = ?";
				preparedStatement = connection.prepareStatement(updateSQL);
				preparedStatement.setString(1, columnValue);
			}
			
			else if (columnName.equals("trama")) {
				updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET trama = ? WHERE codice = ?";
				preparedStatement = connection.prepareStatement(updateSQL);
				preparedStatement.setString(1, columnValue);
			}
			
			else return false;
			
			preparedStatement.setInt(2, code);
			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
		
		return (result != 0);
	}
	
	public synchronized boolean doUpdatePicture(int code, String columnName, byte[] columnValue) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String updateSQL = "";
		
		try {
			connection = ds.getConnection();
		
			if (columnName.equals("copertina")) {
			    byte[] imageData = columnValue;
			    Blob copertinaBlob = connection.createBlob();
			    
			    try (OutputStream outputStream = copertinaBlob.setBinaryStream(1)) {
			        outputStream.write(imageData);
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    
			    updateSQL += "UPDATE " + Constants.NOME_TABELLA_FILM + " SET copertina = ? WHERE codice = ?";
			    preparedStatement = connection.prepareStatement(updateSQL);
			    preparedStatement.setBlob(1, copertinaBlob);
			}
			
			else return false;
			
		}  finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
		
		return (result != 0);
	}
	
	public synchronized Film doRetrieveByKey(int code) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Film film = new Film();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE codice = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, code);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			film.setCodice(rs.getInt("codice"));
			film.setTitolo(rs.getString("titolo"));
			film.setAnno(rs.getInt("anno"));
			film.setDurata(rs.getInt("durata"));
			film.setGenere(rs.getString("genere"));
			film.setTrama(rs.getString("trama"));
			film.setCopertina(rs.getBytes("copertina"));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return film;
	}
	
	/* FILTRO PER GENERE */
	public synchronized Collection<Film> doRetrieveByGenre(String genre) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Film> movies = new LinkedList<>();
		String selectSQL = "";
		
		selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE genere = ?";
		
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, genre);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setCodice(rs.getInt("codice"));
				film.setTitolo(rs.getString("titolo"));
				film.setAnno(rs.getInt("anno"));
				film.setDurata(rs.getInt("durata"));
				film.setGenere(rs.getString("genere"));
				film.setTrama(rs.getString("trama"));
				film.setCopertina(rs.getBytes("copertina"));
				
				movies.add(film);
			}
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return movies;
	}
	
	/* FILTRO PER ANNO */
	public synchronized Collection<Film> doRetrieveByYear(String fromYear, String toYear) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Film> movies = new LinkedList<>();
		String selectSQL = "";
		
		boolean isMoreThanOrLessThan = false;
		
		if (toYear == null) {
			if (fromYear != null) {
				selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE anno >= ?";
				isMoreThanOrLessThan = true;
			}
		}
		else if (fromYear == null) {
			if (toYear != null) {
				selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE anno <= ?";
				isMoreThanOrLessThan = false;
			}
		}
		else {
			selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE anno BETWEEN ? AND ?";
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			if (isMoreThanOrLessThan) {
				if (fromYear != null) {
					int from = Integer.parseInt(fromYear);
					preparedStatement.setInt(1, from);
				}
				else {
					int to = Integer.parseInt(toYear);
					preparedStatement.setInt(2, to);
				}
			} else {
				int from = Integer.parseInt(fromYear);
				int to = Integer.parseInt(toYear);
				preparedStatement.setInt(1, from);
				preparedStatement.setInt(2, to);
			}
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setCodice(rs.getInt("codice"));
				film.setTitolo(rs.getString("titolo"));
				film.setAnno(rs.getInt("anno"));
				film.setDurata(rs.getInt("durata"));
				film.setGenere(rs.getString("genere"));
				film.setTrama(rs.getString("trama"));
				film.setCopertina(rs.getBytes("copertina"));
				
				movies.add(film);
			}
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return movies;
	}
	
	/* FILTRO PER DURATA */
	public synchronized Collection<Film> doRetrieveByDuration(String fromMin, String toMin) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Film> movies = new LinkedList<>();
		String selectSQL = "";
		
		boolean isMoreThanOrLessThan = false;
		
		if (toMin == null) {
			if (fromMin != null) {
				selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE durata >= ?";
				isMoreThanOrLessThan = true;
			}
		}
		else if (fromMin == null) {
			if (toMin != null) {
				selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE durata <= ?";
				isMoreThanOrLessThan = false;
			}
		}
		else {
			selectSQL += "SELECT * FROM " + Constants.NOME_TABELLA_FILM + " WHERE durata BETWEEN ? AND ?";
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			if (isMoreThanOrLessThan) {
				if (fromMin != null) {
					int from = Integer.parseInt(fromMin);
					preparedStatement.setInt(1, from);
				}
				else {
					int to = Integer.parseInt(toMin);
					preparedStatement.setInt(2, to);
				}
			} else {
				int from = Integer.parseInt(fromMin);
				int to = Integer.parseInt(toMin);
				preparedStatement.setInt(1, from);
				preparedStatement.setInt(2, to);
			}
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Film film = new Film();
				film.setCodice(rs.getInt("codice"));
				film.setTitolo(rs.getString("titolo"));
				film.setAnno(rs.getInt("anno"));
				film.setDurata(rs.getInt("durata"));
				film.setGenere(rs.getString("genere"));
				film.setTrama(rs.getString("trama"));
				film.setCopertina(rs.getBytes("copertina"));
				
				movies.add(film);
			}
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return movies;
	}
	
	/* CREARE FILTRO PER LA VALUTAZIONE
	 * 
	 * 
	 * */
	
	public synchronized Collection<Film> doRetrieveAll(String order) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;		
		
		Collection<Film> films = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_FILM ;
		
		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
			{
				Film film = new Film();
				film.setCodice(rs.getInt("codice"));
				film.setTitolo(rs.getString("titolo"));
				film.setAnno(rs.getInt("anno"));
				film.setDurata(rs.getInt("durata"));
				film.setGenere(rs.getString("genere"));
				film.setTrama(rs.getString("trama"));
				film.setCopertina(rs.getBytes("copertina"));
				
				films.add(film);
			}
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} finally {
				if (connection != null) {
					connection.close();
				}
			}			
		}
		
		return films;
	}
}
