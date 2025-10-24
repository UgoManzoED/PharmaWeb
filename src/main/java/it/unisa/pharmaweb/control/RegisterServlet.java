package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.UtenteDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
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

    // Pattern per la validazione
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");


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

        // Usiamo una Mappa per collezionare tutti gli errori di validazione
        Map<String, String> errors = new HashMap<>();

        // --- VALIDAZIONE DEI DATI ---

        if (nome == null || nome.trim().isEmpty()) {
            errors.put("nome", "Il campo Nome è obbligatorio.");
        }
        if (cognome == null || cognome.trim().isEmpty()) {
            errors.put("cognome", "Il campo Cognome è obbligatorio.");
        }
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            errors.put("email", "Inserisci un indirizzo email valido.");
        }
        if (password == null || password.isEmpty()) {
            errors.put("password", "Il campo Password è obbligatorio.");
        } else {
            if (!password.equals(confermaPassword)) {
                errors.put("confermaPassword", "Le password non corrispondono.");
            }
            if (password.length() < 8) {
                errors.put("password", "La password deve contenere almeno 8 caratteri.");
            }
            if (!PASSWORD_REGEX.matcher(password).matches()) {
                errors.put("password", "La password deve contenere almeno una maiuscola, una minuscola e un numero.");
            }
        }
        
        // --- CONTROLLO ESISTENZA EMAIL ---
        UtenteDAO utenteDAO = new UtenteDAO();
        if (errors.isEmpty() && utenteDAO.checkEmailExists(email)) {
            errors.put("email", "L'indirizzo email è già registrato.");
        }

        // --- RISULTATO CONTROLLI ---

        // Se la mappa degli errori non è vuota, c'è stato un problema.
        if (!errors.isEmpty()) {
            // Reinvia i dati inseriti dall'utente per non farglieli riscrivere
            request.setAttribute("form_nome", nome);
            request.setAttribute("form_cognome", cognome);
            request.setAttribute("form_email", email);
            
            // Invia la mappa degli errori alla JSP
            request.setAttribute("errors", errors);
            
            doGet(request, response);
            return;
        }

        // --- REGISTRAZIONE UTENTE ---
        UtenteBean nuovoUtente = new UtenteBean();
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(password);
        
        utenteDAO.saveUtente(nuovoUtente);

        // --- LOGIN AUTOMATICO ---
        UtenteBean utenteLoggato = utenteDAO.getUtenteByEmail(email);
        HttpSession session = request.getSession(true);
        utenteLoggato.setPassword(null);
        session.setAttribute("utente", utenteLoggato);
        
        response.sendRedirect(request.getContextPath() + "/");
    }
}