package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.IndirizzoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndirizzoDAO {

    /**
     * Recupera tutti gli indirizzi di spedizione associati a un utente.
     * @param idUtente l'ID dell'utente.
     * @return una List di IndirizzoBean.
     */
    public List<IndirizzoBean> getAllByUserId(int idUtente) {
        List<IndirizzoBean> indirizzi = new ArrayList<>();
        String sql = "SELECT * FROM IndirizzoSpedizione WHERE FK_Utente = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                IndirizzoBean indirizzo = new IndirizzoBean();
                indirizzo.setIdIndirizzo(rs.getInt("ID_Indirizzo"));
                indirizzo.setNomeDestinatario(rs.getString("NomeDestinatario"));
                indirizzo.setVia(rs.getString("Via"));
                indirizzo.setCitta(rs.getString("Citta"));
                indirizzo.setCap(rs.getString("CAP"));
                indirizzo.setProvincia(rs.getString("Provincia"));
                indirizzo.setIdUtente(rs.getInt("FK_Utente"));
                indirizzi.add(indirizzo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indirizzi;
    }

    /**
     * Recupera un singolo indirizzo tramite il suo ID.
     * @param idIndirizzo l'ID dell'indirizzo da cercare.
     * @return un IndirizzoBean se trovato, altrimenti null.
     */
    public IndirizzoBean getById(int idIndirizzo) {
        String sql = "SELECT * FROM IndirizzoSpedizione WHERE ID_Indirizzo = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idIndirizzo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                IndirizzoBean indirizzo = new IndirizzoBean();
                indirizzo.setIdIndirizzo(rs.getInt("ID_Indirizzo"));
                indirizzo.setNomeDestinatario(rs.getString("NomeDestinatario"));
                indirizzo.setVia(rs.getString("Via"));
                indirizzo.setCitta(rs.getString("Citta"));
                indirizzo.setCap(rs.getString("CAP"));
                indirizzo.setProvincia(rs.getString("Provincia"));
                indirizzo.setIdUtente(rs.getInt("FK_Utente"));
                return indirizzo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Salva un nuovo indirizzo nel database.
     * @param indirizzo l'IndirizzoBean da salvare.
     */
    public void save(IndirizzoBean indirizzo) {
        String sql = "INSERT INTO IndirizzoSpedizione (NomeDestinatario, Via, Citta, CAP, Provincia, FK_Utente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, indirizzo.getNomeDestinatario());
            stmt.setString(2, indirizzo.getVia());
            stmt.setString(3, indirizzo.getCitta());
            stmt.setString(4, indirizzo.getCap());
            stmt.setString(5, indirizzo.getProvincia());
            stmt.setInt(6, indirizzo.getIdUtente());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimuove un indirizzo dal database.
     * @param idIndirizzo l'ID dell'indirizzo da eliminare.
     */
    public void delete(int idIndirizzo) {
        String sql = "DELETE FROM IndirizzoSpedizione WHERE ID_Indirizzo = ?";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idIndirizzo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}