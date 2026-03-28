const indicator = document.querySelector(".indicator");
const password = document.querySelector("#password");
const weak = document.querySelector(".weak");
const medium = document.querySelector(".medium");
const strong = document.querySelector(".strong");
const regExpWeak = /[a-z]/;
const regExpMedium = /\d+/;
const regExpStrong = /.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/;
const levels = document.querySelectorAll(".test-badge-wrapper small");
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
const colors = {
    normal: ["#F91880", "#21A663", "#FBAE3C", "#0496ff", "#FF7A00", "#7300E6"],
    light: ["#FEE7F2", "#E5F9EF", "#FEF6EB", "#E5F4FE", "#FFF1E5", "#F1EEFF"],
    border: ["#FDC5DF", "#BEF2D7", "#FEEACE", "#C0E4FF", "#FFDDBF", "#DCB9FF"],
};

function colorize(selector, mode = "text") {
    const elements = document.querySelectorAll(selector);
    elements.forEach((element, index) => {
        if (mode === "bg") {
            element.style.backgroundColor = colors.normal[index % colors.normal.length];
            element.style.color = "white";
        } else {
            element.style.color = colors.normal[index % colors.normal.length];
        }
    });
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value !== document.getElementById("password").value) {
        confirmPassword.setCustomValidity("Passwords don't match.");
    } else {
        confirmPassword.setCustomValidity("");
    }
}

function checkPasswordStrength() {
    const passwordValue = password.value.trim();
    const isNotEmpty = passwordValue !== "";
    const isWeak = passwordValue.match(regExpWeak);
    const isMedium = passwordValue.match(regExpMedium);
    const isStrong = passwordValue.match(regExpStrong);
    let strength;
    if (isNotEmpty) {
        indicator.style.display = "flex";
        if (passwordValue.length >= 6 && isWeak && isMedium && isStrong)
            strength = 3;
        else if (passwordValue.length >= 6 && ((isWeak && isMedium) || (isMedium && isStrong) || (isWeak && isStrong)))
            strength = 2;
        else if (passwordValue.length < 6)
            strength = 1;
        weak.classList.toggle("active", strength >= 1);
        medium.classList.toggle("active", strength >= 2);
        strong.classList.toggle("active", strength >= 3);
    } else {
        indicator.style.display = "none";
    }
}

colorize(".theme-line i");
colorize(".theme-circle i", "bg");

if (document.body.contains(levels[0])) {
    levels.forEach((level) => {
        if (level.innerHTML.toLowerCase() === "hard") {
            level.classList.add("hard");
        }
        if (level.innerHTML.toLowerCase() === "medium") {
            level.classList.add("medium");
        }
        if (level.innerHTML.toLowerCase() === "easy") {
            level.classList.add("easy");
        }
    });
}

if (document.body.contains(document.querySelector(".logout-link"))) {
    document.querySelector(".logout-link").addEventListener("click", event => {
        event.preventDefault();
        document.logoutForm.submit();
    });
}

$(".reviews").owlCarousel({
    loop: true,
    nav: false,
    dots: true,
    margin: 20,
    autoplay: true,
    autoplayTimeout: 5000,
    autoplayHoverPause: true,
    smartSpeed: 500,
    responsive: {
        0: {
            items: 1,
        },
        768: {
            items: 2,
        },
        992: {
            items: 3,
        },
    },
});

AOS.init({
    once: true,
});