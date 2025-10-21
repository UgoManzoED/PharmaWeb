package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class RigaOrdineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private ProductBean prodotto;
    private int quantita;
    private double prezzoAlMomentoDellAcquisto;

    public RigaOrdineBean(ProductBean prodotto, int quantita, double prezzoAlMomentoDellAcquisto) {
        this.prodotto = prodotto;
        this.quantita = quantita;
        this.prezzoAlMomentoDellAcquisto = prezzoAlMomentoDellAcquisto;
    }

	public ProductBean getProdotto() {
		return prodotto;
	}

	public void setProdotto(ProductBean prodotto) {
		this.prodotto = prodotto;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public double getPrezzoAlMomentoDellAcquisto() {
		return prezzoAlMomentoDellAcquisto;
	}

	public void setPrezzoAlMomentoDellAcquisto(double prezzoAlMomentoDellAcquisto) {
		this.prezzoAlMomentoDellAcquisto = prezzoAlMomentoDellAcquisto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}