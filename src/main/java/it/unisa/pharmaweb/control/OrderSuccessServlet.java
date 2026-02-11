package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.OrdineDAO;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet che gestisce la visualizzazione della pagina di conferma ordine.
 * Serve a nascondere la JSP all'interno di WEB-INF e a garantire che l'utente sia loggato.
 */
@WebServlet("/ordine-confermato")
public class OrderSuccessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. L'utente deve essere loggato
        HttpSession session = request.getSession(false);
        UtenteBean utente = (session != null) ? (UtenteBean) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idOrdineStr = request.getParameter("id");
        
        try {
            int idOrdine = Integer.parseInt(idOrdineStr);
            
            // 2. Recupera i dettagli completi dell'ordine dal Database
            OrdineDAO ordineDAO = new OrdineDAO();
            OrdineBean ordine = ordineDAO.getOrdineById(idOrdine);

            // 3. Controllo di Sicurezza
            // L'ordine esiste? E appartiene all'utente loggato?
            if (ordine == null || ordine.getIdUtente() != utente.getIdUtente()) {
                // Se provo a vedere l'ordine di un altro, errore 403.
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non hai i permessi per visualizzare questo ordine.");
                return;
            }

            // 4. Passa l'intero oggetto ordine alla JSP
            request.setAttribute("ordine", ordine);

            // 5. Forward
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ordine-confermato.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            // ID non valido
            response.sendRedirect(request.getContextPath() + "/area-riservata/dashboard");
        }
    }
}