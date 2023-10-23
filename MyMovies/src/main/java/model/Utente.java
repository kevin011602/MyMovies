package model;

public class Utente {
	// Instance variables
	private String username;
	private String password;
	
	// Constructors
	public Utente() {
		username = null;
		password = null;
	}
	
	// Get methods
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	// Set methods
	public void setUsername(String newUsername) {
		username = newUsername;
	}
	
	public void setPassword(String newPassword) {
		password = newPassword;
	}
	
	// Overriding from Object Class
	@Override
	public String toString()
	{
		return "" +username+
			   ", " +password;
	}
}
