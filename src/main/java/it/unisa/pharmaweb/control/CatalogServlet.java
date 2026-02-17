package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import it.unisa.pharmaweb.model.dao.CategoriaDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/catalogo")
public class CatalogServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recuperiamo i parametri dalla query string
        String searchString = request.getParameter("q");
        String categoryIdStr = request.getParameter("cat");
        
        ProductDAO productDAO = new ProductDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<ProductBean> products = new ArrayList<>();
        String pageTitle = "Catalogo Prodotti";

        try {
            if (searchString != null && !searchString.trim().isEmpty()) {
                // --- CASO 1: RICERCA PER NOME ---
                products = productDAO.searchProductsByName(searchString);
                pageTitle = "Risultati ricerca per: \"" + searchString + "\"";
                request.setAttribute("searchQuery", searchString);
                
            } else if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                // --- CASO 2: FILTRO PER CATEGORIA ---
                int categoryId = Integer.parseInt(categoryIdStr);
                products = productDAO.getProductsByCategory(categoryId);
                if (!products.isEmpty()) {
                    pageTitle = products.get(0).getNomeCategoria();
                } else {
                    pageTitle = "Categoria non trovata o vuota";
                }
                
            } else {
                // --- CASO 3: NESSUN FILTRO (MOSTRA TUTTO) ---
                products = productDAO.getNewProducts(20);
                pageTitle = "Tutti i Prodotti";
            }
        } catch (NumberFormatException e) {
            // Se l'ID categoria non è un numero, mostriamo lista vuota o redirect
            pageTitle = "Errore nei parametri";
        }

        // Impostiamo gli attributi per la View
        request.setAttribute("products", products);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("categories", categoriaDAO.doRetrieveAll());

        // Forward alla pagina del catalogo
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp");
        dispatcher.forward(request, response);
    }
    
    // La ricerca è sempre GET
}