const CANVAS = document.querySelector("#confetti");
const JSCONFETTI = new JSConfetti();
window.onload = () => {
    JSCONFETTI.addConfetti({
        confettiColors: ["#F91880", "#21A663", "#FBAE3C", "#0496ff", "#7856FF", "#FF7A00"],
    })
};