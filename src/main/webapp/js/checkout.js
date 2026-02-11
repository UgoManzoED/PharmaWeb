document.addEventListener('DOMContentLoaded', () => {
    
    // Recupera il totale carrello dal DOM
    const cartTotal = parseFloat(document.getElementById('subtotal')?.textContent.replace('€', '').replace(',', '.').trim()) || 0;
    const maxPoints = parseInt(document.querySelector('.points-available strong')?.textContent) || 0;
    
    // --- Gestione Punti Fedeltà ---
    const puntiInput = document.getElementById('puntiDaUsare');
    const useAllPointsBtn = document.getElementById('useAllPoints');
    const pointsDiscountText = document.getElementById('pointsDiscount');
    const pointsDiscountRow = document.getElementById('pointsDiscountRow');
    const pointsDiscountAmount = document.getElementById('pointsDiscountAmount');
    const finalTotalElement = document.getElementById('finalTotal');
    
    function updateTotals() {
        const puntiUsati = parseInt(puntiInput?.value) || 0;
        const sconto = Math.min(puntiUsati, cartTotal); // Sconto max = totale carrello
        const finalTotal = Math.max(cartTotal - sconto, 0);
        
        // Aggiorna visualizzazione sconto
        if (pointsDiscountText) {
            pointsDiscountText.textContent = `Sconto: ${sconto.toFixed(2)}€`;
        }
        
        // Mostra/nascondi riga sconto nel riepilogo
        if (pointsDiscountRow && pointsDiscountAmount) {
            if (puntiUsati > 0) {
                pointsDiscountRow.style.display = 'flex';
                pointsDiscountAmount.textContent = `-${sconto.toFixed(2)}€`;
            } else {
                pointsDiscountRow.style.display = 'none';
            }
        }
        
        // Aggiorna totale finale
        if (finalTotalElement) {
            finalTotalElement.textContent = `${finalTotal.toFixed(2)}€`;
        }
    }
    
    // Event listener per input punti
    if (puntiInput) {
        puntiInput.addEventListener('input', () => {
            let value = parseInt(puntiInput.value) || 0;
            
            // Validazione
            if (value < 0) value = 0;
            if (value > maxPoints) value = maxPoints;
            if (value > cartTotal) value = Math.floor(cartTotal); // Non può scontare più del totale
            
            puntiInput.value = value;
            updateTotals();
        });
    }
    
    // Bottone "Usa tutti"
    if (useAllPointsBtn && puntiInput) {
        useAllPointsBtn.addEventListener('click', () => {
            const maxUsablePoints = Math.min(maxPoints, Math.floor(cartTotal));
            puntiInput.value = maxUsablePoints;
            updateTotals();
        });
    }
    
    // Inizializza totali al caricamento
    updateTotals();
    
    // --- Validazione Form ---
    const checkoutForm = document.getElementById('checkoutForm');
    const confirmOrderBtn = document.getElementById('confirmOrderBtn');
    
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', (e) => {
            // Verifica indirizzo selezionato
            const indirizzoSelected = checkoutForm.querySelector('input[name="indirizzoId"]:checked');
            if (!indirizzoSelected) {
                e.preventDefault();
                alert('Per favore, seleziona un indirizzo di spedizione.');
                return false;
            }
            
            // Verifica metodo pagamento selezionato
            const pagamentoSelected = checkoutForm.querySelector('input[name="pagamentoId"]:checked');
            if (!pagamentoSelected) {
                e.preventDefault();
                alert('Per favore, seleziona un metodo di pagamento.');
                return false;
            }
            
            // Disabilita il bottone per evitare doppi invii
            if (confirmOrderBtn) {
                confirmOrderBtn.disabled = true;
                confirmOrderBtn.textContent = 'Elaborazione in corso...';
            }
            
            // Permetti l'invio del form
            return true;
        });
    }
    
    // --- Animazione selezione carte ---
    const radioInputs = document.querySelectorAll('.address-card input[type="radio"], .payment-card input[type="radio"]');
    
    radioInputs.forEach(radio => {
        radio.addEventListener('change', function() {
            // Rimuovi selezione visiva da tutti
            const container = this.closest('.address-list, .payment-list');
            if (container) {
                container.querySelectorAll('label').forEach(label => {
                    label.style.transform = 'scale(1)';
                });
                
                // Aggiungi effetto alla card selezionata
                const selectedLabel = this.closest('label');
                if (selectedLabel) {
                    selectedLabel.style.transform = 'scale(1.02)';
                    setTimeout(() => {
                        selectedLabel.style.transform = 'scale(1)';
                    }, 200);
                }
            }
        });
    });
    
});

// Funzioni globali per toggle form inline
function toggleAddressForm() {
    const form = document.getElementById('addAddressForm');
    const overlay = document.getElementById('formOverlay');
    const paymentForm = document.getElementById('addPaymentForm');
    const isHidden = form.style.display === 'none' || form.style.display === '';
    
    if (isHidden) {
        // Aggiorna il token CSRF prima di aprire il form
        updateCsrfToken('addressForm');
        form.style.display = 'block';
        overlay.style.display = 'block';
        paymentForm.style.display = 'none';
    } else {
        form.style.display = 'none';
        overlay.style.display = 'none';
        document.getElementById('addressForm').reset();
    }
}

function togglePaymentForm() {
    const form = document.getElementById('addPaymentForm');
    const overlay = document.getElementById('formOverlay');
    const addressForm = document.getElementById('addAddressForm');
    const isHidden = form.style.display === 'none' || form.style.display === '';
    
    if (isHidden) {
        // Aggiorna il token CSRF prima di aprire il form
        updateCsrfToken('paymentForm');
        form.style.display = 'block';
        overlay.style.display = 'block';
        addressForm.style.display = 'none';
    } else {
        form.style.display = 'none';
        overlay.style.display = 'none';
        document.getElementById('paymentForm').reset();
    }
}

// Funzione per aggiornare il token CSRF
function updateCsrfToken(formId) {
    // Prendi il token più aggiornato dal form principale del checkout
    const checkoutForm = document.getElementById('checkoutForm');
    const currentToken = checkoutForm ? checkoutForm.querySelector('input[name="csrfToken"]').value : '';
    
    // Aggiorna il token nel form popup
    const popupForm = document.getElementById(formId);
    const tokenInput = popupForm.querySelector('input[name="csrfToken"]');
    if (tokenInput && currentToken) {
        tokenInput.value = currentToken;
    }
}
