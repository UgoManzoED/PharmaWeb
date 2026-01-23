document.addEventListener('DOMContentLoaded', () => {
    
    // --- Gestione bottoni aggiungi carrello/wishlist ---
    const addToCartBtn = document.querySelector('.add-to-cart-btn-large');
    const addToWishlistBtn = document.querySelector('.add-to-wishlist-btn-large');
    
    // Aggiungi al carrello (usa la stessa logica di home.js)
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', () => {
            const productId = addToCartBtn.dataset.productId;
            const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content');
            
            if (!csrfToken) {
                console.error('Token CSRF non trovato');
                showToast('Errore di sicurezza. Ricarica la pagina.', true);
                return;
            }
            
            // Disabilita temporaneamente il bottone
            addToCartBtn.disabled = true;
            addToCartBtn.textContent = 'Aggiunta in corso...';
            
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
                    document.getElementById('cart-count').textContent = data.cartItemCount;
                    showToast(`'${data.addedProductName}' è stato aggiunto al carrello!`);
                    
                    // Ripristina il bottone
                    addToCartBtn.disabled = false;
                    addToCartBtn.innerHTML = '🛒 Aggiungi al carrello';
                    
                    if (data.csrfToken) {
                        document.querySelector('meta[name="csrf-token"]').setAttribute('content', data.csrfToken);
                    }
                } else {
                    showToast(`Errore: ${data.error}`, true);
                    addToCartBtn.disabled = false;
                    addToCartBtn.innerHTML = '🛒 Aggiungi al carrello';
                }
            })
            .catch(error => {
                console.error('Errore nella chiamata AJAX:', error);
                showToast('Errore di connessione. Riprova.', true);
                addToCartBtn.disabled = false;
                addToCartBtn.innerHTML = '🛒 Aggiungi al carrello';
            });
        });
    }
    
    // Aggiungi alla wishlist
    if (addToWishlistBtn) {
        addToWishlistBtn.addEventListener('click', () => {
            const productId = addToWishlistBtn.dataset.productId;
            const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content');
            
            if (!csrfToken) {
                console.error('Token CSRF non trovato');
                showToast('Errore di sicurezza. Ricarica la pagina.', true);
                return;
            }
            
            addToWishlistBtn.disabled = true;
            addToWishlistBtn.textContent = 'Aggiunta in corso...';
            
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
                    
                    addToWishlistBtn.disabled = false;
                    addToWishlistBtn.innerHTML = '❤️ Aggiungi ai preferiti';
                    
                    if (data.csrfToken) {
                        document.querySelector('meta[name="csrf-token"]').setAttribute('content', data.csrfToken);
                    }
                } else {
                    showToast(`Errore: ${data.error}`, true);
                    addToWishlistBtn.disabled = false;
                    addToWishlistBtn.innerHTML = '❤️ Aggiungi ai preferiti';
                }
            })
            .catch(error => {
                console.error('Errore AJAX wishlist:', error);
                showToast('Errore di connessione. Riprova.', true);
                addToWishlistBtn.disabled = false;
                addToWishlistBtn.innerHTML = '❤️ Aggiungi ai preferiti';
            });
        });
    }
    
    // --- Contatore caratteri textarea recensione ---
    const reviewTextarea = document.getElementById('reviewText');
    const charCount = document.querySelector('.char-count');
    
    if (reviewTextarea && charCount) {
        reviewTextarea.addEventListener('input', () => {
            const length = reviewTextarea.value.length;
            charCount.textContent = `${length}/1000`;
            
            if (length > 900) {
                charCount.style.color = '#dc3545';
            } else {
                charCount.style.color = '#666';
            }
        });
    }
    
    // --- Validazione form recensione ---
    const reviewForm = document.getElementById('reviewForm');
    
    if (reviewForm) {
        reviewForm.addEventListener('submit', (e) => {
            const ratingInputs = reviewForm.querySelectorAll('input[name="rating"]');
            let hasRating = false;
            
            ratingInputs.forEach(input => {
                if (input.checked) {
                    hasRating = true;
                }
            });
            
            if (!hasRating) {
                e.preventDefault();
                showToast('Per favore, seleziona una valutazione da 1 a 5 stelle.', true);
            }
        });
    }
    
});

// Funzione helper per mostrare notifiche toast
function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'error' : ''}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}
