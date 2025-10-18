package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
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

@WebServlet("")
public class HomePageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Istanzia il DAO per accedere ai dati dei prodotti.
        ProductDAO productDAO = new ProductDAO();
        
        // Recuperiamo gli 8 prodotti più recenti.
        List<ProductBean> newProducts = productDAO.getNewProducts(8);
        
        // "Allega" la lista di prodotti alla richiesta HTTP.
        request.setAttribute("newProducts", newProducts);
        
        // Inoltra la richiesta (che ora contiene i prodotti) alla pagina JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}