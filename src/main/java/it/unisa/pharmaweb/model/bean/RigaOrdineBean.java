package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

/**
 * Questa classe rappresenta una singola riga di un ordine.
 * Contiene sia il riferimento al prodotto originale (se esiste ancora),
 * sia i dati "congelati" (Snapshot) al momento dell'acquisto per lo storico.
 */
public class RigaOrdineBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Chiavi e collegamenti
    private int idOrdine;
    private int idProdotto;
    
    // Riferimento all'oggetto prodotto completo
    private ProductBean prodotto; 

    // --- DATI SNAPSHOT ---
    private int quantita;
    private double prezzoAcquisto;       // Il prezzo pagato in quel momento
    private String nomeProdottoSnapshot; // Il nome del prodotto in quel momento
    private int ivaApplicata;            // L'IVA applicata in quel momento (es. 22)

    // Costruttore vuoto
    public RigaOrdineBean() {
    }

    /**
     * Costruttore di comodità per creare una riga ordine durante il checkout.
     * Popola automaticamente i dati snapshot dal ProductBean.
     */
    public RigaOrdineBean(ProductBean prodotto, int quantita, double prezzoAcquisto) {
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.prezzoAcquisto = prezzoAcquisto;
        
        // Logica Snapshot
        if (prodotto != null) {
            this.idProdotto = prodotto.getIdProdotto();
            this.nomeProdottoSnapshot = prodotto.getNomeProdotto(); // Congeliamo il nome
        }
        
        this.ivaApplicata = 22; // Default IVA
    }
    
    // --- Getter e Setter ---

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public ProductBean getProdotto() {
        return prodotto;
    }

    public void setProdotto(ProductBean prodotto) {
        this.prodotto = prodotto;
        if (prodotto != null) {
            this.idProdotto = prodotto.getIdProdotto();
        }
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(double prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }

    public String getNomeProdottoSnapshot() {
        return nomeProdottoSnapshot;
    }

    public void setNomeProdottoSnapshot(String nomeProdottoSnapshot) {
        this.nomeProdottoSnapshot = nomeProdottoSnapshot;
    }

    public int getIvaApplicata() {
        return ivaApplicata;
    }

    public void setIvaApplicata(int ivaApplicata) {
        this.ivaApplicata = ivaApplicata;
    }
    
    public double getPrezzoAlMomentoDellAcquisto() {
        return this.prezzoAcquisto;
    }
}