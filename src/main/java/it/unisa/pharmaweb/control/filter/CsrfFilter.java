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
@WebFilter("/*") // Intercetta tutte le richieste all'applicazione
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
            // I token non corrispondono, richiesta CSRF non valida
            
        	// Controllo Richiesta AJAX
        	if("XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"))
        			|| httpRequest.getHeader("Accept") != null
        			&& httpRequest.getHeader("Accept").contains("application/json")) {
        		// Rispondo con JSON
        		httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        		httpResponse.setContentType("application/json; charset=UTF-8");
        		httpResponse.getWriter().write("{\"success\":false,\"error\":\"Errore di sicurezza. Ricarica la pagina.\"}");
        		httpResponse.getWriter().flush();
        	} else {
        		httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Richiesta CSRF non valida.");
        	}
        } else {
            // I token corrispondono, la richiesta è legittima, genera un nuovo token per la prossima richiesta
        	String newCsrfToken = UUID.randomUUID().toString();
        	httpRequest.getSession().setAttribute("csrfToken", newCsrfToken);
            chain.doFilter(request, response);
        }
    }
}