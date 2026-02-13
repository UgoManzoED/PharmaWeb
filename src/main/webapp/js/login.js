document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('.form-container form[action*="login"]');
    if (!form) return;

    form.addEventListener('submit', (e) => {
        const email = document.getElementById('email');
        const password = document.getElementById('password');
        let isValid = true;

        // Pulizia
        const oldErrors = form.querySelectorAll('.error-message');
        oldErrors.forEach(el => el.remove());

        if (!email.value.includes('@')) {
            showError(email, "Inserisci un'email valida.");
            isValid = false;
        }
        if (password.value.length === 0) {
            showError(password, "La password è obbligatoria.");
            isValid = false;
        }

        if (!isValid) e.preventDefault();
    });

    function showError(input, msg) {
        const span = document.createElement('span');
        span.className = 'error-message';
        span.style.color = 'red';
        span.textContent = msg;
        input.parentNode.appendChild(span);
    }
});