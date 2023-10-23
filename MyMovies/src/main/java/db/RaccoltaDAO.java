package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

import javax.sql.DataSource;

import model.Raccolta;
import utils.Constants;

public class RaccoltaDAO {
		private DataSource ds =  null;
		
		public RaccoltaDAO(DataSource ds) {
			this.ds = ds;
		}
		
		// Crea una nuova raccolta
		public synchronized void doSave(Raccolta raccolta) throws SQLException {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet generatedKeys = null;

		    String insertSQL = "INSERT INTO " + Constants.NOME_TABELLA_RACCOLTA + " VALUES (?, ?)";

		    try {
		        connection = ds.getConnection();
		        preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		        preparedStatement.setString(1, raccolta.getNome());
		        preparedStatement.setString(2, raccolta.getNomeUtente());
		        
		        preparedStatement.executeUpdate();
		    } finally {
		        try {
		            if (generatedKeys != null) {
		                generatedKeys.close();
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
		    }
		}
		
		// Cancella una raccolta
		public synchronized boolean doDelete(String nome, String username) throws SQLException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			
			int result = 0;
			String deleteSQL = "DELETE FROM " + Constants.NOME_TABELLA_RACCOLTA + " WHERE nome = ? AND nomeUtente = ?";
			
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(deleteSQL);
				preparedStatement.setString(1, nome);
				preparedStatement.setString(2, username);
				result = preparedStatement.executeUpdate();
			}
			finally {
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
		
		public synchronized boolean doUpdate(String username, String columnName, String oldColumnValue, String newColumnValue) throws SQLException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			
			int result = 0;
			String updateSQL = "";
			
			try {
				connection = ds.getConnection();
				
				if (columnName.equals("nome")) {
					updateSQL += "UPDATE " + Constants.NOME_TABELLA_RACCOLTA + " SET nome = ? WHERE nome = ? AND nomeUtente = ?";
					preparedStatement = connection.prepareStatement(updateSQL);
					preparedStatement.setString(1, newColumnValue);
					preparedStatement.setString(2, oldColumnValue);
					preparedStatement.setString(3, username);
				}
				
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
		
		
		public synchronized Raccolta doRetrieveByKey(String nome, String username) throws SQLException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			
			Raccolta raccolta = new Raccolta();
			String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_RACCOLTA + " WHERE nome = ? AND nomeUtente = ?";
			
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(selectSQL);
				preparedStatement.setString(1, nome);
				preparedStatement.setString(2, username);
				
				ResultSet rs = preparedStatement.executeQuery();
				rs.next();
				raccolta.setNome(rs.getString("nome"));
				raccolta.setNomeUtente(rs.getString("nomeUtente"));
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
			
			return raccolta;
		}
		
		public synchronized Collection<Raccolta> doRetrieveAll(String order) throws SQLException {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			
			Collection<Raccolta> raccolte = new LinkedList<>();
			String selectSQL = "SELECT * FROM " + Constants.NOME_TABELLA_RACCOLTA;
			
			if (order != null && !order.equals(""))
				selectSQL += " ORDER BY " + order;
			
			try {
				connection = ds.getConnection();
				preparedStatement = connection.prepareStatement(selectSQL);
				
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					Raccolta raccolta = new Raccolta();
					raccolta.setNome(rs.getString("nome"));
					raccolta.setNomeUtente(rs.getString("nomeUtente"));
					
					raccolte.add(raccolta);
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
			
			return raccolte;
		}
	}