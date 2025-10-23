package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.bean.UtenteBean;
import it.unisa.pharmaweb.model.dao.IndirizzoDAO;
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

        // Recupera lo storico degli ordini dell'utente
        OrdineDAO ordineDAO = new OrdineDAO();
        List<OrdineBean> storicoOrdini = ordineDAO.getOrdiniByUtente(utente.getIdUtente());

        // Aggiungi lo storico alla richiesta per passarlo alla JSP
        request.setAttribute("storicoOrdini", storicoOrdini);
        
        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        request.setAttribute("indirizzi", indirizzoDAO.getAllByUserId(utente.getIdUtente()));

        // Inoltra alla pagina JSP della dashboard
        RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}