document.addEventListener('DOMContentLoaded', () => {

    // --- 0. inizializzazione contatori ---
    // Inizializza i contatori del carrello e wishlist (se non sono già stati impostati)
    if (document.getElementById('cart-count').textContent === '0') {

    }
    if (document.getElementById('wishlist-count').textContent === '0') {

    }

    // --- 1. logica per i caroselli ---
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

    // --- 2. logica AJAX per aggiungere al carrello ---
    const addToCartButtons = document.querySelectorAll('.add-to-cart-button');

    addToCartButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            // Recupera il token CSRF dalla sessione
            const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content');
            if (!csrfToken) {
                console.error('Token CSRF non trovato');
                showToast('Errore di sicurezza. Ricarica la pagina.', true);
                return;
            }

            // Usiamo l'API Fetch per la chiamata AJAX
            fetch('cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: `action=add&productId=${productId}&csrfToken=${csrfToken}`
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        console.log('Prodotto aggiunto:', data.addedProductName);
                        // Aggiorniamo il contatore nell'header
                        document.getElementById('cart-count').textContent = data.cartItemCount;
                        // Mostriamo una notifica
                        showToast(`'${data.addedProductName}' è stato aggiunto al carrello!`);
                        // Aggiorna il token CSRF per la prossima richiesta
                        if (data.csrfToken) {
                            document.querySelector('meta[name="csrf-token"]').setAttribute('content', data.csrfToken);
                        }
                    } else {
                        showToast(`Errore: ${data.error}`, true);
                    }
                })
                .catch(error => {
                    console.error('Errore nella chiamata AJAX:', error);
                    showToast('Errore di connessione. Riprova.', true);
                });
        });
    });

    // --- 3. logica AJAX per aggiungere alla wishlist (molto simile) ---
    const addToWishlistButtons = document.querySelectorAll('.add-to-wishlist-button');

    addToWishlistButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            // Recupera il token CSRF dalla sessione
            const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content');
            if (!csrfToken) {
                console.error('Token CSRF non trovato');
                showToast('Errore di sicurezza. Ricarica la pagina.', true);
                return;
            }

            fetch('wishlist', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: `action=add&productId=${productId}&csrfToken=${csrfToken}`
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if(data.success) {
                        document.getElementById('wishlist-count').textContent = data.wishlistItemCount;
                        showToast(`'${data.addedProductName}' è stato aggiunto alla wishlist!`);
                        // Aggiorna il token CSRF per la prossima richiesta
                        if (data.csrfToken) {
                            document.querySelector('meta[name="csrf-token"]').setAttribute('content', data.csrfToken);
                        }
                    } else {
                        showToast(`Errore: ${data.error}`, true);
                    }
                })
                .catch(error => {
                    console.error('Errore AJAX wishlist:', error);
                    showToast('Errore di connessione. Riprova.', true);
                });
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