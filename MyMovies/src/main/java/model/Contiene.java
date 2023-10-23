package model;

public class Contiene {
	
	private Film film;
	private Catalogo catalogo;
	
	public Contiene() {
		film = null;
		catalogo = null;
	}

	public Film getFilm() {
		return film;
	}

	public void setFilm(Film film) {
		this.film = film;
	}

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}
	
	@Override
	public String toString() {
		return film.toString() + ", " + catalogo.toString();
	}
}