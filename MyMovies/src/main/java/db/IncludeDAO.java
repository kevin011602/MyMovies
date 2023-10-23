package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Catalogo;
import model.Contiene;
import model.Film;
import model.Include;
import model.Raccolta;
import utils.Constants;

public class IncludeDAO {
	private DataSource ds = null;
	
	public IncludeDAO(DataSource ds) {
		this.ds = ds;
	}
	
	// Salva un film sul catalogo
	public synchronized void doSave(Include include) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_INCLUDE + " VALUES (?, ?, ?)";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, include.getIdRaccolta());
			preparedStatement.setInt(2, include.getCodiceFilm());
			preparedStatement.setString(3, include.getUsername());
			
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
	
	// Cancella un film dal catalogo
	public synchronized boolean doDelete(String id, int code, String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_INCLUDE + " WHERE idRaccolta = ? AND codiceFilm = ? AND nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, id);
			preparedStatement.setInt(2, code);
			preparedStatement.setString(3, username);
			
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
	
	// Cancella tutti i film dalla raccolta x
	public synchronized boolean doDeleteAllByKey(String id, String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		int result = 0;
		String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_INCLUDE + " WHERE idRaccolta = ? AND nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(1, id);
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
	
	// Restituisci l'associazione Raccolta x include il film y
	public synchronized Include doRetrieveByKey(String id, int code, String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Include include = new Include();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_INCLUDE + " WHERE idRaccolta = ? AND codiceFilm = ? AND nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			preparedStatement.setInt(2, code);
			preparedStatement.setString(3, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			include.setIdRaccolta(rs.getString("idRaccolta"));
			include.setCodiceFilm(rs.getInt("codiceFilm"));
			include.setUsername(rs.getString("nomeUtente"));
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
		
		return include;
	}
	
	// Restituisce tutte le associazioni, raccolta x include i seguenti film:
	public synchronized Collection<Include> doRetrieveAllByKey(String id, String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Include> includeCollection = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_INCLUDE + " WHERE idRaccolta = ? AND nomeUtente = ?";
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Include include = new Include();
				include.setIdRaccolta(rs.getString("idRaccolta"));
				include.setCodiceFilm(rs.getInt("codiceFilm"));	
				include.setUsername(rs.getString("nomeUtente"));
				includeCollection.add(include);				
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
		
		return includeCollection;
	}
	
	// Restituisce tutte le associazioni, tutte le raccolte in totale includono i seguenti film:
	public synchronized Collection<Include> doRetrieveAll(String order, String username) throws SQLException
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		Collection<Include> includeCollection = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_INCLUDE + " WHERE nomeUtente = ?";
		
		if (order != null && !order.equals("")) {
			selectSQL += " ORDER BY " + order;
		}
		
		try {
			connection = ds.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Include include = new Include();
				include.setIdRaccolta(rs.getString("idRaccolta"));
				include.setCodiceFilm(rs.getInt("codiceFilm"));	
				include.setUsername(rs.getString("nomeUtente"));
				
				includeCollection.add(include);
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
		
		return includeCollection;
	}
}