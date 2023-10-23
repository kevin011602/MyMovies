document.addEventListener("DOMContentLoaded", function() {
    // Aggiunge la classe "loaded" al body quando la pagina è completamente caricata
    document.body.classList.add("loaded");
});

var isFormVisible = false;
function toggleModulo() {
	
    var mostraFormButton = document.getElementById("mostraForm");
    isFormVisible = !isFormVisible;
    
    if (isFormVisible) {
        mostraFormButton.textContent = "-";
    } else {
        mostraFormButton.textContent = "+";
    }
	
    var formDiv = document.getElementById("nomeRaccoltaForm");
    if (formDiv.classList.contains("fade-in")) {
        formDiv.classList.remove("fade-in");
        setTimeout(function () {
            formDiv.style.display = "none";
        }, 300); // Imposta il tempo di attesa sulla stessa durata dell'animazione
    } else {
        formDiv.style.display = "block";
        setTimeout(function () {
            formDiv.classList.add("fade-in");
        }, 0);
    }
}

document.addEventListener("DOMContentLoaded", function() {
    var deleteLinks = document.querySelectorAll(".Raccolta #deleteButton");

    deleteLinks.forEach(function(deleteLink) {
        deleteLink.addEventListener("click", function(event) {
            event.preventDefault();

            var nomeRaccolta = this.getAttribute("data-nome-raccolta");
			
            var raccoltaElement = this.closest(".Raccolta");

            if (raccoltaElement) {
                raccoltaElement.remove();
            }
			
			var contextPath = "";
			var currentURL = window.location.pathname;
			if (currentURL.startsWith("/")) {
			    var secondSlashIndex = currentURL.indexOf("/", 1);
			    contextPath = currentURL.substring(0, secondSlashIndex);
			}
			
            // Esegui una richiesta AJAX per cancellare la raccolta
            var xhr = new XMLHttpRequest();
            xhr.open("GET", contextPath + "/CollectionControlServlet?action=delete&nome=" + nomeRaccolta);
            xhr.onreadystatechange = function() {
			if (xhr.readyState === 4 && xhr.status === 200) {
				// Eventuali commenti...
			}
            };

            xhr.send();
        });
    });
});

// Questo codice dovrebbe sostituire il tuo codice JavaScript esistente

$(document).ready(function() {
    // Aggiungi questo gestore di eventi per gli elementi esistenti e futuri
    $("#Raccolte").on("click", ".Raccolta #deleteButton", function(event) {
        event.preventDefault();

        var nomeRaccolta = $(this).data("nome-raccolta");

        var raccoltaElement = $(this).closest(".Raccolta");

        if (raccoltaElement) {
            raccoltaElement.remove();
        }

        var contextPath = "";
        var currentURL = window.location.pathname;
        if (currentURL.startsWith("/")) {
            var secondSlashIndex = currentURL.indexOf("/", 1);
            contextPath = currentURL.substring(0, secondSlashIndex);
        }

        // Esegui una richiesta AJAX per cancellare la raccolta
        var xhr = new XMLHttpRequest();
        xhr.open("GET", contextPath + "/CollectionControlServlet?action=delete&nome=" + nomeRaccolta);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // Eventuali commenti...
            }
        };

        xhr.send();
    });

    // Rimani su questa parte del codice
    $("#formRaccolta").submit(function(event) {
        event.preventDefault(); // Impedisce il comportamento predefinito del modulo

        var form = $(this);
        var formData = form.serialize(); // Serializza i dati del modulo

        $.ajax({
            type: form.attr("method"),
            url: form.attr("action"),
            data: formData,
            dataType: "json",
            success: function(response) {
                if (response.success) {
                    // Aggiungi il nome della nuova raccolta alla pagina senza ricaricare
                    var newRaccoltaName = response.nomeRaccolta;
                    var raccoltaDiv = document.createElement("div");
                    raccoltaDiv.className = "Raccolta";
                    raccoltaDiv.innerHTML = `
                        <div id="name">
                            <h3>
                                <a href="<%=request.getContextPath()%>/raccolta.jsp?nomeRaccolta=${newRaccoltaName}&username=<%=utente.getUsername()%>">
                                    ${newRaccoltaName}
                                </a>
                            </h3>
                        </div>
                        <a id="deleteButton" data-nome-raccolta="${newRaccoltaName}">
                            <img id="delete" src="<%=request.getContextPath()%>/imgs/delete.png" alt="Elimina">
                        </a>
                    `;

                    // Aggiungi il nuovo elemento alla lista delle raccolte
                    $("#Raccolte").append(raccoltaDiv);

                    // Pulisci il campo di input del nome della raccolta
                    $("#nomeRaccolta").val("");
                } else {
                    // Gestisci eventuali errori in modo appropriato
                    console.log("Errore durante la creazione della raccolta.");
                }
            },
            error: function(xhr, status, error) {
                // Gestisci eventuali errori qui
                console.log("Errore:", status, error);
            }
        });
    });
});


