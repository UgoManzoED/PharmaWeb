package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.RecensioneBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO {

    /**
     * Salva una nuova recensione nel database.
     * @param recensione il bean contenente i dati.
     */
    public void doSave(RecensioneBean recensione) {
        String sql = "INSERT INTO Recensione (Voto, Testo, DataRecensione, FK_Utente, FK_Prodotto) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recensione.getVoto());
            stmt.setString(2, recensione.getTesto());
            stmt.setTimestamp(3, new Timestamp(recensione.getDataRecensione().getTime()));
            stmt.setInt(4, recensione.getIdUtente());
            stmt.setInt(5, recensione.getIdProdotto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera tutte le recensioni per un determinato prodotto.
     * Usa la 'VistaRecensioniComplete'.
     * @param idProdotto l'ID del prodotto.
     * @return lista di recensioni.
     */
    public List<RecensioneBean> doRetrieveByProdotto(int idProdotto) {
        List<RecensioneBean> recensioni = new ArrayList<>();
        String sql = "SELECT * FROM VistaRecensioniComplete WHERE ID_Prodotto = ? ORDER BY DataRecensione DESC";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProdotto);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RecensioneBean bean = new RecensioneBean();
                bean.setIdRecensione(rs.getInt("ID_Recensione"));
                bean.setVoto(rs.getInt("Voto"));
                bean.setTesto(rs.getString("Testo"));
                bean.setDataRecensione(rs.getTimestamp("DataRecensione"));
                bean.setIdUtente(rs.getInt("ID_Utente"));
                bean.setIdProdotto(rs.getInt("ID_Prodotto"));
                bean.setNomeUtente(rs.getString("NomeUtenteVisibile"));

                recensioni.add(bean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recensioni;
    }

    /**
     * Controlla se un utente ha il permesso di recensire un prodotto.
     * Regola: L'utente deve aver acquistato il prodotto almeno una volta.
     * @param idUtente l'ID dell'utente.
     * @param idProdotto l'ID del prodotto.
     * @return true se l'utente ha acquistato il prodotto, false altrimenti.
     */
    public boolean canReview(int idUtente, int idProdotto) {
        String sql = "SELECT count(*) FROM Ordine o " +
                     "JOIN RigaOrdine ro ON o.ID_Ordine = ro.FK_Ordine " +
                     "WHERE o.FK_Utente = ? AND ro.FK_Prodotto = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Se il conteggio è > 0, significa che l'ha comprato
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}