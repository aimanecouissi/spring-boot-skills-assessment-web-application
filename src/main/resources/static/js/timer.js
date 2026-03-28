function submitForm() {
    document.getElementById('form').submit();
}

function startTimer(duration, callback) {
    let timer = duration;
    let minutes, seconds;
    const interval = setInterval(function () {
        minutes = Math.floor(timer / 60);
        seconds = timer % 60;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;
        document.getElementById('timer').textContent = '00:' + minutes + ':' + seconds;
        if (--timer < 0) {
            clearInterval(interval);
            callback();
        }
    }, 1000);
}

window.onload = function () {
    const oneMinuteThirtySeconds = 89;
    startTimer(oneMinuteThirtySeconds, submitForm);
};