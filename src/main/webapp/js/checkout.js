document.addEventListener('DOMContentLoaded', () => {
    
    // --- Setup per la formattazione valuta (Italiano) ---
    const currencyFormatter = new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    });

    // Recupera il totale carrello dai DATA ATTRIBUTE
    const subtotalElement = document.getElementById('subtotal');
    // Se l'elemento non esiste (es. carrello vuoto o errore), default a 0
    const cartTotal = subtotalElement ? parseFloat(subtotalElement.dataset.value) : 0;
    
    // Recupera punti massimi
    const userPointsElement = document.getElementById('user-points-display');
    const maxPoints = userPointsElement ? parseInt(userPointsElement.textContent) : 0;
    
    // --- Gestione Punti Fedeltà ---
    const puntiInput = document.getElementById('puntiDaUsare');
    const useAllPointsBtn = document.getElementById('useAllPoints');
    const pointsDiscountText = document.getElementById('pointsDiscount');
    const pointsDiscountRow = document.getElementById('pointsDiscountRow');
    const pointsDiscountAmount = document.getElementById('pointsDiscountAmount');
    const finalTotalElement = document.getElementById('finalTotal');
    
    function updateTotals() {
        if (!puntiInput) return;

        const puntiUsati = parseInt(puntiInput.value) || 0;
        
        // Sconto non può superare il totale
        const sconto = Math.min(puntiUsati, cartTotal); 
        const finalTotal = Math.max(cartTotal - sconto, 0);
        
        // Aggiorna visualizzazione sconto sotto l'input
        if (pointsDiscountText) {
            pointsDiscountText.textContent = `Sconto: ${currencyFormatter.format(sconto)}`;
        }
        
        // Mostra/nascondi riga sconto nel riepilogo a destra
        if (pointsDiscountRow && pointsDiscountAmount) {
            if (puntiUsati > 0) {
                pointsDiscountRow.style.display = 'flex';
                // Mostriamo il meno davanti per chiarezza
                pointsDiscountAmount.textContent = `-${currencyFormatter.format(sconto)}`;
            } else {
                pointsDiscountRow.style.display = 'none';
            }
        }
        
        // Aggiorna totale finale
        if (finalTotalElement) {
            finalTotalElement.textContent = currencyFormatter.format(finalTotal);
        }
    }
    
    // Event listener per input punti (digitazione manuale)
    if (puntiInput) {
        puntiInput.addEventListener('input', () => {
            let value = parseInt(puntiInput.value);
            
            if (isNaN(value)) value = 0;
            
            // Validazione
            if (value < 0) value = 0;
            if (value > maxPoints) value = maxPoints;
            // Non permettiamo di usare più punti del valore in euro del carrello
            if (value > cartTotal) value = Math.floor(cartTotal); 
            
            puntiInput.value = value; // Aggiorna l'input con il valore validato
            updateTotals();
        });
    }
    
    // Bottone "Usa tutti"
    if (useAllPointsBtn && puntiInput) {
        useAllPointsBtn.addEventListener('click', () => {
            // Usa il massimo possibile: o tutti i punti, o il totale del carrello
            const maxUsablePoints = Math.min(maxPoints, Math.floor(cartTotal));
            puntiInput.value = maxUsablePoints;
            updateTotals();
        });
    }
    
    // Inizializza totali al caricamento della pagina
    updateTotals();
    
    // --- Validazione Form Checkout ---
    const checkoutForm = document.getElementById('checkoutForm');
    const confirmOrderBtn = document.getElementById('confirmOrderBtn');
    
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', (e) => {
            // 1. Verifica indirizzo selezionato
            const indirizzoSelected = checkoutForm.querySelector('input[name="indirizzoId"]:checked');
            if (!indirizzoSelected) {
                e.preventDefault();
                alert('Per favore, seleziona un indirizzo di spedizione.');
                // Scroll per mostrare la sezione indirizzi
                document.querySelector('.checkout-section').scrollIntoView({behavior: 'smooth'});
                return false;
            }
            
            // 2. Verifica metodo pagamento selezionato
            const pagamentoSelected = checkoutForm.querySelector('input[name="pagamentoId"]:checked');
            if (!pagamentoSelected) {
                e.preventDefault();
                alert('Per favore, seleziona un metodo di pagamento.');
                return false;
            }
            
            // 3. Disabilita il bottone per evitare doppi invii (double submit protection)
            if (confirmOrderBtn) {
                confirmOrderBtn.disabled = true;
                confirmOrderBtn.textContent = 'Elaborazione in corso...';
                confirmOrderBtn.classList.add('processing'); // Utile per styling CSS
            }
            
            // Permetti l'invio del form
            return true;
        });
    }
    
    // --- Animazione selezione card (Indirizzi e Pagamenti) ---
    // Aggiunge un effetto visivo quando si seleziona un radio button
    const radioInputs = document.querySelectorAll('.address-card input[type="radio"], .payment-card input[type="radio"]');
    
    radioInputs.forEach(radio => {
        radio.addEventListener('change', function() {
            // Trova il container genitore (lista indirizzi o pagamenti)
            const container = this.closest('.address-list, .payment-list');
            
            if (container) {
                // Rimuovi classe 'selected' da tutte le card nel gruppo
                container.querySelectorAll('label').forEach(label => {
                    label.classList.remove('selected-card');
                    label.style.transform = 'scale(1)';
                    label.style.borderColor = '#ddd'; // Colore default
                });
                
                // Aggiungi stile alla card selezionata
                const selectedLabel = this.closest('label');
                if (selectedLabel) {
                    selectedLabel.classList.add('selected-card');
                    selectedLabel.style.transform = 'scale(1.02)';
                    selectedLabel.style.borderColor = '#28a745'; // Verde PharmaWeb
                    
                    // Reset dell'animazione dopo un po'
                    setTimeout(() => {
                        selectedLabel.style.transform = 'scale(1)';
                    }, 200);
                }
            }
        });
    });
    
});