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
     * Controlla se un'email è già presente nel database.
     * @param email l'email da controllare.
     * @return true se l'email esiste già, false altrimenti.
     */
    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Utente WHERE Email = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // In caso di errore, è più sicuro assumere che esista per evitare registrazioni duplicate.
        return true; 
    }
    
    /**
     * Aggiorna i dati anagrafici di un utente (nome, cognome).
     * Non modifica email, password o ruolo.
     * @param utente il bean dell'utente con i dati aggiornati. L'ID viene usato per identificare l'utente da modificare.
     */
    public void updateUtente(UtenteBean utente) {
        String sql = "UPDATE Utente SET Nome = ?, Cognome = ? WHERE ID_Utente = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setInt(3, utente.getIdUtente());
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Aggiorna la password di un utente, eseguendo l'hashing della nuova password.
     * @param idUtente l'ID dell'utente.
     * @param nuovaPasswordInChiaro la nuova password da salvare.
     */
    public void updatePassword(int idUtente, String nuovaPasswordInChiaro) {
        String hashedPassword = BCrypt.hashpw(nuovaPasswordInChiaro, BCrypt.gensalt());
        String sql = "UPDATE Utente SET Password = ? WHERE ID_Utente = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashedPassword);
            stmt.setInt(2, idUtente);
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Salva un nuovo utente nel database, eseguendo l'hashing della password.
     * @param utente l'UtenteBean con i dati da salvare (la password deve essere in chiaro).
     */
    public void saveUtente(UtenteBean utente) {
        String hashedPassword = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt()); // Genera il salt e calcola l'hash della password in chiaro
        
        String sql = "INSERT INTO Utente (Email, Password, Nome, Cognome, Ruolo) VALUES (?, ?, ?, ?, 'cliente')";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getEmail());
            stmt.setString(2, hashedPassword); // Salvo l'hash della password (Non la password in chiaro)
            stmt.setString(3, utente.getNome());
            stmt.setString(4, utente.getCognome());
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Recupera una lista di tutti gli utenti registrati nel sistema.
     * Utile per il pannello di amministrazione.
     * @return una List di UtenteBean.
     */
    public List<UtenteBean> getAllUtenti() {
        List<UtenteBean> utenti = new ArrayList<>();
        String sql = "SELECT * FROM Utente ORDER BY Cognome, Nome";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UtenteBean utente = new UtenteBean();
                utente.setIdUtente(rs.getInt("ID_Utente"));
                utente.setEmail(rs.getString("Email"));
                // NON recuperiamo la password per sicurezza
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));
                utente.setRuolo(rs.getString("Ruolo"));
                utente.setPuntiFedelta(rs.getInt("PuntiFedelta"));
                utenti.add(utente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    /**
     * Rimuove un utente dal database.
     * @param idUtente l'ID dell'utente da eliminare.
     */
    public void deleteUtente(int idUtente) {
        String sql = "DELETE FROM Utente WHERE ID_Utente = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtente);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
