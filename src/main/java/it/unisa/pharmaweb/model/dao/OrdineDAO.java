package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.bean.RigaOrdineBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {
    /**
     * Salva un ordine completo (testata e righe) nel database.
     * Questo metodo DEVE essere eseguito all'interno di una transazione.
     * @param conn la connessione transazionale attiva.
     * @param ordine l'oggetto OrdineBean da salvare.
     * @throws SQLException se si verifica un errore SQL.
     */
    public synchronized void saveOrdine(Connection conn, OrdineBean ordine) throws SQLException {
        // 1. Inserisci la testata dell'ordine e recupera l'ID generato
        String ordineSql = "INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmtOrdine = conn.prepareStatement(ordineSql, Statement.RETURN_GENERATED_KEYS)) {
            stmtOrdine.setDouble(1, ordine.getImportoTotale());
            stmtOrdine.setString(2, ordine.getIndirizzoSpedizione());
            stmtOrdine.setString(3, ordine.getMetodoPagamentoUtilizzato());
            stmtOrdine.setString(4, ordine.getStato());
            stmtOrdine.setInt(5, ordine.getPuntiGuadagnati());
            stmtOrdine.setInt(6, ordine.getPuntiUtilizzati());
            stmtOrdine.setInt(7, ordine.getIdUtente());
            stmtOrdine.executeUpdate();

            // Recupera l'ID dell'ordine appena creato
            try (ResultSet generatedKeys = stmtOrdine.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ordine.setIdOrdine(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creazione ordine fallita, nessun ID ottenuto.");
                }
            }
        }
        
        // 2. Inserisci le righe dell'ordine
        String rigaSql = "INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtRiga = conn.prepareStatement(rigaSql)) {
            for (RigaOrdineBean riga : ordine.getRighe()) {
                stmtRiga.setInt(1, ordine.getIdOrdine());
                stmtRiga.setInt(2, riga.getProdotto().getIdProdotto());
                stmtRiga.setInt(3, riga.getQuantita());
                stmtRiga.setDouble(4, riga.getPrezzoAlMomentoDellAcquisto());
                stmtRiga.addBatch();
            }
            stmtRiga.executeBatch();
        }
    }
    
    /**
     * Recupera tutti gli ordini effettuati da un utente specifico, ordinati dal più recente.
     * @param idUtente l'ID dell'utente di cui recuperare lo storico.
     * @return una List di OrdineBean.
     */
    public List<OrdineBean> getOrdiniByUtente(int idUtente) {
        List<OrdineBean> ordini = new ArrayList<>();
        // Usiamo la VistaRiepilogoOrdini che abbiamo già creato
        String sql = "SELECT * FROM VistaRiepilogoOrdini WHERE ID_Utente = ? ORDER BY DataOrdine DESC";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("ID_Ordine"));
                ordine.setDataOrdine(rs.getTimestamp("DataOrdine"));
                ordine.setImportoTotale(rs.getDouble("ImportoTotale"));
                ordine.setStato(rs.getString("Stato"));
                ordine.setIndirizzoSpedizione(rs.getString("IndirizzoSpedizione"));
                ordine.setMetodoPagamentoUtilizzato(rs.getString("MetodoPagamentoUtilizzato"));
                ordini.add(ordine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }
    
    /**
     * Recupera gli ordini filtrati per data e/o cliente.
     * Se un parametro è null, viene ignorato nel filtro.
     */
    public List<OrdineBean> getAllOrdini(String startDate, String endDate, String emailCliente) {
        List<OrdineBean> ordini = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM VistaRiepilogoOrdini WHERE 1=1");
        
        // Costruzione dinamica della query
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND DataOrdine >= ?");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND DataOrdine <= ?");
        }
        if (emailCliente != null && !emailCliente.isEmpty()) {
            sql.append(" AND EmailCliente LIKE ?");
        }
        
        sql.append(" ORDER BY DataOrdine DESC");

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (startDate != null && !startDate.isEmpty()) {
                stmt.setString(index++, startDate + " 00:00:00");
            }
            if (endDate != null && !endDate.isEmpty()) {
                stmt.setString(index++, endDate + " 23:59:59");
            }
            if (emailCliente != null && !emailCliente.isEmpty()) {
                stmt.setString(index++, "%" + emailCliente + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(rs.getInt("ID_Ordine"));
                ordine.setDataOrdine(rs.getTimestamp("DataOrdine"));
                ordine.setImportoTotale(rs.getDouble("ImportoTotale"));
                ordine.setStato(rs.getString("Stato"));
                ordini.add(ordine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }
}