package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.IndirizzoDAO;
import it.unisa.pharmaweb.model.dao.MetodoPagamentoDAO;
import it.unisa.pharmaweb.model.dao.OrdineDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/area-riservata/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");

        // Prepara i dati per le varie sezioni della dashboard
        OrdineDAO ordineDAO = new OrdineDAO();
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();

        // Recupera lo storico degli ordini
        List<OrdineBean> storicoOrdini = ordineDAO.getOrdiniByUtente(utente.getIdUtente());
        request.setAttribute("storicoOrdini", storicoOrdini);

        // Recupera gli indirizzi salvati
        request.setAttribute("indirizzi", indirizzoDAO.getAllByUserId(utente.getIdUtente()));
        
        // Recupera i metodi di pagamento salvati
        request.setAttribute("pagamenti", pagamentoDAO.getAllByUserId(utente.getIdUtente()));

        // Inoltra alla pagina JSP della dashboard
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}