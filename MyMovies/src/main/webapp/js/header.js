document.addEventListener("DOMContentLoaded", function() {
    var catalogoLink = document.getElementById("catalogoLink");
    var areaUtenteLink = document.getElementById("areaUtenteLink");
    var infoLink = document.getElementById("infoLink");

    var currentURL = window.location.href;

    if (currentURL.indexOf("catalogo.jsp") !== -1 || currentURL.indexOf("CatalogueServlet") !== -1) {
        catalogoLink.classList.add("current-page");
    }
    else if (currentURL.indexOf("userArea.jsp") !== -1) {
		areaUtenteLink.classList.add("current-page");
	}
	else if(currentURL.indexOf("info.jsp") !== -1) {
		infoLink.classList.add("current-page");
	}
});