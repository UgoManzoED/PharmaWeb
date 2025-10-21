package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.bean.RigaOrdineBean;
import java.sql.*;

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
}