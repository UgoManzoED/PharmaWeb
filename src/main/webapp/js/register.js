document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registrationForm');
    if (!form) return;

    const nome = document.getElementById('nome');
    const cognome = document.getElementById('cognome');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confermaPassword = document.getElementById('confermaPassword');

    // Pattern Regex
    const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const PASSWORD_REGEX = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$/;

    // Funzione helper per validare i singoli campi
    function validateField(input, condition, message) {
        if (condition) {
            showError(input, message);
            return false;
        }
        return true;
    }

    // Validazione generale
    function validateForm() {
        let isValid = true;
        clearAllErrors();

        if (!validateField(nome, nome.value.trim() === '', 'Il campo Nome è obbligatorio.')) isValid = false;
        if (!validateField(cognome, cognome.value.trim() === '', 'Il campo Cognome è obbligatorio.')) isValid = false;
        if (!validateField(email, !EMAIL_REGEX.test(email.value), 'Inserisci un indirizzo email valido.')) isValid = false;
        if (!validateField(password, !PASSWORD_REGEX.test(password.value), 'La password deve avere almeno 8 caratteri, una maiuscola, una minuscola e un numero.')) isValid = false;
        if (!validateField(confermaPassword, password.value !== confermaPassword.value, 'Le password non corrispondono.')) isValid = false;

        return isValid;
    }

    form.addEventListener('submit', (event) => {
        // Previene l'invio del form per poterlo validare
        if (!validateForm()) {
            event.preventDefault();
        }
    });

    // --- Validazione real-time al cambio input o perdita focus ---
    [nome, cognome, email, password, confermaPassword].forEach(input => {
        input.addEventListener('blur', () => {
            // Rimuove l'errore solo per questo campo specifico
            input.classList.remove('invalid');
            const existingError = input.parentNode.querySelector('.error-message');
            if (existingError) existingError.remove();
            
            // Esegue una validazione rapida "soft"
            if (input === email && email.value !== "" && !EMAIL_REGEX.test(email.value)) {
                showError(email, 'Email non valida.');
            }
        });
    });

    // Funzione per mostrare un errore (evitando duplicati)
    function showError(inputElement, message) {
        inputElement.classList.add('invalid');
        
        // Evita di aggiungere più messaggi di errore per lo stesso campo
        if (!inputElement.parentNode.querySelector('.error-message')) {
            const errorSpan = document.createElement('span');
            errorSpan.className = 'error-message';
            errorSpan.style.color = 'red'; // Feedback visivo immediato
            errorSpan.textContent = message;
            inputElement.parentNode.appendChild(errorSpan);
        }
    }

    // Funzione per pulire tutti gli errori
    function clearAllErrors() {
        const errorMessages = form.querySelectorAll('.error-message');
        errorMessages.forEach(span => span.remove());

        const invalidInputs = form.querySelectorAll('.invalid');
        invalidInputs.forEach(input => input.classList.remove('invalid'));
    }
});