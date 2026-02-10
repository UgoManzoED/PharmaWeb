package it.unisa.pharmaweb.model.dao;

import it.unisa.pharmaweb.model.bean.CategoriaBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    /**
     * Recupera tutte le categorie presenti nel database.
     * @return una List di CategoriaBean.
     */
    public List<CategoriaBean> doRetrieveAll() {
        List<CategoriaBean> categorie = new ArrayList<>();
        String sql = "SELECT * FROM Categoria ORDER BY Nome";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setIdCategoria(rs.getInt("ID_Categoria"));
                categoria.setNome(rs.getString("Nome"));
                categorie.add(categoria);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorie;
    }

    /**
     * Recupera una singola categoria tramite il suo ID.
     * @param id l'ID della categoria.
     * @return il CategoriaBean se trovato, null altrimenti.
     */
    public CategoriaBean doRetrieveById(int id) {
        String sql = "SELECT * FROM Categoria WHERE ID_Categoria = ?";
        
        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setIdCategoria(rs.getInt("ID_Categoria"));
                categoria.setNome(rs.getString("Nome"));
                return categoria;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Salva una nuova categoria.
     * @param categoria il bean con il nome da salvare.
     */
    public void doSave(CategoriaBean categoria) {
        String sql = "INSERT INTO Categoria (Nome) VALUES (?)";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Aggiorna il nome di una categoria esistente.
     * @param categoria il bean con i dati aggiornati.
     */
    public void doUpdate(CategoriaBean categoria) {
        String sql = "UPDATE Categoria SET Nome = ? WHERE ID_Categoria = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getIdCategoria());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancella una categoria.
     * Fallisce se ci sono prodotti collegati (Foreign Key constraint).
     * @param id l'ID della categoria da cancellare.
     */
    public void doDelete(int id) {
        String sql = "DELETE FROM Categoria WHERE ID_Categoria = ?";

        try (Connection conn = DriverManagerConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}