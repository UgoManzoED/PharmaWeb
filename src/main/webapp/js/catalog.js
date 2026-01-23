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
                switch(sortValue) {
                    case 'price-asc':
                        return parseFloat(a.dataset.price) - parseFloat(b.dataset.price);
                    case 'price-desc':
                        return parseFloat(b.dataset.price) - parseFloat(a.dataset.price);
                    case 'name-asc':
                        return a.dataset.name.localeCompare(b.dataset.name);
                    case 'name-desc':
                        return b.dataset.name.localeCompare(a.dataset.name);
                    default:
                        return 0;
                }
            });
            
            // Riordina il DOM
            products.forEach(product => productsGrid.appendChild(product));
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
                const discount = parseInt(product.dataset.discount);
                if (discount <= 0) {
                    show = false;
                }
            }
            
            // Filtro disponibilità
            if (filterAvailable && filterAvailable.checked) {
                const available = parseInt(product.dataset.available);
                if (available <= 0) {
                    show = false;
                }
            }
            
            // Mostra/nascondi prodotto
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
    if (filterDiscount) {
        filterDiscount.addEventListener('change', applyFilters);
    }
    
    if (filterAvailable) {
        filterAvailable.addEventListener('change', applyFilters);
    }
    
    // --- Smooth scroll per categorie ---
    const categoryLinks = document.querySelectorAll('.category-link');
    categoryLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            // Permettiamo la navigazione normale
            // ma aggiungiamo un piccolo feedback visivo
            link.style.transform = 'scale(0.98)';
            setTimeout(() => {
                link.style.transform = 'scale(1)';
            }, 100);
        });
    });
    
});
