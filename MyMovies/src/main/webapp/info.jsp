<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>



<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About MyMovies</title>
    <link rel="icon" type="image/x-icon" href="imgs/favicon.ico">
    <link type="text/css" rel="stylesheet" href="css/info.css">
</head>
<body>
	<%@ include file="header.jsp"%>
	
    <div id="slider-container">
        <div class="slide aboutContainer">
            <h1>A proposito di MyMovies...</h1>
            <p>Benvenuto su MyMovies, il tuo gestionale personale di film. Qui puoi creare il tuo catalogo di film, aggiungere film al tuo elenco personale e organizzarli in raccolte personalizzate.</p>
        </div>

        <div class="slide aboutContainer">
            <h1>Catalogo Personale</h1>
            <p>Il catalogo personale ti consente di tenere traccia dei film che hai visto o che desideri vedere. Aggiungi film, gestisci la tua collezione e scopri nuovi titoli da guardare.</p>
        </div>

        <div class="slide aboutContainer">
            <h1>Raccolte Personalizzate</h1>
            <p>Crea raccolte personalizzate come "Film preferiti" e "Film più divertenti" per organizzare i tuoi film in base ai tuoi gusti.</p>
        </div>

        <div class="slide aboutContainer">
            <h1>Recensioni e Punteggi</h1>
            <p>Lascia recensioni e assegna punteggi ai film che hai visto.</p>
        </div>
    </div>

    <div class="button-container">
        <button id="prevBtn">Precedente</button>
        <button id="nextBtn">Successivo</button>
    </div>

    <script src="js/info.js"></script>
</body>
</html>

