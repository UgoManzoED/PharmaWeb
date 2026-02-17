document.addEventListener('DOMContentLoaded', () => {
    
    // --- Gestione ordinamento prodotti ---
    const sortSelect = document.getElementById('sort-select');
    const productsGrid = document.getElementById('products-grid');
    
    if (sortSelect && productsGrid) {
        sortSelect.addEventListener('change', () => {
            const sortValue = sortSelect.value;
            const products = Array.from(productsGrid.children);
            
            // Ordina i prodotti in base alla selezione
            products.sort((a, b) => {
                const priceA = parseFloat(a.dataset.price) || 0;
                const priceB = parseFloat(b.dataset.price) || 0;
                const nameA = a.dataset.name || "";
                const nameB = b.dataset.name || "";

                switch(sortValue) {
                    case 'price-asc':
                        return priceA - priceB;
                    case 'price-desc':
                        return priceB - priceA;
                    case 'name-asc':
                        return nameA.localeCompare(nameB);
                    case 'name-desc':
                        return nameB.localeCompare(nameA);
                    default:
                        return parseInt(a.dataset.id) - parseInt(b.dataset.id);
                }
            });
            
            // Riordina il DOM utilizzando un DocumentFragment per ottimizzare le performance
            const fragment = document.createDocumentFragment();
            products.forEach(product => fragment.appendChild(product));
            productsGrid.appendChild(fragment);
        });
    }
    
    // --- Gestione filtri ---
    const filterDiscount = document.getElementById('filter-discount');
    const filterAvailable = document.getElementById('filter-available');
    
    function applyFilters() {
        const products = document.querySelectorAll('.product-card');
        let visibleCount = 0;
        
        products.forEach(product => {
            let show = true;
            
            // Filtro sconto
            if (filterDiscount && filterDiscount.checked) {
                const discount = parseInt(product.dataset.discount) || 0;
                if (discount <= 0) {
                    show = false;
                }
            }
            
            // Filtro disponibilità
            if (filterAvailable && filterAvailable.checked) {
                const available = parseInt(product.dataset.available) || 0;
                if (available <= 0) {
                    show = false;
                }
            }
            
            // Mostra/nascondi prodotto tramite classi CSS
            if (show) {
                product.classList.remove('hidden');
                product.classList.add('show');
                visibleCount++;
            } else {
                product.classList.add('hidden');
                product.classList.remove('show');
            }
        });
        
        // Aggiorna contatore risultati
        const resultsCount = document.querySelector('.results-count');
        if (resultsCount) {
            resultsCount.textContent = `${visibleCount} prodotto/i trovato/i`;
        }
    }
    
    // Aggiungi event listeners ai filtri
    if (filterDiscount) filterDiscount.addEventListener('change', applyFilters);
    if (filterAvailable) filterAvailable.addEventListener('change', applyFilters);
    
    // --- Smooth scroll per categorie ---
    const categoryLinks = document.querySelectorAll('.category-link');
    categoryLinks.forEach(link => {
        link.addEventListener('click', () => {
            // Feedback visivo rapido al click
            link.style.transform = 'scale(0.98)';
            setTimeout(() => {
                link.style.transform = 'scale(1)';
            }, 100);
        });
    });


    // Funzione Helper per gestire le chiamate AJAX
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

            
            if (data.success) {
                console.log('Prodotto aggiunto:', data.addedProductName);
                successCallback(data);
            } else {              
                showToast(data.error || 'Operazione non riuscita', true);
            }

        } catch (error) {
            console.error('Errore nella chiamata AJAX:', error);
            showToast('Errore di connessione. Riprova.', true);
        }
    }

    // --- Logica AJAX per aggiungere al carrello ---
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

    // --- Logica AJAX per aggiungere alla wishlist ---
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