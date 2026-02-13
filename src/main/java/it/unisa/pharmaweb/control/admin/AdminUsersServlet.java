package it.unisa.pharmaweb.control.admin;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.UtenteDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet dedicata alla gestione degli utenti lato amministratore.
 * Permette di visualizzare la lista degli iscritti e di gestire le cancellazioni.
 */
@WebServlet("/admin/utenti")
public class AdminUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Visualizza la lista di tutti gli utenti registrati.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtenteDAO utenteDAO = new UtenteDAO();

        List<UtenteBean> utenti = utenteDAO.getAllUtenti();
        
        request.setAttribute("utenti", utenti);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/admin/utenti.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce le operazioni di modifica o cancellazione utente.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        UtenteDAO utenteDAO = new UtenteDAO();

        if ("delete".equals(action)) {
            try {
                int idUtente = Integer.parseInt(request.getParameter("id"));
                
                // Impedisci all'admin di cancellare se stesso per errore
                UtenteBean sessionUser = (UtenteBean) request.getSession().getAttribute("utente");
                if (sessionUser != null && sessionUser.getIdUtente() == idUtente) {
                    request.setAttribute("error", "Non puoi cancellare il tuo account amministratore mentre sei loggato.");
                } else {
                    utenteDAO.deleteUtente(idUtente);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Dopo l'operazione, ricarichiamo la lista
        response.sendRedirect(request.getContextPath() + "/admin/utenti");
    }
}