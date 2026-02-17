document.addEventListener('DOMContentLoaded', () => {
    
    // --- Gestione bottoni aggiungi carrello/wishlist ---
    const addToCartBtn = document.querySelector('.add-to-cart-btn-large');
    const addToWishlistBtn = document.querySelector('.add-to-wishlist-btn-large');
    
    // Funzione generica per gestire le chiamate AJAX
    async function handleAjaxAction(url, productId, btn) {
        const metaToken = document.querySelector('meta[name="csrf-token"]');
        const csrfToken = metaToken?.getAttribute('content');
        
        if (!csrfToken) {
            console.error('Token CSRF non trovato');
            showToast('Errore di sicurezza. Ricarica la pagina.', true);
            return;
        }
        
        // Stato di caricamento
        const originalContent = btn.innerHTML;
        btn.disabled = true;
        btn.textContent = 'In corso...';
        
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: `action=add&productId=${productId}&csrfToken=${encodeURIComponent(csrfToken)}`
            });

            const data = await response.json();

            // Rotazione del Token CSRF
            if (data.csrfToken) {
                metaToken.setAttribute('content', data.csrfToken);
            }

            

            if (data.success) {
            // Aggiornamento contatori header
            if (url === 'cart') {
                document.getElementById('cart-count').textContent = data.cartItemCount;
            } else if (url === 'wishlist') {
                document.getElementById('wishlist-count').textContent = data.wishlistItemCount;
            }
            showToast(data.addedProductName ? `'${data.addedProductName}' aggiunto!` : 'Operazione completata!');
        } else {
            // Mostra l'errore dal backend (es. "Quantità non disponibile in magazzino")
            showToast(data.error || 'Operazione non riuscita', true);
        }

        if (!response.ok) {
                throw new Error(data.error || `Errore: ${response.status}`);
            }
            
        } catch (error) {
            console.error('Errore AJAX:', error);
            showToast(error.message, true);
        } finally {
            // Ripristino bottone
            btn.disabled = false;
            btn.innerHTML = originalContent;
        }
    }

    // Listener Aggiungi al carrello
    if (addToCartBtn) {
        addToCartBtn.addEventListener('click', () => {
            handleAjaxAction('cart', addToCartBtn.dataset.productId, addToCartBtn);
        });
    }
    
    // Listener Aggiungi alla wishlist
    if (addToWishlistBtn) {
        addToWishlistBtn.addEventListener('click', () => {
            handleAjaxAction('wishlist', addToWishlistBtn.dataset.productId, addToWishlistBtn);
        });
    }
    
    // --- Contatore caratteri textarea recensione ---
    const reviewTextarea = document.getElementById('reviewText');
    const charCount = document.querySelector('.char-count');
    
    if (reviewTextarea && charCount) {
        reviewTextarea.addEventListener('input', () => {
            const length = reviewTextarea.value.length;
            charCount.textContent = `${length}/1000`;
            
            // Feedback visivo quando ci si avvicina al limite
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
            // Verifica che sia stato selezionato un voto (rating)
            const ratingSelected = reviewForm.querySelector('input[name="voto"]:checked');
            
            if (!ratingSelected) {
                e.preventDefault();
                showToast('Per favore, seleziona una valutazione (stelle).', true);
            }
        });
    }
    
});

/**
 * Mostra notifiche temporanee all'utente
 */
function showToast(message, isError = false) {
    const toast = document.createElement('div');
    toast.className = `toast ${isError ? 'error' : ''}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    
    // Animazione di entrata (necessita di CSS)
    setTimeout(() => toast.classList.add('show'), 10);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}