/**
 * Gestisce l'aggiornamento della quantità nel carrello.
 * 
 * @param {string} productId - L'ID del prodotto da aggiornare
 * @param {number} change - La variazione (+1 o -1)
 * @param {number} maxStock - La quantità massima disponibile in magazzino
 */
function updateQty(productId, change, maxStock) {
    // Recupera l'input della quantità tramite ID univoco
    const input = document.getElementById('qty-' + productId);
    
    if (!input) {
        console.error("Input quantità non trovato per prodotto: " + productId);
        return;
    }

    let currentQty = parseInt(input.value);
    let newQty = currentQty + change;

    // Controllo minimo: non scendere sotto 1 (per rimuovere c'è il tasto apposito)
    if (newQty < 1) {
        return;
    }

    // Controllo massimo: non superare la disponibilità di magazzino
    if (newQty > parseInt(maxStock)) {
        alert("Spiacenti, hai raggiunto la quantità massima disponibile per questo prodotto.");
        return;
    }

    // Aggiorna visivamente l'input
    input.value = newQty;

    // Invia automaticamente il form per aggiornare il server
    input.form.submit();
}