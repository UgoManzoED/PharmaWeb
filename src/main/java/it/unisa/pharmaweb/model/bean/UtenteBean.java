package it.unisa.pharmaweb.model.bean;

import java.io.Serializable;

public class UtenteBean implements Serializable {
	 	private static final long serialVersionUID = 1L;

	    private int idUtente;
	    private String email;
	    private String password;
	    private String nome;
	    private String cognome;
	    private String ruolo; // Sarà 'cliente' o 'admin'
	    private int puntiFedelta;

	    // --- Costruttore vuoto ---
	    public UtenteBean() {
	    	
	    }

	    public int getIdUtente() {
	        return idUtente;
	    }

	    public void setIdUtente(int idUtente) {
	        this.idUtente = idUtente;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getNome() {
	        return nome;
	    }

	    public void setNome(String nome) {
	        this.nome = nome;
	    }

	    public String getCognome() {
	        return cognome;
	    }

	    public void setCognome(String cognome) {
	        this.cognome = cognome;
	    }

	    public String getRuolo() {
	        return ruolo;
	    }

	    public void setRuolo(String ruolo) {
	        this.ruolo = ruolo;
	    }

	    public int getPuntiFedelta() {
	        return puntiFedelta;
	    }

	    public void setPuntiFedelta(int puntiFedelta) {
	        this.puntiFedelta = puntiFedelta;
	    }
}
