package it.unisa.pharmaweb.control;

import com.google.gson.Gson;
import it.unisa.pharmaweb.model.bean.CartBean;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.WishlistBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Recupera o crea la Wishlist dalla sessione
        WishlistBean wishlist = (WishlistBean) session.getAttribute("wishlist");
        if (wishlist == null) {
            wishlist = new WishlistBean();
            session.setAttribute("wishlist", wishlist);
        }
        
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }
        
        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAO();
        
        try {
            if (action != null) {
                // Azioni che rispondono con JSON (tipicamente da AJAX)
                if ("add".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    ProductBean product = productDAO.getProductById(productId);
                    if (product != null) {
                        wishlist.addItem(product);
                    }
                    // Prepara e invia risposta JSON
                    sendJsonResponse(response, session, wishlist.getSize(), product != null ? product.getNomeProdotto() : null);
                    return; // Interrompe l'esecuzione per non fare redirect
                }

                // Azioni che ricaricano la pagina (tipicamente dalla wishlist.jsp)
                if ("remove".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    wishlist.removeItem(productId);
                } else if ("clear".equals(action)) {
                    wishlist.clear();
                } else if ("moveToCart".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    ProductBean product = productDAO.getProductById(productId);
                    if (product != null && productDAO.isAvailable(productId, cart.getQuantityOfProduct(productId) + 1)) {
                        cart.addItem(product, 1);
                        wishlist.removeItem(productId);
                    }
                } else if ("moveAllToCart".equals(action)) {
                    // Copiamo la lista per evitare problemi di concorrenza durante l'iterazione e la rimozione
                    for (ProductBean product : new ArrayList<>(wishlist.getItems())) {
                        if (productDAO.isAvailable(product.getIdProdotto(), cart.getQuantityOfProduct(product.getIdProdotto()) + 1)) {
                            cart.addItem(product, 1);
                            wishlist.removeItem(product.getIdProdotto());
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID prodotto non valido.");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/WEB-INF/views/wishlist.jsp");
    }
    
    private void sendJsonResponse(HttpServletResponse response, HttpSession session, int itemCount, String productName) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("wishlistItemCount", itemCount);
        if (productName != null) {
            responseData.put("addedProductName", productName);
        }
        
        responseData.put("csrfToken", (String) session.getAttribute("csrfToken"));
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(request, response);
    }
}