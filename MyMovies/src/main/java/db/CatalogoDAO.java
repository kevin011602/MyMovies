package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Utente;
import model.Catalogo;
import utils.Constants;

public class CatalogoDAO {
	private DataSource ds =  null;
	
	public CatalogoDAO(DataSource ds) {
		this.ds = ds;
	}
	
	public synchronized void doSave(Catalogo catalogo) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_CATALOGO + " VALUES (?, ?)";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, catalogo.getUtente().getUsername());
			preparedStatement.setInt(2, catalogo.getNumFilm());
			
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
	
	public synchronized boolean doDelete(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_CATALOGO + " WHERE " + Constants.NOME_UTENTE_CATALOGO + " = ?";
		
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
	
	public synchronized boolean doEmptyByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String updateSQL = "UPDATE " + Constants.NOME_TABELLA_CATALOGO + 
				" SET numFilm = 0 WHERE nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(updateSQL);
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
	
	public synchronized boolean doSizeUpdateByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String updateSQL = "UPDATE " + Constants.NOME_TABELLA_CATALOGO + 
				" SET numFilm = numFilm + 1 WHERE nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(updateSQL);
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
	
	public synchronized Catalogo doRetrieveByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Catalogo catalogo = new Catalogo();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_CATALOGO + " WHERE nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			Utente utente;
			UtenteDAO dao = new UtenteDAO(ds);
			String customerUsername = rs.getString("nomeUtente");
			utente = dao.doRetrieveByKey(customerUsername);
			catalogo.setUtente(utente);
			catalogo.setNumFilm(rs.getInt("numFilm"));
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}			
		}
		
		return catalogo;
	}
	
	public synchronized Collection<Catalogo> doRetrieveAll(String order) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Catalogo> cataloghi = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_CATALOGO;
		
		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " +order;
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
			{
				Catalogo catalogo = new Catalogo();
				Utente utente;
				UtenteDAO dao = new UtenteDAO(ds);
				String customerUsername = rs.getString("shopping_cart_customer_username");
				utente = dao.doRetrieveByKey(customerUsername);
				catalogo.setUtente(utente);
				catalogo.setNumFilm(rs.getInt("numFilm"));
				
				cataloghi.add(catalogo);
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
		
		return cataloghi;
	}
}
