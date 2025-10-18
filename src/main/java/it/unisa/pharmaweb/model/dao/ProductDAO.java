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
 * Fornisce metodi per interagire con la tabella Prodotto
 * e le relative viste nel database.
 * @author Ugo Manzo
 * @version 1.0
 */

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
     * La query è effettuata tramite l'uso di PreparedStatement per evitare SQL injections.
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
    
    /**
     * Salva un nuovo prodotto nel database.
     * @param product il ProductBean contenente i dati del nuovo prodotto (l'ID sarà ignorato).
     */
    public void saveProduct(ProductBean product) {
        String sql = "INSERT INTO Prodotto (Nome, Descrizione, Prezzo, ScontoPercentuale, QuantitaDisponibile, URL_Immagine, FK_Categoria) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManagerConnection-Pool.getConnection();
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
     * Rimuove un prodotto dal database.
     * L'operazione fallirà se il prodotto è presente in almeno un ordine,
     * a causa del vincolo di integrità referenziale.
     * @param idProdotto l'ID del prodotto da eliminare.
     */
    public void deleteProduct(int idProdotto) {
        String sql = "DELETE FROM Prodotto WHERE ID_Prodotto = ?";

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
     * @throws SQLException se l'aggiornamento fallisce.
     * @throws IllegalArgumentException se la quantità è negativa.
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
     * Usato per le operazioni di rifornimento del magazzino.
     * @param idProdotto l'ID del prodotto.
     * @param quantitaDaAggiungere la quantità (positiva) da aggiungere.
     * @throws IllegalArgumentException se la quantità è negativa.
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
     * @param idProdotto l'ID del prodotto da controllare.
     * @param quantitaRichiesta la quantità che si desidera acquistare.
     * @return true se la quantità richiesta è disponibile, false altrimenti.
     */
    public boolean isAvailable(int idProdotto, int quantitaRichiesta) {
        String sql = "SELECT QuantitaDisponibile FROM Prodotto WHERE ID_Prodotto = ?";

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
     * Restituisce il numero totale di prodotti presenti nel catalogo.
     * Utile per la paginazione.
     * @return il conteggio totale dei prodotti.
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Prodotto";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1); // Restituisce il COUNT
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Recupera una "pagina" di prodotti dal catalogo, ordinati per nome.
     * @param offset il punto di partenza da cui recuperare i prodotti (es. per la pagina 3 con 10 prodotti per pagina, l'offset è 20).
     * @param limit il numero di prodotti da recuperare (il numero di prodotti per pagina).
     * @return una List di ProductBean per la pagina richiesta.
     */
    public List<ProductBean> getProductsPaginated(int offset, int limit) {
    	List<ProductBean> products = new ArrayList<>();
    	// Usiamo la vista e la clausola LIMIT di MySQL per la paginazione.
    	String sql = "SELECT * FROM VistaCatalogoProdotti ORDER BY NomeProdotto ASC LIMIT ? OFFSET ?";
    	
    	try (Connection conn = DriverManagerConnectionPool.getConnection();
	    	PreparedStatement stmt = conn.prepareStatement(sql)) {
		    	stmt.setInt(1, limit);
		    	stmt.setInt(2, offset);
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
