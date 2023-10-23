package utils;

public class Constants {
	// Utente
	public static final String NOME_TABELLA_UTENTE = "Utente";
	public static final String NOME_UTENTE = "nome";
	public static final String PASSWORD_UTENTE = "password_hash";
	
	// Catalogo
	public static final String NOME_TABELLA_CATALOGO = "Catalogo";
	public static final String CODICE_CATALOGO = "codice";
	public static final String NOME_UTENTE_CATALOGO = "nomeUtente";
	public static final String NUMERO_FILM_CATALOGO = "numFilm";
	
	// Film
	public static final String NOME_TABELLA_FILM = "Film";
	public static final String CODICE_FILM = "codice";
	public static final String TITOLO_FILM = "titolo";
	public static final String ANNO_FILM = "anno";
	public static final String DURATA_FILM = "durata";
	public static final String GENERE_FILM = "genere";
	public static final String TRAMA_FILM = "trama";
	public static final String COPERTINA_FILM = "copertina";
	
	// Contiene
	public static final String NOME_TABELLA_CONTIENE = "Contiene";
	public static final String CODICE_CATALOGO_CONTIENE = "codiceCatalogo";
	public static final String CODICE_FILM_CONTIENE = "codiceFilm";
	
	// Raccolta
	public static final String NOME_TABELLA_RACCOLTA = "Raccolta";
	public static final String NOME__RACCOLTA = "nome";
	public static final String NOME_UTENTE_RACCOLTA = "nomeUtente";
	
	// Include
	public static final String NOME_TABELLA_INCLUDE = "Include";
	public static final String ID_RACCOLTA_INLCUDE = "idRaccolta"; // sarebbe il nome della raccolta
	public static final String CODICE_FILM_INCLUDE = "codiceFilm";
}
