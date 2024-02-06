DROP DATABASE IF EXISTS MyMovies;
CREATE DATABASE MyMovies;
USE MyMovies;

DROP TABLE IF EXISTS Utente;
CREATE TABLE Utente (
	nome CHAR(30) NOT NULL PRIMARY KEY,
    password_hash CHAR(200) NOT NULL
);

DROP TABLE IF EXISTS Catalogo;
CREATE TABLE Catalogo (
    nomeUtente CHAR(30) NOT NULL,
    numFilm INT NOT NULL,
    
    PRIMARY KEY (nomeUtente),
    
    FOREIGN KEY (nomeUtente) REFERENCES Utente(nome)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

DROP TABLE IF EXISTS Film;
CREATE TABLE Film (
    codice INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	titolo char(50) NOT NULL,
    anno YEAR,
    durata INT,
    genere CHAR(30),
    trama TEXT,
    copertina LONGBLOB
);

DROP TABLE IF EXISTS Contiene;
CREATE TABLE Contiene (
    codiceCatalogo char(30) NOT NULL,
    codiceFilm INT NOT NULL,
    
    PRIMARY KEY (codiceCatalogo, codiceFilm),
    FOREIGN KEY (codiceCatalogo) REFERENCES Catalogo(nomeUtente)
    ON DELETE CASCADE,
    FOREIGN KEY (codiceFilm) REFERENCES Film(codice)
    ON DELETE CASCADE
);

USE MyMovies;
DROP TABLE IF EXISTS Raccolta;
CREATE TABLE Raccolta (
	nome CHAR(50) NOT NULL,
    nomeUtente CHAR(30) NOT NULL,
    
    PRIMARY KEY(nome, nomeUtente),
    FOREIGN KEY (nomeUtente) REFERENCES Utente(nome)
);

DROP TABLE IF EXISTS Include;
CREATE TABLE Include (
    idRaccolta CHAR(50) NOT NULL,
    codiceFilm INT NOT NULL,
    nomeUtente CHAR(30) NOT NULL,
    
    PRIMARY KEY (idRaccolta, nomeUtente, codiceFilm),
    FOREIGN KEY (idRaccolta, nomeUtente) REFERENCES Raccolta(nome, nomeUtente)
      ON UPDATE CASCADE
      ON DELETE CASCADE,
    FOREIGN KEY (codiceFilm) REFERENCES Film(codice)
);
