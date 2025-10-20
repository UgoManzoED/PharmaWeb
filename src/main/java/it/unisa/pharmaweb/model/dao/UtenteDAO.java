package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
	/**
     * Recupera un utente dal database tramite la sua email.
     * Utilizzato principalmente per la procedura di login.
     * @param email l'email dell'utente da cercare.
     * @return un UtenteBean se l'utente è stato trovato, altrimenti null.
     */
    public UtenteBean getUtenteByEmail(String email) {
        String sql = "SELECT * FROM Utente WHERE Email = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                UtenteBean utente = new UtenteBean();
                utente.setIdUtente(rs.getInt("ID_Utente"));
                utente.setEmail(rs.getString("Email"));
                utente.setPassword(rs.getString("Password")); // Recuperiamo l'hash della password
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setRuolo(rs.getString("Ruolo"));
                utente.setPuntiFedelta(rs.getInt("PuntiFedelta"));
                return utente;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Utente non trovato
    }
}
