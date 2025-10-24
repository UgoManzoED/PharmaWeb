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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");

        UtenteDAO utenteDAO = new UtenteDAO();

        // --- VALIDAZIONE DEI DATI ---
        
        // Controlla se le password corrispondono
        if (!password.equals(confermaPassword)) {
            request.setAttribute("error", "Le password non corrispondono.");
            doGet(request, response); // Ricarica la pagina di registrazione con l'errore
            return;
        }

        // Controlla se l'email è già in uso
        if (utenteDAO.checkEmailExists(email)) {
            request.setAttribute("error", "L'indirizzo email è già registrato.");
            doGet(request, response);
            return;
        }

        // TODO: Altri controlli di validazione (es. lunghezza minima password)

        // --- REGISTRAZIONE UTENTE ---

        // Crea un nuovo UtenteBean con i dati validati
        UtenteBean nuovoUtente = new UtenteBean();
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(password); // Passiamo la password in chiaro, il DAO si occuperà dell'hashing
        
        // Salva il nuovo utente nel database
        utenteDAO.saveUtente(nuovoUtente);

        // --- LOGIN AUTOMATICO POST-REGISTRAZIONE ---

        // Recupera l'utente appena creato per avere l'ID e gli altri dati di default
        UtenteBean utenteLoggato = utenteDAO.getUtenteByEmail(email);

        HttpSession session = request.getSession(true);
        utenteLoggato.setPassword(null); // Password rimossa per sicurezza
        session.setAttribute("utente", utenteLoggato);
        
        // Reindirizza l'utente alla homepage
        response.sendRedirect(request.getContextPath() + "/");
    }
}