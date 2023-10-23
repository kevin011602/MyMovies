package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Utente;
import utils.Constants;

public class UtenteDAO {
	private DataSource ds = null;
	
	public UtenteDAO(DataSource ds) {
		this.ds = ds;
	}
	
	public synchronized void doSave(Utente utente) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_UTENTE + " VALUES (?, ?)";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, utente.getUsername());
			preparedStatement.setString(2, utente.getPassword());
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
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_UTENTE + " WHERE " + Constants.NOME_UTENTE + " = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, username);
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
	
	public synchronized boolean doUpdate(String username, String columnName, String columnValue) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String updateSQL = "";
		if (columnName.equals("customer_username"))
			updateSQL += "UPDATE " + Constants.NOME_TABELLA_UTENTE + " SET nome = ? WHERE nome = ?";
		else if (columnName.equals("customer_password"))
			updateSQL += "UPDATE " + Constants.NOME_TABELLA_UTENTE + " SET password_hash = ? WHERE password_hash = ?";
		
		int result = 0;
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(updateSQL);
			preparedStatement.setString(1, columnValue);
			preparedStatement.setString(2, username);
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
	
	public synchronized Utente doRetrieveByKey(String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Utente utente = new Utente();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_UTENTE + " WHERE nome = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			utente.setUsername(rs.getString("nome"));
			utente.setPassword(rs.getString("password_hash"));
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
		
		return utente;
	}
	
	public synchronized Collection<Utente> doRetrieveAll(String order) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Utente> utenti = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_UTENTE;
		
		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Utente utente = new Utente();
				utente.setUsername(rs.getString("nome"));
				utente.setPassword(rs.getString("password_hash"));
				utenti.add(utente);
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
		
		return utenti;
	}
}
