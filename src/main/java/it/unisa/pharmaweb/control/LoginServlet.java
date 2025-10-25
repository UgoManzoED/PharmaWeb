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
            request.setAttribute("error", "Credenziali non valide. Riprova.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
            dispatcher.forward(request, response);
            return; // Interrompe l'esecuzione
        }

        // CASO 2: Login Riuscito
        HttpSession session = request.getSession(true); // Crea sessione o recupera una esistente
        utente.setPassword(null); // Rimuoviamo la password dall'oggetto prima di metterlo in sessione
        session.setAttribute("utente", utente); // Salviamo l'oggetto utente in modo da disporre dei dati nella sessione
        
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se l'utente fa una richiesta GET a /login gli mostra la pagina di login.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(request, response);
    }
}