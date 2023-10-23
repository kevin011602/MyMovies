document.getElementById("modifyImage").addEventListener("click", function () {
    document.getElementById("overlay").style.display = "block";
});

document.getElementById("overlay").addEventListener("click", function (event) {
    if (event.target === this) {
        this.style.display = "none";
    }
});