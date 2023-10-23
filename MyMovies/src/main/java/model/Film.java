package model;

import java.sql.Blob;

public class Film {
    private int codice;
    private String titolo;
    private int anno;
    private int durata;
    private String genere;
    private String trama;
    private byte[] copertina;

    // Costruttore vuoto
    public Film() {
    }

    // Costruttore con parametri
    public Film(int codice, String titolo, int anno, int durata, String genere, String trama, byte[] copertina) {
        this.codice = codice;
        this.titolo = titolo;
        this.anno = anno;
        this.durata = durata;
        this.genere = genere;
        this.trama = trama;
        this.copertina = copertina;
    }

    // Metodi getter e setter per tutti i campi
    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }

    public byte[] getCopertina() {
        return copertina;
    }

    public void setCopertina(byte[] copertina) {
        this.copertina = copertina;
    }
    
    @Override
    public String toString() {
        return "Film{" +
                "codice=" + codice +
                ", titolo='" + titolo + '\'' +
                ", anno=" + anno +
                ", durata=" + durata +
                ", genere='" + genere + '\'' +
                ", trama='" + trama + '\'' +
                '}';
    }
}