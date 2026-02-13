document.addEventListener('DOMContentLoaded', () => {
    const productForm = document.querySelector('.admin-form');
    if (!productForm) return;

    productForm.addEventListener('submit', (e) => {
        const prezzo = productForm.querySelector('input[name="prezzo"]');
        const quantita = productForm.querySelector('input[name="quantita"]');
        
        if (parseFloat(prezzo.value) <= 0) {
            alert("Il prezzo deve essere maggiore di zero.");
            e.preventDefault();
        }
        if (parseInt(quantita.value) < 0) {
            alert("La quantità non può essere negativa.");
            e.preventDefault();
        }
    });
});