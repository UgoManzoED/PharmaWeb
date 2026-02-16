package it.unisa.pharmaweb.control.filter;

import java.io.IOException;

import it.unisa.pharmaweb.model.dao.RecensioneDAO;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

public class GlobalDataFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // Carichiamo le recensioni solo per le richieste GET (pagine)
        if (req.getMethod().equalsIgnoreCase("GET")) {
            RecensioneDAO dao = new RecensioneDAO();
            request.setAttribute("latestReviews", dao.getLatestReviews(3));
        }
        chain.doFilter(request, response);
    }
}