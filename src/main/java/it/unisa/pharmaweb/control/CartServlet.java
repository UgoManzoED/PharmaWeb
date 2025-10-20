package it.unisa.pharmaweb.control;

import com.google.gson.Gson; // Importa Gson
import it.unisa.pharmaweb.model.bean.CartBean;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }
        
        String action = request.getParameter("action");
        ProductBean product = null;
        
        try {
            if (action != null) {
                if ("add".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = 1;
                    
                    ProductDAO productDAO = new ProductDAO();
                    product = productDAO.getProductById(productId);
                    
                    if (product != null) {
                        cart.addItem(product, quantity);
                    }
                } else if ("remove".equals(action)) {
                	int productId = Integer.parseInt(request.getParameter("productId"));
                    cart.removeItem(productId);
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Invia un codice di errore HTTP
            response.getWriter().write("ID prodotto non valido.");
            return;
        }
        
        // 1. Imposta il tipo di contenuto della risposta a JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 2. Prepara i dati da inviare come risposta
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("cartItemCount", cart.getItems().size()); // Numero di item diversi nel carrello
        if (product != null) {
            responseData.put("addedProductName", product.getNomeProdotto());
        }
        
        // 3. Usa Gson per convertire la mappa in una stringa JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);
        
        // 4. Scrivi la stringa JSON nella risposta
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
}