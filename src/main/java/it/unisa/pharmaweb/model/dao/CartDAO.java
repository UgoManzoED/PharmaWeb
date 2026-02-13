package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.CartBean;
import it.unisa.pharmaweb.model.bean.CartItemBean;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.WishlistBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione della persistenza del carrello e della wishlist.
 * Permette di salvare lo stato tra diverse sessioni di navigazione degli utenti loggati.
 */
public class CartDAO {

    // --- LOGICA CARRELLO ---

    /**
     * Salva o aggiorna un articolo nel carrello dell'utente nel database.
     */
    public void saveOrUpdateCartItem(int idUtente, int idProdotto, int quantita) {
        String sql = "INSERT INTO CarrelloPersistente (FK_Utente, FK_Prodotto, Quantita) " +
                     "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE Quantita = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.setInt(3, quantita);
            stmt.setInt(4, quantita); // Per ON DUPLICATE KEY UPDATE
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimuove un articolo dal carrello persistente.
     */
    public void removeCartItem(int idUtente, int idProdotto) {
        String sql = "DELETE FROM CarrelloPersistente WHERE FK_Utente = ? AND FK_Prodotto = ?";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Svuota il carrello persistente dell'utente (es. dopo il checkout).
     */
    public void clearCart(int idUtente) {
        String sql = "DELETE FROM CarrelloPersistente WHERE FK_Utente = ?";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica il carrello salvato nel DB per un determinato utente.
     * Usa la VistaCatalogoProdotti per avere i prezzi aggiornati.
     */
    public CartBean getCartByUser(int idUtente) {
        CartBean cart = new CartBean();
        String sql = "SELECT v.*, cp.Quantita FROM CarrelloPersistente cp " +
                     "JOIN VistaCatalogoProdotti v ON cp.FK_Prodotto = v.ID_Prodotto " +
                     "WHERE cp.FK_Utente = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setIdProdotto(rs.getInt("ID_Prodotto"));
                p.setNomeProdotto(rs.getString("NomeProdotto"));
                p.setPrezzoDiListino(rs.getDouble("PrezzoDiListino"));
                p.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                p.setScontoPercentuale(rs.getInt("ScontoPercentuale"));
                p.setUrlImmagine(rs.getString("URL_Immagine"));
                p.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                
                int quantita = rs.getInt("Quantita");
                cart.addItem(p, quantita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    // --- LOGICA WISHLIST ---

    /**
     * Salva un prodotto nella wishlist dell'utente nel DB.
     */
    public void saveWishlistItem(int idUtente, int idProdotto) {
        String sql = "INSERT IGNORE INTO WishlistPersistente (FK_Utente, FK_Prodotto) VALUES (?, ?)";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimuove un prodotto dalla wishlist persistente.
     */
    public void removeWishlistItem(int idUtente, int idProdotto) {
        String sql = "DELETE FROM WishlistPersistente WHERE FK_Utente = ? AND FK_Prodotto = ?";
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica la wishlist dal DB.
     */
    public WishlistBean getWishlistByUser(int idUtente) {
        WishlistBean wishlist = new WishlistBean();
        String sql = "SELECT v.* FROM WishlistPersistente wp " +
                     "JOIN VistaCatalogoProdotti v ON wp.FK_Prodotto = v.ID_Prodotto " +
                     "WHERE wp.FK_Utente = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setIdProdotto(rs.getInt("ID_Prodotto"));
                p.setNomeProdotto(rs.getString("NomeProdotto"));
                p.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                p.setUrlImmagine(rs.getString("URL_Immagine"));
                p.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                wishlist.addItem(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wishlist;
    }
}