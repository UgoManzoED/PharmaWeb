document.addEventListener('DOMContentLoaded', () => {

    // --- 1. LOGICA PER I CAROSELLI ---
    const carousels = document.querySelectorAll('.product-carousel');

    carousels.forEach(carousel => {
        const container = carousel.querySelector('.products-container');
        const prevButton = carousel.querySelector('.carousel-arrow.prev');
        const nextButton = carousel.querySelector('.carousel-arrow.next');
        const scrollAmount = 300; // Valore di scorrimento in pixel

        if (nextButton) {
            nextButton.addEventListener('click', () => {
                container.scrollBy({ left: scrollAmount, behavior: 'smooth' });
            });
        }

        if (prevButton) {
            prevButton.addEventListener('click', () => {
                container.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
            });
        }
    });

    // --- 2. LOGICA AJAX PER AGGIUNGERE AL CARRELLO ---
    const addToCartButtons = document.querySelectorAll('.add-to-cart-button');

    addToCartButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            // Usiamo l'API Fetch per la chiamata AJAX
            fetch('cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=add&productId=${productId}`
            })
                .then(response => response.json()) // Parsiamo la risposta JSON
                .then(data => {
                    if (data.success) {
                        console.log('Prodotto aggiunto:', data.addedProductName);
                        // Aggiorniamo il contatore nell'header
                        document.getElementById('cart-count').textContent = data.cartItemCount;
                        // Mostriamo una notifica
                        showToast(`'${data.addedProductName}' è stato aggiunto al carrello!`);
                    } else {
                        showToast(`Errore: ${data.error}`, true);
                    }
                })
                .catch(error => console.error('Errore nella chiamata AJAX:', error));
        });
    });

    // --- 3. LOGICA AJAX PER AGGIUNGERE ALLA WISHLIST (molto simile) ---
    const addToWishlistButtons = document.querySelectorAll('.add-to-wishlist-button');

    addToWishlistButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            fetch('wishlist', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `action=add&productId=${productId}`
            })
                .then(response => response.json())
                .then(data => {
                    if(data.success) {
                        document.getElementById('wishlist-count').textContent = data.wishlistItemCount;
                        showToast(`'${data.addedProductName}' è stato aggiunto alla wishlist!`);
                    }
                })
                .catch(error => console.error('Errore AJAX wishlist:', error));
        });
    });

});

// Funzione helper per mostrare notifiche "toast"
function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'error' : ''}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    // Rimuove il toast dopo 3 secondi
    setTimeout(() => {
        toast.remove();
    }, 3000);
}