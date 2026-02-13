package it.unisa.pharmaweb.control.admin;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.dao.OrdineDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet dedicata alla gestione degli ordini lato amministratore.
 * Permette la visualizzazione globale di tutti gli ordini effettuati sul sito,
 * offrendo funzionalità di filtraggio per data e per email del cliente.
 * 
 * @author Ugo Manzo
 * @version 1.1
 */
@WebServlet("/admin/ordini")
public class AdminOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la visualizzazione della lista ordini.
     * Recupera i parametri di filtraggio dall'URL e interroga il database.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recupero parametri di filtraggio
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String email = request.getParameter("email");

        // Istanza del DAO per l'accesso ai dati degli ordini
        OrdineDAO ordineDAO = new OrdineDAO();
        
        // Recupero della lista filtrata
        List<OrdineBean> ordini = ordineDAO.getAllOrdini(startDate, endDate, email);

        // Impostiamo la lista degli ordini come attributo della richiesta per la JSP
        request.setAttribute("ordini", ordini);
        
        // Re-inviamo i parametri di ricerca alla JSP per implementare lo sticky form
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("email", email);

        // Inoltro della richiesta alla vista protetta in WEB-INF
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/ordini.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Le operazioni di visualizzazione e filtraggio vengono gestite tramite metodo GET.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}