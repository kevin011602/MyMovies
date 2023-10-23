package model;

public class Catalogo {
    private Utente utente;
    private int numFilm;

    // Costruttore con parametri
    public Catalogo(int codice, Utente utente, int numFilm) {
        this.utente = utente;
        this.numFilm = numFilm;
    }

    // Costruttore senza parametri
    public Catalogo() {
    	
	}

	// Metodi getter e setter per gli attributi
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public int getNumFilm() {
        return numFilm;
    }

    public void setNumFilm(int numFilm) {
        this.numFilm = numFilm;
    }

    @Override
    public String toString() {
        return "Catalogo: " + ", utente=" + utente + ", numFilm=" + numFilm + "]";
    }
}