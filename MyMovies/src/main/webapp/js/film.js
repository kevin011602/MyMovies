document.getElementById("modifyImage").addEventListener("click", function () {
    document.getElementById("overlay").style.display = "block";
});

document.getElementById("overlay").addEventListener("click", function (event) {
    if (event.target === this) {
        this.style.display = "none";
    }
});

// Aggiungi un gestore di eventi per il modulo di modifica
document.getElementById("movieEditForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Impedisce il comportamento predefinito del modulo

    // Raccogli i dati dal modulo
    var formData = new FormData(this);

    // Effettua una richiesta AJAX per l'aggiornamento
    $.ajax({
        type: 'POST',
        url: contextPath + '/MovieControlServlet?action=update',
        data: formData,
        contentType: false,
        processData: false,
        success: function(data) {
			console.log(data);
			
            // Aggiorna dinamicamente gli elementi della pagina con i nuovi dati
            $("#trama").text(data.trama);
            $("#titolo").text(data.titolo);
            $("#anno").text(data.anno);
            $("#genere").text(data.genere);
            $("#durata").text(data.durata);
            $("#anno").text(data.anno);
            
            // Aggiorna l'immagine della copertina (assicurati che l'ID sia corretto)
            $("#poster").attr("src", contextPath + "/ProcessMoviePosterServlet?code=" + data.codice);

            // Nascondi il form di modifica dopo l'aggiornamento
            document.getElementById("overlay").style.display = "none";
        },
        error: function(xhr, status, error) {
            // Gestisci eventuali errori
        }
    });
});

