package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.UtenteDAO;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        UtenteDAO utenteDAO = new UtenteDAO();
        UtenteBean utente = utenteDAO.getUtenteByEmail(email);
        
        // --- GESTIONE DELLA LOGICA DI LOGIN ---

        // CASO 1: Utente non trovato nel database
        if (utente == null) {
            request.setAttribute("error", "Credenziali non valide. Riprova.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return; // Interrompe l'esecuzione
        }

        // CASO 2: Password non corretta
        // NOTA: Temporaneo, useremo l'hash, non in chiaro
        if (!utente.getPassword().equals(password)) {
            request.setAttribute("error", "Credenziali non valide. Riprova.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // CASO 3: Login Riuscito
        // Creiamo una nuova sessione o ne recuperiamo una esistente
        HttpSession session = request.getSession(true); 
        // Salviamo l'intero oggetto UtenteBean nella sessione.
        // Questo ci permetterà di accedere ai dati dell'utente in qualsiasi pagina.
        session.setAttribute("utente", utente);
        
        // Reindirizziamo l'utente alla homepage dopo il login.
        // Usiamo sendRedirect per cambiare l'URL nel browser.
        response.sendRedirect(request.getContextPath() + "/");
    }
}