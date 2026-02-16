/**
 * Gestisce l'aggiornamento della quantità nel carrello.
 * 
 * @param {string} productId - L'ID del prodotto da aggiornare
 * @param {number} change - La variazione (+1 o -1)
 * @param {number} maxStock - La quantità massima disponibile in magazzino
 */
async function updateQty(productId, change, maxStock) {
    const input = document.getElementById('qty-' + productId);
    let newQty = parseInt(input.value) + change;

    if (newQty < 1 || newQty > parseInt(maxStock)) return;

    const metaToken = document.querySelector('meta[name="csrf-token"]');
    const csrfToken = metaToken?.getAttribute('content');

    try {
        const response = await fetch('cart', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded', 'X-Requested-With': 'XMLHttpRequest' },
            body: `action=update&productId=${productId}&quantity=${newQty}&csrfToken=${encodeURIComponent(csrfToken)}`
        });

        location.reload(); 
        
    } catch (error) {
        showToast("Errore durante l'aggiornamento", true);
    }
}

function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'error' : ''}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    
    // Forza reflow
    void toast.offsetWidth; 
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}