package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

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
    
    /**
     * Salva un nuovo utente nel database, eseguendo l'hashing della password.
     * @param utente l'UtenteBean con i dati da salvare (la password deve essere in chiaro).
     */
    public void saveUtente(UtenteBean utente) {
        String hashedPassword = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt()); // 1. Genera il salt e calcola l'hash della password in chiaro
        
        String sql = "INSERT INTO Utente (Email, Password, Nome, Cognome, Ruolo) VALUES (?, ?, ?, ?, 'cliente')";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getEmail());
            stmt.setString(2, hashedPassword); // 2. Salvo l'hash della password (Non la password in chiaro)
            stmt.setString(3, utente.getNome());
            stmt.setString(4, utente.getCognome());
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
