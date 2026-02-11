package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.IndirizzoBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.IndirizzoDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/area-riservata/indirizzi")
public class UserAddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        
        // Verifica sessione
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();

        if ("add".equals(action)) {
            // --- AGGIUNTA INDIRIZZO ---
            String via = request.getParameter("via");
            String citta = request.getParameter("citta");
            String cap = request.getParameter("cap");
            String provincia = request.getParameter("provincia");
            String destinatario = request.getParameter("destinatario");

            // Validazione
            if (via != null && !via.isEmpty() && cap != null && cap.length() == 5) {
                IndirizzoBean nuovoIndirizzo = new IndirizzoBean();
                nuovoIndirizzo.setIdUtente(utente.getIdUtente());
                nuovoIndirizzo.setVia(via);
                nuovoIndirizzo.setCitta(citta);
                nuovoIndirizzo.setCap(cap);
                nuovoIndirizzo.setProvincia(provincia);
                nuovoIndirizzo.setNomeDestinatario(destinatario);

                indirizzoDAO.save(nuovoIndirizzo);
            } else {
                request.setAttribute("error", "Dati indirizzo non validi");
            }

        } else if ("delete".equals(action)) {
            // --- RIMOZIONE INDIRIZZO ---
            try {
                int idIndirizzo = Integer.parseInt(request.getParameter("idIndirizzo"));
                
                // Controlla che l'indirizzo appartenga all'utente
                IndirizzoBean indirizzo = indirizzoDAO.getById(idIndirizzo);
                if (indirizzo != null && indirizzo.getIdUtente() == utente.getIdUtente()) {
                    indirizzoDAO.delete(idIndirizzo);
                }
            } catch (NumberFormatException e) {
                // Errore ID
            }
        }
        response.sendRedirect(request.getContextPath() + "/area-riservata/checkout");
    }
}