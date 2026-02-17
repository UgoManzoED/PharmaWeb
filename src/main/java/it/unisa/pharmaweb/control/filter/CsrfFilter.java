package it.unisa.pharmaweb.control.filter;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Questo filtro implementa il pattern "Synchronizer Token" per la protezione da attacchi CSRF.
 * Per le richieste GET, genera un token, lo salva in sessione e lo rende disponibile per le viste.
 * Per le richieste POST, valida il token ricevuto dal form contro quello in sessione.
 */
public class CsrfFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // --- GESTIONE TOKEN PER TUTTE LE RICHIESTE NON-POST ---
        // Se non è una richiesta POST, ci assicuriamo che un token esista in sessione per i futuri form.
        if (!httpRequest.getMethod().equalsIgnoreCase("POST")) {
            // Se la sessione non ha un token, ne creiamo uno
            if (httpRequest.getSession().getAttribute("csrfToken") == null) {
                String csrfToken = UUID.randomUUID().toString();
                httpRequest.getSession().setAttribute("csrfToken", csrfToken);
            }
            // Lascia che la richiesta prosegua normalmente
            chain.doFilter(request, response);
            return;
        }

        // --- VALIDAZIONE TOKEN PER LE SOLE RICHIESTE POST ---
        String sessionToken = (String) httpRequest.getSession().getAttribute("csrfToken");
        String requestToken = httpRequest.getParameter("csrfToken");

        if (sessionToken == null || requestToken == null || !sessionToken.equals(requestToken)) {
        
            String nextToken = UUID.randomUUID().toString();
            httpRequest.getSession().setAttribute("csrfToken", nextToken);

            // Controllo Richiesta AJAX
            if ("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With")) || 
                (httpRequest.getHeader("Accept") != null && httpRequest.getHeader("Accept").contains("application/json"))) {
                
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.getWriter().write("{\"success\":false,\"error\":\"Sessione scaduta o sicurezza non valida. Riprova.\",\"csrfToken\":\"" + nextToken + "\"}");
                httpResponse.getWriter().flush();
            } else {
                // Per i form normali, mandiamo alla 403. .
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Richiesta CSRF non valida.");
            }
        } else {
            // Ruotiamo il token per la prossima operazione
            String nextToken = UUID.randomUUID().toString();
            httpRequest.getSession().setAttribute("csrfToken", nextToken);
            chain.doFilter(request, response);
        }
    }
}