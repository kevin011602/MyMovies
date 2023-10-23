package model;

public class Include {
    private String idRaccolta;
    private int codiceFilm;
    private String username; // chiave esterna dell'utente
    
    Include(String idRaccolta, int codiceFilm) {
    	this.idRaccolta = idRaccolta;
    	this.codiceFilm = codiceFilm;
    }
    
	public Include() {
		
	}

	public String getIdRaccolta() {
		return idRaccolta;
	}
	public void setIdRaccolta(String idRaccolta) {
		this.idRaccolta = idRaccolta;
	}
	public int getCodiceFilm() {
		return codiceFilm;
	}
	public void setCodiceFilm(int codiceFilm) {
		this.codiceFilm = codiceFilm;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "id raccolta: " + idRaccolta + ", codice film: " + codiceFilm;
	}
}