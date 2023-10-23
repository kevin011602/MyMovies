// script.js

document.addEventListener("DOMContentLoaded", function () {
        const mostraFormButton = document.getElementById("mostra-form");
        const moduloInserimento = document.getElementById("modulo-inserimento");
        const rimuoviFotoButton = document.getElementById("rimuovi-foto");
        const inputLocandina = document.getElementById("poster");

        mostraFormButton.addEventListener("click", function () {
            if (moduloInserimento.style.display === "none" || moduloInserimento.style.display === "") {
                moduloInserimento.style.display = "block";
            } else {
                moduloInserimento.style.display = "none";
            }
        });

        rimuoviFotoButton.addEventListener("click", function () {
            // Reimposta il valore dell'input del file
            inputLocandina.value = "";
        });
        
        // Aggiunge la classe "loaded" al body quando la pagina è completamente caricata
  		document.body.classList.add("loaded");
  		
	  	// Aggiungi il gestore per l'evento "click" al pulsante "Ripristina"
	    var ripristinaButton = document.querySelector(".pulsantiFiltri-container button:nth-child(2)");
	    ripristinaButton.addEventListener("click", function (e) {
	        // Impediamo l'invio del form
	        e.preventDefault();
	        // Ripristiniamo il form
	        var form = document.getElementById("filtroForm");
	        form.reset();
	    });
});

$(document).ready(function() {
    $('#poster').on('change', function() {
        if ($(this).val() !== '') {
            $('#rimuovi-foto').css('opacity', '1').removeClass('hidden');
        } else {
            $('#rimuovi-foto').css('opacity', '0');
        }
    });

    $('#rimuovi-foto').on('click', function() {
        $('#poster').val('');
        $(this).css('opacity', '0');
    });
});

function checkGenreSelection() {
    var genreSelect = document.getElementById("genere");
    if (genreSelect.value === "") {
        alert("Si prega di selezionare un genere.");
        return false; // Impedisce l'invio del modulo se il genere non è stato selezionato
    }
    return true; // Permette l'invio del modulo se un genere è stato selezionato
}

$(document).ready(function () {
    // Quando il form viene inviato
    $("#form-inserimento-film").submit(function (e) {
        e.preventDefault(); // Evita il comportamento predefinito di invio del form

        console.log("Form inviato");

        // Ottieni il contesto dell'applicazione (context path)
        var contextPath = "";
        var currentURL = window.location.pathname;
        if (currentURL.startsWith("/")) {
            var secondSlashIndex = currentURL.indexOf("/", 1);
            contextPath = currentURL.substring(0, secondSlashIndex);
        }

        // Esegue una chiamata AJAX per inviare i dati del form
        $.ajax({
            type: "POST",
            url: contextPath + "/MovieControlServlet?action=add", // Imposta l'URL del servlet
            data: new FormData(this), // Invia i dati del form
            processData: false,
            contentType: false,

            success: function (response) {
                // Gestisci la risposta JSON qui
                var film = response;

                // Converti la rappresentazione base64 in un oggetto blob per l'immagine
                var copertinaData = atob(film.copertina);
                var copertinaArray = new Uint8Array(copertinaData.length);

                for (var i = 0; i < copertinaData.length; i++) {
                    copertinaArray[i] = copertinaData.charCodeAt(i);
                }

                var copertinaBlob = new Blob([copertinaArray], { type: 'image/jpeg' });

                // Ora puoi visualizzare l'immagine
                var copertinaURL = URL.createObjectURL(copertinaBlob);

                // Crea un nuovo elemento per il film appena aggiunto
                var nuovoFilmElement = document.createElement('div');
                nuovoFilmElement.className = 'Film';

                // Aggiungi l'immagine al nuovo elemento
                var immagineElement = document.createElement('img');
                immagineElement.src = copertinaURL;
                nuovoFilmElement.appendChild(immagineElement);

                // Aggiungi il titolo del film al nuovo elemento
                var titoloElement = document.createElement('h3');
                var titoloLink = document.createElement('a');
                titoloLink.href = contextPath + '/film.jsp?codiceFilm=' + film.codice;
                titoloLink.textContent = film.titolo + ' (' + film.anno + ')';
                titoloElement.appendChild(titoloLink);
                nuovoFilmElement.appendChild(titoloElement);

                // Aggiungi il tasto per eliminare il film al nuovo elemento
                var eliminaElement = document.createElement('a');
                eliminaElement.href = contextPath + '/MovieControlServlet?action=delete&code=' + film.codice;
                var eliminaImg = document.createElement('img');
                eliminaImg.src = contextPath + '/imgs/delete.png';
                eliminaImg.alt = 'Elimina';
                eliminaElement.appendChild(eliminaImg);
                nuovoFilmElement.appendChild(eliminaElement);

                // Aggiungi il nuovo elemento al catalogo dei film
                var filmCatalogo = document.getElementById('FilmCatalogo');
                filmCatalogo.appendChild(nuovoFilmElement);

                applyFilmStyles(nuovoFilmElement);

                // Ripristina i campi del form
                $('#poster').val("");
                $('#title').val('');
                $('#anno').val('');
                $('#durata').val('');
                $('#genere').val('');
                $('#trama').val('');
            },

            error: function (jqXHR, textStatus, errorThrown) {
                // Gestisci gli errori qui
                alert("Errore durante l'aggiunta del film: " + errorThrown);
            }
        });
    });
});

function applyFilmStyles(filmElement) {
    // Seleziona l'immagine all'interno dell'elemento film
    var imgElement = filmElement.querySelector('img');

    // Applica gli stili CSS all'immagine
    imgElement.style.height = '100%';
    imgElement.style.width = '100%';
    imgElement.style.objectFit = 'cover';

    // Aggiungi overlay e effetto hover
    var overlay = document.createElement('div');
    overlay.className = 'overlay';
    filmElement.appendChild(overlay);

    filmElement.addEventListener('mouseover', function() {
        overlay.style.opacity = '1';
    });

    filmElement.addEventListener('mouseout', function() {
        overlay.style.opacity = '0';
    });

    // Seleziona l'elemento del titolo del film
    var titleElement = filmElement.querySelector('h3');

    // Applica gli stili CSS al titolo
    titleElement.style.color = 'white';
    titleElement.style.fontSize = '20px';
    titleElement.style.position = 'absolute';
    titleElement.style.top = '40%';
    titleElement.style.left = '50%';
    titleElement.style.transform = 'translate(-50%, -50%)';
    titleElement.style.msTransform = 'translate(-50%, -50%)';

    // Seleziona il link all'interno del titolo
    var titleLink = titleElement.querySelector('a');

    // Applica gli stili CSS al link
    titleLink.style.color = 'white';
    titleLink.style.textDecoration = 'none';

    filmElement.style.position = 'relative'; // Aggiungi questa riga per posizionare correttamente l'overlay
}

