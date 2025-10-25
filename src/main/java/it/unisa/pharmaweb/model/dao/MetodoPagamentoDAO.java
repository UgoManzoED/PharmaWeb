package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.MetodoPagamentoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagamentoDAO {

    /**
     * Recupera tutti i metodi di pagamento associati a un utente.
     * @param idUtente l'ID dell'utente.
     * @return una List di MetodoPagamentoBean.
     */
    public List<MetodoPagamentoBean> getAllByUserId(int idUtente) {
        List<MetodoPagamentoBean> pagamenti = new ArrayList<>();
        String sql = "SELECT * FROM MetodoPagamento WHERE FK_Utente = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MetodoPagamentoBean pagamento = new MetodoPagamentoBean();
                pagamento.setIdMetodo(rs.getInt("ID_Metodo"));
                pagamento.setTipoCarta(rs.getString("TipoCarta"));
                pagamento.setTitolare(rs.getString("Titolare"));
                pagamento.setUltime4Cifre(rs.getString("Ultime4Cifre"));
                pagamento.setMeseScadenza(rs.getInt("MeseScadenza"));
                pagamento.setAnnoScadenza(rs.getInt("AnnoScadenza"));
                pagamento.setIdUtente(rs.getInt("FK_Utente"));
                pagamenti.add(pagamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pagamenti;
    }

    /**
     * Recupera un singolo metodo di pagamento tramite il suo ID.
     * @param idMetodo l'ID del metodo da cercare.
     * @return un MetodoPagamentoBean se trovato, altrimenti null.
     */
    public MetodoPagamentoBean getById(int idMetodo) {
        String sql = "SELECT * FROM MetodoPagamento WHERE ID_Metodo = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idMetodo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MetodoPagamentoBean pagamento = new MetodoPagamentoBean();
                pagamento.setIdMetodo(rs.getInt("ID_Metodo"));
                pagamento.setTipoCarta(rs.getString("TipoCarta"));
                pagamento.setTitolare(rs.getString("Titolare"));
                pagamento.setUltime4Cifre(rs.getString("Ultime4Cifre"));
                pagamento.setMeseScadenza(rs.getInt("MeseScadenza"));
                pagamento.setAnnoScadenza(rs.getInt("AnnoScadenza"));
                pagamento.setIdUtente(rs.getInt("FK_Utente"));
                return pagamento;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Salva un nuovo metodo di pagamento nel database.
     * @param pagamento il MetodoPagamentoBean da salvare.
     */
    public void save(MetodoPagamentoBean pagamento) {
        String sql = "INSERT INTO MetodoPagamento (TipoCarta, Titolare, Ultime4Cifre, MeseScadenza, AnnoScadenza, FK_Utente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pagamento.getTipoCarta());
            stmt.setString(2, pagamento.getTitolare());
            stmt.setString(3, pagamento.getUltime4Cifre());
            stmt.setInt(4, pagamento.getMeseScadenza());
            stmt.setInt(5, pagamento.getAnnoScadenza());
            stmt.setInt(6, pagamento.getIdUtente());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Rimuove un metodo di pagamento dal database.
     * @param idMetodo l'ID del metodo da eliminare.
     */
    public void delete(int idMetodo) {
        String sql = "DELETE FROM MetodoPagamento WHERE ID_Metodo = ?";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMetodo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}