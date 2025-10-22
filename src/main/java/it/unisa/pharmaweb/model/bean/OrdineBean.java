package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrdineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdine;
    private int idUtente;
    private Date dataOrdine;
    private double importoTotale;
    private String indirizzoSpedizione;
    private String metodoPagamentoUtilizzato;
    private String stato;
    private int puntiGuadagnati;
    private int puntiUtilizzati;
    private List<RigaOrdineBean> righe;
    
    public OrdineBean() {
    	
    }
    
	public OrdineBean(int idOrdine, int idUtente, Date dataOrdine, double importoTotale, String indirizzoSpedizione,
			String metodoPagamentoUtilizzato, String stato, int puntiGuadagnati, int puntiUtilizzati,
			List<RigaOrdineBean> righe) {
		this.idOrdine = idOrdine;
		this.idUtente = idUtente;
		this.dataOrdine = dataOrdine;
		this.importoTotale = importoTotale;
		this.indirizzoSpedizione = indirizzoSpedizione;
		this.metodoPagamentoUtilizzato = metodoPagamentoUtilizzato;
		this.stato = stato;
		this.puntiGuadagnati = puntiGuadagnati;
		this.puntiUtilizzati = puntiUtilizzati;
		this.righe = righe;
	}

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public double getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(double importoTotale) {
		this.importoTotale = importoTotale;
	}

	public String getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(String indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}

	public String getMetodoPagamentoUtilizzato() {
		return metodoPagamentoUtilizzato;
	}

	public void setMetodoPagamentoUtilizzato(String metodoPagamentoUtilizzato) {
		this.metodoPagamentoUtilizzato = metodoPagamentoUtilizzato;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public int getPuntiGuadagnati() {
		return puntiGuadagnati;
	}

	public void setPuntiGuadagnati(int puntiGuadagnati) {
		this.puntiGuadagnati = puntiGuadagnati;
	}

	public int getPuntiUtilizzati() {
		return puntiUtilizzati;
	}

	public void setPuntiUtilizzati(int puntiUtilizzati) {
		this.puntiUtilizzati = puntiUtilizzati;
	}

	public List<RigaOrdineBean> getRighe() {
		return righe;
	}

	public void setRighe(List<RigaOrdineBean> righe) {
		this.righe = righe;
	}
    
}