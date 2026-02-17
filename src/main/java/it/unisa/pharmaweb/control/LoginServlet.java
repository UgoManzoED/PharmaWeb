package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.CartBean;
import it.unisa.pharmaweb.model.bean.CartItemBean;
import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.bean.WishlistBean;
import it.unisa.pharmaweb.model.dao.CartDAO;
import it.unisa.pharmaweb.model.dao.UtenteDAO;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String passwordInChiaro = request.getParameter("password");
        
        UtenteDAO utenteDAO = new UtenteDAO();
        UtenteBean utente = utenteDAO.getUtenteByEmail(email);
        
        // CASO 1: Utente non trovato o password non corretta
        // BCrypt.checkpw gestisce entrambi i casi
        // L'utente viene recuperato, e poi si controlla se la password in chiaro corrisponde all'hash salvato
         if (utente == null || !BCrypt.checkpw(passwordInChiaro, utente.getPassword())) {
           
            HttpSession session = request.getSession();
            session.setAttribute("loginError", "Credenziali non valide. Riprova.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // CASO 2: Login Riuscito
        HttpSession session = request.getSession(true); // Crea sessione o recupera una esistente
        utente.setPassword(null); // Rimuoviamo la password dall'oggetto prima di metterlo in sessione
        session.setAttribute("utente", utente); // Salviamo l'oggetto utente in modo da disporre dei dati nella sessione
        
        // --- GESTIONE PERSISTENZA CARRELLO E WISHLIST ---
        
        // Recuperiamo i dati temporanei della sessione (prodotti aggiunti da anonimo)
        CartBean sessionCart = (CartBean) session.getAttribute("cart");
        WishlistBean sessionWishlist = (WishlistBean) session.getAttribute("wishlist");
        CartDAO cartDAO = new CartDAO();

        // 1. UNIONE CARRELLO
        // Recuperiamo il carrello salvato nel DB per questo utente
        CartBean dbCart = cartDAO.getCartByUser(utente.getIdUtente());
        
        if (sessionCart != null) {
            // Se l'utente aveva aggiunto prodotti da anonimo, li aggiungiamo al carrello del DB
            for (CartItemBean item : sessionCart.getItems()) {
                dbCart.addItem(item.getProduct(), item.getQuantity());
                // Aggiorniamo il record sul database per rendere la modifica permanente
                cartDAO.saveOrUpdateCartItem(utente.getIdUtente(), 
                                             item.getProduct().getIdProdotto(), 
                                             dbCart.getQuantityOfProduct(item.getProduct().getIdProdotto()));
            }
        }
        // Sovrascriviamo il carrello in sessione con quello unificato e persistente
        session.setAttribute("cart", dbCart);

        // 2. UNIONE WISHLIST
        // Recuperiamo la wishlist salvata nel DB
        WishlistBean dbWishlist = cartDAO.getWishlistByUser(utente.getIdUtente());
        
        if (sessionWishlist != null) {
            // Se l'utente aveva aggiunto preferiti da anonimo, li aggiungiamo alla wishlist del DB
            for (ProductBean p : sessionWishlist.getItems()) {
                dbWishlist.addItem(p);
                cartDAO.saveWishlistItem(utente.getIdUtente(), p.getIdProdotto());
            }
        }
        // Sovrascriviamo la wishlist in sessione con quella unificata
        session.setAttribute("wishlist", dbWishlist);

        // Reindirizzamento alla homepage
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Controlla se c'è un errore in sessione dal POST precedente
        HttpSession session = request.getSession();
        String error = (String) session.getAttribute("loginError");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("loginError"); // Rimuovi dopo averlo mostrato
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
    }
}