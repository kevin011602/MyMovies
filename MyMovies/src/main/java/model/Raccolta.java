package model;

public class Raccolta {
	private String nome;
    private String nomeUtente;
    
    Raccolta(String nome, String nomeUtente) {
    	this.nome = nome;
    	this.nomeUtente = nomeUtente;
    }
    
	public Raccolta() {
		
	}

	public String getNomeUtente() {
		return nomeUtente;
	}
	public void setNomeUtente(String nomeUtente) {
		this.nomeUtente = nomeUtente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String titolo) {
		this.nome = titolo;
	}
	
	@Override
	public String toString() {
		return "nome: " + nome + ", nome utente: " + nomeUtente;
	}
}