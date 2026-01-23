package it.unisa.pharmaweb.control;

import com.google.gson.Gson;
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
        Gson gson = new Gson();
        Map<String, Object> responseData = new HashMap<>();
        
        try {
            if (action != null) {
                ProductDAO productDAO = new ProductDAO();

                if ("add".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantityToAdd = 1;
                    
                    int currentQuantityInCart = cart.getQuantityOfProduct(productId);
                    
                    // CONTROLLO DISPONIBILITÀ
                    if (productDAO.isAvailable(productId, currentQuantityInCart + quantityToAdd)) {
                        ProductBean product = productDAO.getProductById(productId);
                        if (product != null) {
                            cart.addItem(product, quantityToAdd);
                            responseData.put("success", true);
                            responseData.put("addedProductName", product.getNomeProdotto());
                        }
                    } else {
                        responseData.put("success", false);
                        responseData.put("error", "Quantità non disponibile in magazzino!");
                    }
                } else if ("remove".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    cart.removeItem(productId);
                    responseData.put("success", true);
                    // Ricarichiamo la pagina perché la rimozione avviene dalla pagina del carrello
                    response.sendRedirect(request.getContextPath() + "/WEB-INF/views/cart.jsp");
                    return;
                } else if ("update".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int newQuantity = Integer.parseInt(request.getParameter("quantity"));
                    
                    // Per l'update, controlliamo la disponibilità della nuova quantità totale
                    if(productDAO.isAvailable(productId, newQuantity)) {
                        cart.updateQuantity(productId, newQuantity);
                        responseData.put("success", true);
                    } else {
                        responseData.put("success", false);
                        responseData.put("error", "La quantità richiesta non è disponibile.");
                    }
                    // L'update avviene dalla pagina del carrello, quindi ricarichiamo
                    response.sendRedirect(request.getContextPath() + "/WEB-INF/views/cart.jsp");
                    return;
                } else if ("clear".equals(action)) {
                    cart.clear();
                    responseData.put("success", true);
                    response.sendRedirect(request.getContextPath() + "/WEB-INF/views/cart.jsp");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseData.put("success", false);
            responseData.put("error", "Richiesta non valida.");
        }

        // --- Risposta JSON per l'azione 'add' (o errori) ---
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        responseData.put("cartItemCount", cart.getItems().size());
        responseData.put("csrfToken", (String) session.getAttribute("csrfToken"));
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(responseData));
        out.flush();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }
}