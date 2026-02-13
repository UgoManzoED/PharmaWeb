document.addEventListener('DOMContentLoaded', () => {

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

    // --- Funzione Helper per gestire le chiamate ---
    async function handleAjaxAction(url, productId, successCallback) {
        // Recupera il token CSRF dalla sessione
        const metaToken = document.querySelector('meta[name="csrf-token"]');
        if (!metaToken) {
            console.error('Token CSRF non trovato');
            showToast('Errore di sicurezza. Ricarica la pagina.', true);
            return;
        }
        const csrfToken = metaToken.getAttribute('content');

        try {
            // Usiamo l'API Fetch per la chiamata AJAX
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: `action=add&productId=${productId}&csrfToken=${encodeURIComponent(csrfToken)}`
            });

            const data = await response.json();

            // Aggiorna il token CSRF per la prossima richiesta
            if (data.csrfToken) {
                metaToken.setAttribute('content', data.csrfToken);
            }

            if (!response.ok) {
                 throw new Error(data.error || `HTTP error! status: ${response.status}`);
            }

            if (data.success) {
                console.log('Prodotto aggiunto:', data.addedProductName);
                successCallback(data);
            } else {
                showToast(`Errore: ${data.error}`, true);
            }

        } catch (error) {
            console.error('Errore nella chiamata AJAX:', error);
            showToast('Errore di connessione. Riprova.', true);
        }
    }

    // --- 2. logica AJAX per aggiungere al carrello ---
    const addToCartButtons = document.querySelectorAll('.add-to-cart-button');

    addToCartButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            // Chiamata centralizzata verso la servlet 'cart'
            handleAjaxAction('cart', productId, (data) => {
                // Aggiorniamo il contatore nell'header
                document.getElementById('cart-count').textContent = data.cartItemCount;
                // Mostriamo una notifica
                showToast(`'${data.addedProductName}' è stato aggiunto al carrello!`);
            });
        });
    });

    // --- 3. logica AJAX per aggiungere alla wishlist ---
    const addToWishlistButtons = document.querySelectorAll('.add-to-wishlist-button');

    addToWishlistButtons.forEach(button => {
        button.addEventListener('click', () => {
            const productId = button.dataset.productId;

            // Chiamata centralizzata verso la servlet 'wishlist'
            handleAjaxAction('wishlist', productId, (data) => {
                const wishlistCounter = document.getElementById('wishlist-count');
                if(wishlistCounter) wishlistCounter.textContent = data.wishlistItemCount;
                
                showToast(`'${data.addedProductName}' è stato aggiunto alla wishlist!`);
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

    // Forza il reflow per l'animazione
    void toast.offsetWidth; 
    toast.classList.add('show');
    
    // Rimuove il toast dopo 3 secondi
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300); 
    }, 3000);
}