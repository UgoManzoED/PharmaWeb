package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.MetodoPagamentoBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.MetodoPagamentoDAO;
import java.io.IOException;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/area-riservata/pagamenti")
public class UserPaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();

        if ("add".equals(action)) {
            String tipo = request.getParameter("tipo"); // Visa, Mastercard...
            String numero = request.getParameter("numero"); // Solo per estrarre le ultime 4 cifre
            String titolare = request.getParameter("titolare");
            String meseStr = request.getParameter("mese");
            String annoStr = request.getParameter("anno");

            if (numero != null && numero.length() >= 13) {
                MetodoPagamentoBean metodo = new MetodoPagamentoBean();
                metodo.setIdUtente(utente.getIdUtente());
                metodo.setTipoCarta(tipo);
                metodo.setTitolare(titolare);
                
                // Estrae le ultime 4 cifre
                String ultime4 = numero.substring(numero.length() - 4);
                metodo.setUltime4Cifre(ultime4);
                
                metodo.setMeseScadenza(Integer.parseInt(meseStr));
                metodo.setAnnoScadenza(Integer.parseInt(annoStr));

                pagamentoDAO.save(metodo);
            }

        } else if ("delete".equals(action)) {
            try {
                int idMetodo = Integer.parseInt(request.getParameter("idMetodo"));
                MetodoPagamentoBean metodo = pagamentoDAO.getById(idMetodo);
                if (metodo != null && metodo.getIdUtente() == utente.getIdUtente()) {
                    pagamentoDAO.delete(idMetodo);
                }
            } catch (NumberFormatException e) {
                // Errore ID
            }
        }

        response.sendRedirect(request.getContextPath() + "/area-riservata/checkout");
    }
}