package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.RecensioneBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import it.unisa.pharmaweb.model.dao.RecensioneDAO;
import it.unisa.pharmaweb.model.bean.CategoriaBean;
import it.unisa.pharmaweb.model.dao.CategoriaDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Questa servlet gestisce la logica per la homepage.
 * Recupera i prodotti da mostrare in vetrina e li inoltra alla pagina JSP.
 * @author Ugo Manzo
 * @version 1.0
 */

@WebServlet("/home")
public class HomePageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        RecensioneDAO recensioneDAO = new RecensioneDAO();
        
        // Recupero Prodotti
        List<ProductBean> newProducts = productDAO.getNewProducts(8);
        List<ProductBean> popularProducts = productDAO.getPopularProducts(8);
        List<ProductBean> discountedProducts = productDAO.getDiscountedProducts(8);
        
        // Recupero Recensioni
        List<RecensioneBean> latestReviews = recensioneDAO.getLatestReviews(3);
        
        // Recupero Categorie
        List<CategoriaBean> categories = categoriaDAO.doRetrieveAll();
        
        request.setAttribute("newProducts", newProducts);
        request.setAttribute("popularProducts", popularProducts);
        request.setAttribute("discountedProducts", discountedProducts);
        request.setAttribute("categories", categories);
        request.setAttribute("latestReviews", latestReviews);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/HomePage.jsp");
        dispatcher.forward(request, response);
    }
}