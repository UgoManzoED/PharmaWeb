document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registrationForm');
    const nome = document.getElementById('nome');
    const cognome = document.getElementById('cognome');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confermaPassword = document.getElementById('confermaPassword');

    // Pattern Regex (simili a quelli della servlet)
    const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const PASSWORD_REGEX = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

    form.addEventListener('submit', (event) => {
        // Previene l'invio del form per poterlo validare
        event.preventDefault();

        let isValid = true;

        // Rimuove tutti i messaggi di errore precedenti
        clearAllErrors();

        // Validazione Nome
        if (nome.value.trim() === '') {
            showError(nome, 'Il campo Nome è obbligatorio.');
            isValid = false;
        }

        // Validazione Cognome
        if (cognome.value.trim() === '') {
            showError(cognome, 'Il campo Cognome è obbligatorio.');
            isValid = false;
        }

        // Validazione Email
        if (!EMAIL_REGEX.test(email.value)) {
            showError(email, 'Inserisci un indirizzo email valido.');
            isValid = false;
        }

        // Validazione Password
        if (!PASSWORD_REGEX.test(password.value)) {
            showError(password, 'La password deve avere almeno 8 caratteri, una maiuscola, una minuscola e un numero.');
            isValid = false;
        }

        // Validazione Conferma Password
        if (password.value !== confermaPassword.value) {
            showError(confermaPassword, 'Le password non corrispondono.');
            isValid = false;
        }

        // Se tutto è valido, invia il form
        if (isValid) {
            form.submit();
        }
    });

    // Funzione per mostrare un errore
    function showError(inputElement, message) {
        inputElement.classList.add('invalid');
        const errorSpan = document.createElement('span');
        errorSpan.className = 'error-message';
        errorSpan.textContent = message;
        inputElement.parentNode.appendChild(errorSpan);
    }

    // Funzione per pulire tutti gli errori
    function clearAllErrors() {
        const errorMessages = form.querySelectorAll('.error-message');
        errorMessages.forEach(span => span.remove());

        const invalidInputs = form.querySelectorAll('.invalid');
        invalidInputs.forEach(input => input.classList.remove('invalid'));
    }
});