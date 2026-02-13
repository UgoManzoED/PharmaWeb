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
                        return 0;
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
    
});
