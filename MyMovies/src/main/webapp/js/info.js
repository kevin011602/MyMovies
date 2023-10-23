const slides = document.querySelectorAll(".slide");
const prevButton = document.getElementById("prevBtn");
const nextButton = document.getElementById("nextBtn");

let currentIndex = 0;

function showSlide(index) {
    slides.forEach((slide, i) => {
        if (i === index) {
            slide.classList.add("active");
        } else {
            slide.classList.remove("active");
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    showSlide(currentIndex);

    prevButton.addEventListener("click", () => {
        currentIndex = (currentIndex - 1 + slides.length) % slides.length;
        showSlide(currentIndex);
    });

    nextButton.addEventListener("click", () => {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
    });
});
