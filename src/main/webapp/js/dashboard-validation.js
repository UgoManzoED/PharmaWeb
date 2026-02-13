document.addEventListener('DOMContentLoaded', () => {
    // Validazione Indirizzi
    const addressForm = document.querySelector('form[action*="indirizzi"]');
    if (addressForm) {
        addressForm.addEventListener('submit', (e) => {
            const cap = addressForm.querySelector('input[name="cap"]');
            const prov = addressForm.querySelector('input[name="provincia"]');
            let isValid = true;

            if (!/^\d{5}$/.test(cap.value)) {
                alert("Il CAP deve essere di 5 cifre.");
                isValid = false;
            }
            if (prov.value.length !== 2) {
                isValid = false;
            }

            if (!isValid) e.preventDefault();
        });
    }

    // Validazione Pagamenti
    const paymentForm = document.querySelector('form[action*="pagamenti"]');
    if (paymentForm) {
        paymentForm.addEventListener('submit', (e) => {
            const numero = paymentForm.querySelector('input[name="numero"]');
            if (!/^\d{13,16}$/.test(numero.value)) {
                alert("Numero carta non valido.");
                e.preventDefault();
            }
        });
    }
});