package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.ProductBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione dei dati dei prodotti.
 * Fornisce metodi per interagire con la tabella Prodotto e le relative viste nel database.
 * Implementa la logica di Soft Delete (campo 'Attivo') per preservare lo storico.
 * 
 * @author Ugo Manzo
 * @version 1.1
 */
public class ProductDAO {

    /**
     * Recupera una lista dei prodotti più recenti aggiunti al catalogo.
     * Filtra solo i prodotti attivi (Soft Delete).
     * @param limit il numero massimo di prodotti da restituire.
     * @return una List di ProductBean.
     */
    public List<ProductBean> getNewProducts(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE Attivo = 1 ORDER BY ID_Prodotto DESC LIMIT ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean product = mapRowToBean(rs);
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
                return mapRowToBean(rs);
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
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE ID_Categoria = ? AND Attivo = 1 ORDER BY NomeProdotto ASC";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapRowToBean(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Cerca prodotti il cui nome contiene una data stringa di ricerca.
     * @param query la stringa da cercare nel nome dei prodotti.
     * @return una List di ProductBean che corrispondono alla ricerca.
     */
    public List<ProductBean> searchProductsByName(String query) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE LOWER(NomeProdotto) LIKE ? AND Attivo = 1";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchQuery = "%" + query.toLowerCase() + "%";
            stmt.setString(1, searchQuery);
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapRowToBean(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Salva un nuovo prodotto nel database.
     * @param product il ProductBean contenente i dati del nuovo prodotto.
     */
    public void saveProduct(ProductBean product) {
        String sql = "INSERT INTO Prodotto (Nome, Descrizione, Prezzo, ScontoPercentuale, QuantitaDisponibile, URL_Immagine, FK_Categoria) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getNomeProdotto());
            stmt.setString(2, product.getDescrizione());
            stmt.setDouble(3, product.getPrezzoDiListino());
            stmt.setInt(4, product.getScontoPercentuale());
            stmt.setInt(5, product.getQuantitaDisponibile());
            stmt.setString(6, product.getUrlImmagine());
            stmt.setInt(7, product.getIdCategoria());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Aggiorna i dati di un prodotto esistente nel database.
     * @param product il ProductBean con i dati aggiornati.
     */
    public void updateProduct(ProductBean product) {
        String sql = "UPDATE Prodotto SET Nome = ?, Descrizione = ?, Prezzo = ?, ScontoPercentuale = ?, " +
                     "QuantitaDisponibile = ?, URL_Immagine = ?, FK_Categoria = ? WHERE ID_Prodotto = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getNomeProdotto());
            stmt.setString(2, product.getDescrizione());
            stmt.setDouble(3, product.getPrezzoDiListino());
            stmt.setInt(4, product.getScontoPercentuale());
            stmt.setInt(5, product.getQuantitaDisponibile());
            stmt.setString(6, product.getUrlImmagine());
            stmt.setInt(7, product.getIdCategoria());
            stmt.setInt(8, product.getIdProdotto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Rimuove un prodotto dal database (Soft Delete).
     * Imposta il flag 'Attivo' a 0 per mantenere l'integrità referenziale con gli ordini passati.
     * @param idProdotto l'ID del prodotto da eliminare.
     */
    public void deleteProduct(int idProdotto) {
        String sql = "UPDATE Prodotto SET Attivo = 0 WHERE ID_Prodotto = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProdotto);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Diminuisce la quantità disponibile di un prodotto.
     * Usato durante la finalizzazione di un ordine.
     * @param conn la connessione transazionale.
     * @param idProdotto l'ID del prodotto.
     * @param quantitaDaSottrarre la quantità (positiva) da sottrarre.
     */
    public void decreaseStock(Connection conn, int idProdotto, int quantitaDaSottrarre) throws SQLException {
        if (quantitaDaSottrarre < 0) {
            throw new IllegalArgumentException("La quantità da sottrarre non può essere negativa.");
        }
        String sql = "UPDATE Prodotto SET QuantitaDisponibile = QuantitaDisponibile - ? WHERE ID_Prodotto = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantitaDaSottrarre);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Aumenta la quantità disponibile di un prodotto.
     * @param idProdotto l'ID del prodotto.
     * @param quantitaDaAggiungere la quantità (positiva) da aggiungere.
     */
    public void increaseStock(int idProdotto, int quantitaDaAggiungere) {
        if (quantitaDaAggiungere < 0) {
            throw new IllegalArgumentException("La quantità da aggiungere non può essere negativa.");
        }
        String sql = "UPDATE Prodotto SET QuantitaDisponibile = QuantitaDisponibile + ? WHERE ID_Prodotto = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantitaDaAggiungere);
            stmt.setInt(2, idProdotto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Controlla se una data quantità di un prodotto è disponibile in magazzino.
     * Verifica anche che il prodotto sia Attivo.
     * @param idProdotto l'ID del prodotto.
     * @param quantitaRichiesta la quantità richiesta.
     */
    public boolean isAvailable(int idProdotto, int quantitaRichiesta) {
        String sql = "SELECT QuantitaDisponibile FROM Prodotto WHERE ID_Prodotto = ? AND Attivo = 1";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProdotto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int quantitaDisponibile = rs.getInt("QuantitaDisponibile");
                return quantitaDisponibile >= quantitaRichiesta;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Restituisce il numero totale di prodotti attivi nel catalogo.
     * Utile per la paginazione.
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Prodotto WHERE Attivo = 1";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Recupera una "pagina" di prodotti attivi dal catalogo.
     * @param offset punto di partenza.
     * @param limit numero di prodotti.
     */
    public List<ProductBean> getProductsPaginated(int offset, int limit) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE Attivo = 1 ORDER BY NomeProdotto ASC LIMIT ? OFFSET ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, limit);
                stmt.setInt(2, offset);
                ResultSet rs = stmt.executeQuery();
        
                while (rs.next()) {
                     products.add(mapRowToBean(rs));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Recupera i prodotti più venduti.
     */
    public List<ProductBean> getPopularProducts(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT p.*, c.Nome AS NomeCategoria " +
                     "FROM Prodotto p " +
                     "JOIN RigaOrdine ro ON p.ID_Prodotto = ro.FK_Prodotto " +
                     "LEFT JOIN Categoria c ON p.FK_Categoria = c.ID_Categoria " +
                     "WHERE p.Attivo = 1 " +
                     "GROUP BY p.ID_Prodotto " +
                     "ORDER BY SUM(ro.Quantita) DESC " +
                     "LIMIT ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setIdProdotto(rs.getInt("ID_Prodotto"));
                product.setNomeProdotto(rs.getString("Nome"));
                product.setDescrizione(rs.getString("Descrizione"));
                
                double prezzoListino = rs.getDouble("Prezzo");
                int sconto = rs.getInt("ScontoPercentuale");
                product.setPrezzoDiListino(prezzoListino);
                product.setScontoPercentuale(sconto);
                
                if (sconto > 0) {
                    product.setPrezzoFinale(prezzoListino * (1 - sconto / 100.0));
                } else {
                    product.setPrezzoFinale(prezzoListino);
                }
                
                product.setQuantitaDisponibile(rs.getInt("QuantitaDisponibile"));
                product.setUrlImmagine(rs.getString("URL_Immagine"));
                product.setIdCategoria(rs.getInt("FK_Categoria"));
                product.setNomeCategoria(rs.getString("NomeCategoria"));
                
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Recupera i prodotti attivi in sconto.
     */
    public List<ProductBean> getDiscountedProducts(int limit) {
        List<ProductBean> products = new ArrayList<>();
        String sql = "SELECT * FROM VistaCatalogoProdotti WHERE ScontoPercentuale > 0 AND Attivo = 1 ORDER BY ScontoPercentuale DESC LIMIT ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapRowToBean(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Metodo di utility privato per mappare una riga del ResultSet (dalla Vista) a un ProductBean.
     * Evita duplicazione di codice.
     */
    private ProductBean mapRowToBean(ResultSet rs) throws SQLException {
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
}