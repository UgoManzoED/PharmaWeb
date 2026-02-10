package it.unisa.pharmaweb.control.admin;

import it.unisa.pharmaweb.model.bean.OrdineBean;
import it.unisa.pharmaweb.model.dao.OrdineDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/ordini")
public class AdminOrdersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String email = request.getParameter("email");

        OrdineDAO ordineDAO = new OrdineDAO();
        List<OrdineBean> ordini = ordineDAO.getAllOrdini(startDate, endDate, email);

        request.setAttribute("ordini", ordini);
        
        // Passiamo i parametri indietro per mantenere i filtri nel form
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("email", email);

        request.getRequestDispatcher("/WEB-INF/views/admin/ordini.jsp").forward(request, response);
    }
}