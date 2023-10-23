package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Contiene;
import model.Film;
import model.Catalogo;
import utils.Constants;

public class ContieneDAO {
	private DataSource ds = null;
	
	public ContieneDAO(DataSource ds) {
		this.ds = ds;
	}
	
	public synchronized void doSave(Contiene contiene) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_CONTIENE + " VALUES (?, ?)";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, contiene.getCatalogo().getUtente().getUsername());
			preparedStatement.setInt(2, contiene.getFilm().getCodice());
			
			preparedStatement.executeUpdate();
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
	}
	
	public synchronized boolean doDelete(String username, int code) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_CONTIENE + " WHERE codiceCatalogo = ? " + "AND codiceFilm = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, code);
			
			result = preparedStatement.executeUpdate();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}				
		}
		
		return (result != 0);
	}
	
	// Eliminazione di tutti i film dal catalogo
	public synchronized boolean doDeleteAllByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_CONTIENE + " WHERE codiceCatalogo = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, username);
			
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
	
	public synchronized Contiene doRetrieveByKey(String username, int code) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Contiene contiene = new Contiene();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_CONTIENE + " WHERE codiceCatalogo = ? " + "AND codiceFilm = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, code);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			
			Catalogo catalogo;
			CatalogoDAO dao1 = new CatalogoDAO(ds);
			String codiceCatalogo = rs.getString("codiceCatalogo");
			catalogo = dao1.doRetrieveByKey(codiceCatalogo);
			contiene.setCatalogo(catalogo);
			
			Film film;
			FilmDAO dao2 = new FilmDAO(ds);
			int codiceFilm = rs.getInt("codiceFilm");
			film = dao2.doRetrieveByKey(codiceFilm);
			contiene.setFilm(film);
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
		
		return contiene;
	}
	
	public synchronized Collection<Contiene> doRetrieveAllByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Contiene> contieneCollection = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_CONTIENE + " WHERE codiceCatalogo = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Contiene contiene = new Contiene();
				Catalogo catalogo;
				CatalogoDAO dao1 = new CatalogoDAO(ds);
				String codiceCatalogo = rs.getString("codiceCatalogo");
				catalogo = dao1.doRetrieveByKey(codiceCatalogo);
				contiene.setCatalogo(catalogo);
				
				Film film;
				FilmDAO dao2 = new FilmDAO(ds);
				int codiceFilm = rs.getInt("codiceFilm");
				film = dao2.doRetrieveByKey(codiceFilm);
				contiene.setFilm(film);	
				
				contieneCollection.add(contiene);				
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
		
		return contieneCollection;
	}
	
	public synchronized Collection<Contiene> doRetrieveAll(String order) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Contiene> contieneCollection = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_CONTIENE;
		
		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " +order;
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Contiene contiene = new Contiene();
				Catalogo catalogo;
				CatalogoDAO dao1 = new CatalogoDAO(ds);
				String codiceCatalogo = rs.getString("codiceCatalogo");
				catalogo = dao1.doRetrieveByKey(codiceCatalogo);
				contiene.setCatalogo(catalogo);
				
				Film film;
				FilmDAO dao2 = new FilmDAO(ds);
				int codiceFilm = rs.getInt("codiceFilm");
				film = dao2.doRetrieveByKey(codiceFilm);
				contiene.setFilm(film);	
				
				contieneCollection.add(contiene);
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
		
		return contieneCollection;
	}
}