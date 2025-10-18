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
    
    // public List<ProductBean> getProductsByCategory(int idCategoria) { ... }
    // public List<ProductBean> searchProductsByName(String query) { ... }
}
