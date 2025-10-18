package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.ProductBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

	/**
     * Recupera una lista dei prodotti più recenti aggiunti al catalogo.
     * Utilizza la VistaCatalogoProdotti per ottenere dati completi, incluso il prezzo finale.
     * @param limit il numero massimo di prodotti da restituire.
     * @return una List di ProductBean.
     */
    public List<ProductBean> getNewProducts(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti ORDER BY ID_Prodotto DESC LIMIT ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setIdProdotto(rs.getInt("ID_Prodotto"));
                product.setNomeProdotto(rs.getString("NomeProdotto"));
                product.setDescrizione(rs.getString("Descrizione"));
                product.setPrezzoDiListino(rs.getDouble("PrezzoDiListino"));
                product.setScontoPercentuale(rs.getInt("ScontoPercentuale"));
                product.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                product.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                product.setUrlImmagine(rs.getString("URL_Immagine"));
                product.setIdCategoria(rs.getInt("ID_Categoria"));
                product.setNomeCategoria(rs.getString("NomeCategoria"));
                
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Recupera un singolo prodotto tramite il suo ID.
     * @param id l'ID del prodotto da cercare.
     * @return un ProductBean se trovato, altrimenti null.
     */
    public ProductBean getProductById(int id) {
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE ID_Prodotto = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ProductBean product = new ProductBean();
                product.setIdProdotto(rs.getInt("ID_Prodotto"));
                product.setNomeProdotto(rs.getString("NomeProdotto"));
                product.setDescrizione(rs.getString("Descrizione"));
                product.setPrezzoDiListino(rs.getDouble("PrezzoDiListino"));
                product.setScontoPercentuale(rs.getInt("ScontoPercentuale"));
                product.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                product.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                product.setUrlImmagine(rs.getString("URL_Immagine"));
                product.setIdCategoria(rs.getInt("ID_Categoria"));
                product.setNomeCategoria(rs.getString("NomeCategoria"));
                return product;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Recupera una lista di tutti i prodotti appartenenti a una specifica categoria.
     * @param idCategoria l'ID della categoria da cercare.
     * @return una List di ProductBean.
     */
    public List<ProductBean> getProductsByCategory(int idCategoria) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE ID_Categoria = ? ORDER BY NomeProdotto ASC";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setIdProdotto(rs.getInt("ID_Prodotto"));
                product.setNomeProdotto(rs.getString("NomeProdotto"));
                product.setDescrizione(rs.getString("Descrizione"));
                product.setPrezzoDiListino(rs.getDouble("PrezzoDiListino"));
                product.setScontoPercentuale(rs.getInt("ScontoPercentuale"));
                product.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                product.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                product.setUrlImmagine(rs.getString("URL_Immagine"));
                product.setIdCategoria(rs.getInt("ID_Categoria"));
                product.setNomeCategoria(rs.getString("NomeCategoria"));
                
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Cerca prodotti il cui nome contiene una data stringa di ricerca.
     * La ricerca è case-insensitive (non distingue tra maiuscole e minuscole).
     * La query è effettuata tramite l'uso di PreparedStatement per evitare SQL injections
     * @param query la stringa da cercare nel nome dei prodotti.
     * @return una List di ProductBean che corrispondono alla ricerca.
     */
    public List<ProductBean> searchProductsByName(String query) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE LOWER(NomeProdotto) LIKE ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchQuery = "%" + query.toLowerCase() + "%";
            stmt.setString(1, searchQuery);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setIdProdotto(rs.getInt("ID_Prodotto"));
                product.setNomeProdotto(rs.getString("NomeProdotto"));
                product.setDescrizione(rs.getString("Descrizione"));
                product.setPrezzoDiListino(rs.getDouble("PrezzoDiListino"));
                product.setScontoPercentuale(rs.getInt("ScontoPercentuale"));
                product.setPrezzoFinale(rs.getDouble("PrezzoFinale"));
                product.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                product.setUrlImmagine(rs.getString("URL_Immagine"));
                product.setIdCategoria(rs.getInt("ID_Categoria"));
                product.setNomeCategoria(rs.getString("NomeCategoria"));
                
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
