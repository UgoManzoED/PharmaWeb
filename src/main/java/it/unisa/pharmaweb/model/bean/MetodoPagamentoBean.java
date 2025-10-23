package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class MetodoPagamentoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idMetodo;
    private String tipoCarta; // Es. 'Visa', 'Mastercard', 'Paypal'
    private String titolare;
    private String ultime4Cifre;
    private int meseScadenza;
    private int annoScadenza;
    private int idUtente;

    public MetodoPagamentoBean() {
    }

    public int getIdMetodo() {
        return idMetodo;
    }

    public void setIdMetodo(int idMetodo) {
        this.idMetodo = idMetodo;
    }

    public String getTipoCarta() {
        return tipoCarta;
    }

    public void setTipoCarta(String tipoCarta) {
        this.tipoCarta = tipoCarta;
    }

    public String getTitolare() {
        return titolare;
    }

    public void setTitolare(String titolare) {
        this.titolare = titolare;
    }

    public String getUltime4Cifre() {
        return ultime4Cifre;
    }

    public void setUltime4Cifre(String ultime4Cifre) {
        this.ultime4Cifre = ultime4Cifre;
    }

    public int getMeseScadenza() {
        return meseScadenza;
    }

    public void setMeseScadenza(int meseScadenza) {
        this.meseScadenza = meseScadenza;
    }

    public int getAnnoScadenza() {
        return annoScadenza;
    }

    public void setAnnoScadenza(int annoScadenza) {
        this.annoScadenza = annoScadenza;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
}
