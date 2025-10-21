package it.unisa.pharmaweb.control;

import com.google.gson.Gson;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.WishlistBean;
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
        
        String action = request.getParameter("action");
        ProductBean product = null;
        
        try {
            if (action != null) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                
                if ("add".equals(action)) {
                    ProductDAO productDAO = new ProductDAO();
                    product = productDAO.getProductById(productId);
                    if (product != null) {
                        wishlist.addItem(product);
                    }
                } else if ("remove".equals(action)) {
                    wishlist.removeItem(productId);
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID prodotto non valido.");
            return;
        }

        // --- Risposta JSON (identica a quella del carrello) ---
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("wishlistItemCount", wishlist.getSize());
        if (product != null) {
            responseData.put("addedProductName", product.getNomeProdotto());
        }
        
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseData);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/wishlist.jsp").forward(request, response);
    }
}