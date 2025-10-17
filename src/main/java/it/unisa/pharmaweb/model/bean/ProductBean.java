package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class ProductBean implements Serializable{
		private static final long serialVersionUID = 1L;

	    // Corrispondono alle colonne della VistaCatalogoProdotti
	    private int idProdotto;
	    private String nomeProdotto;
	    private String descrizione;
	    private double prezzoDiListino;
	    private int scontoPercentuale;
	    private double prezzoFinale;
	    private int quantitaDisponibile;
	    private String urlImmagine;
	    private int idCategoria;
	    private String nomeCategoria;

	    // --- Costruttore vuoto ---
	    
	    public ProductBean() {
	        // Costruttore vuoto
	    }

	    // --- Metodi Getter e Setter ---

	    public int getIdProdotto() {
	        return idProdotto;
	    }

	    public void setIdProdotto(int idProdotto) {
	        this.idProdotto = idProdotto;
	    }

	    public String getNomeProdotto() {
	        return nomeProdotto;
	    }

	    public void setNomeProdotto(String nomeProdotto) {
	        this.nomeProdotto = nomeProdotto;
	    }

	    public String getDescrizione() {
	        return descrizione;
	    }

	    public void setDescrizione(String descrizione) {
	        this.descrizione = descrizione;
	    }

	    public double getPrezzoDiListino() {
	        return prezzoDiListino;
	    }

	    public void setPrezzoDiListino(double prezzoDiListino) {
	        this.prezzoDiListino = prezzoDiListino;
	    }

	    public int getScontoPercentuale() {
	        return scontoPercentuale;
	    }

	    public void setScontoPercentuale(int scontoPercentuale) {
	        this.scontoPercentuale = scontoPercentuale;
	    }

	    public double getPrezzoFinale() {
	        return prezzoFinale;
	    }

	    public void setPrezzoFinale(double prezzoFinale) {
	        this.prezzoFinale = prezzoFinale;
	    }

	    public int getQuantitaDisponibile() {
	        return quantitaDisponibile;
	    }

	    public void setQuantitaDisponibile(int quantitaDisponibile) {
	        this.quantitaDisponibile = quantitaDisponibile;
	    }

	    public String getUrlImmagine() {
	        return urlImmagine;
	    }

	    public void setUrlImmagine(String urlImmagine) {
	        this.urlImmagine = urlImmagine;
	    }

	    public int getIdCategoria() {
	        return idCategoria;
	    }

	    public void setIdCategoria(int idCategoria) {
	        this.idCategoria = idCategoria;
	    }

	    public String getNomeCategoria() {
	        return nomeCategoria;
	    }

	    public void setNomeCategoria(String nomeCategoria) {
	        this.nomeCategoria = nomeCategoria;
	    }
}