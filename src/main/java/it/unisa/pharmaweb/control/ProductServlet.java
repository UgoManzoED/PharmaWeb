package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.ProductBean;
import it.unisa.pharmaweb.model.bean.RecensioneBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.ProductDAO;
import it.unisa.pharmaweb.model.dao.RecensioneDAO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/prodotto")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la visualizzazione della pagina di dettaglio del prodotto.
     * URL: /prodotto?id=123
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        // 1. Validazione ID
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/"); // Torna alla home se manca l'ID
            return;
        }

        try {
            int idProdotto = Integer.parseInt(idStr);
            ProductDAO productDAO = new ProductDAO();
            RecensioneDAO recensioneDAO = new RecensioneDAO();

            // 2. Recupera il prodotto
            ProductBean product = productDAO.getProductById(idProdotto);
            
            // Se il prodotto non esiste (es. ID sbagliato)
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
                return;
            }

            // 3. Recupera le recensioni del prodotto
            List<RecensioneBean> recensioni = recensioneDAO.doRetrieveByProdotto(idProdotto);

            // 4. Controlla se l'utente è loggato E ha comprato il prodotto per poterlo recensire
            boolean canReview = false;
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("utente") != null) {
                UtenteBean utente = (UtenteBean) session.getAttribute("utente");
                canReview = recensioneDAO.canReview(utente.getIdUtente(), idProdotto);
            }

            // 5. Imposta gli attributi per la JSP
            request.setAttribute("product", product);
            request.setAttribute("recensioni", recensioni);
            request.setAttribute("canReview", canReview);

            // 6. Forward alla pagina di dettaglio
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/product-detail.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    /**
     * Gestisce l'invio di una nuova recensione.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;

        // Se non loggato, al login
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
            int voto = Integer.parseInt(request.getParameter("voto"));
            String testo = request.getParameter("testo");

            RecensioneDAO recensioneDAO = new RecensioneDAO();

            // Ricontrolla lato server se può recensire
            if (recensioneDAO.canReview(utente.getIdUtente(), idProdotto)) {
                RecensioneBean recensione = new RecensioneBean();
                recensione.setIdProdotto(idProdotto);
                recensione.setIdUtente(utente.getIdUtente());
                recensione.setVoto(voto);
                recensione.setTesto(testo);
                recensione.setDataRecensione(new Date());

                recensioneDAO.doSave(recensione);
            } else {
                // Tentativo di recensire senza permesso
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non hai acquistato questo prodotto.");
                return;
            }

            // Redirect alla pagina del prodotto per vedere la nuova recensione
            response.sendRedirect(request.getContextPath() + "/prodotto?id=" + idProdotto);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati non validi");
        }
    }
}