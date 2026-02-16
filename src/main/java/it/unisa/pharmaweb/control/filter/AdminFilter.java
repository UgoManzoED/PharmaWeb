package it.unisa.pharmaweb.control.filter;

import it.unisa.pharmaweb.model.bean.UtenteBean;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro di sicurezza per l'area amministrativa.
 * Intercetta tutte le richieste verso /admin/* e verifica che l'utente:
 * 1. Sia loggato.
 * 2. Abbia il ruolo "admin".
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("utente") != null);
        boolean isAdmin = false;

        if (isLoggedIn) {
            UtenteBean utente = (UtenteBean) session.getAttribute("utente");
            // Controlla il ruolo
            if ("admin".equals(utente.getRuolo())) {
                isAdmin = true;
            }
        }

        if (isLoggedIn && isAdmin) {
            // È un admin loggato, può passare
            chain.doFilter(request, response);
        } else {
            // Non è autorizzato.
            // Se è loggato ma non è admin -> Errore 403 (Forbidden)
            // Se non è loggato -> Redirect al Login
            if (isLoggedIn) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato. Area riservata agli amministratori.");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            }
        }
    }
}