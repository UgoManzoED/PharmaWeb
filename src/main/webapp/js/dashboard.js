document.addEventListener('DOMContentLoaded', () => {
    const navLinks = document.querySelectorAll('.nav-link');
    const tabs = document.querySelectorAll('.tab-content');

    function switchTab(tabId) {
        // Rimuovi active da tutti i link e tab
        navLinks.forEach(l => l.classList.remove('active'));
        tabs.forEach(t => t.classList.remove('active'));

        // Aggiungi active al link cliccato e al tab corrispondente
        document.querySelector(`[data-tab="${tabId}"]`).classList.add('active');
        document.getElementById(`tab-${tabId}`).classList.add('active');
    }

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const tabId = link.getAttribute('data-tab');
            switchTab(tabId);
            // Aggiorna l'URL senza ricaricare
            window.location.hash = tabId;
        });
    });

    // Gestione dell'anchor all'avvio
    const currentHash = window.location.hash.substring(1);
    if (currentHash && document.getElementById(`tab-${currentHash}`)) {
        switchTab(currentHash);
    }
});