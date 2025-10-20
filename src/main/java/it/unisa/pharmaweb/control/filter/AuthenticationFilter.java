package it.unisa.pharmaweb.control.filter;

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
 * Questo filtro intercetta le richieste a tutte le risorse all'interno dell'area riservata
 * e controlla se l'utente è autenticato. In caso contrario, lo reindirizza alla pagina di login.
 * @author Ugo Manzo
 */
@WebFilter("/area-riservata/*")
public class AuthenticationFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {

        // Castiamo gli oggetti request e response ai loro tipi HTTP specifici
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Recuperiamo la sessione, ma senza crearne una nuova se non esiste (parametro false)
        HttpSession session = httpRequest.getSession(false);

        // Controlliamo se la sessione esiste E se contiene l'attributo "utente"
        boolean isLoggedIn = (session != null && session.getAttribute("utente") != null);

        if (isLoggedIn) {
            // L'utente è autenticato.
            // La richiesta prosegue verso la sua destinazione originale
            chain.doFilter(request, response);
        } else {
            // L'utente non è autenticato.
            // Reindirizziamolo alla pagina di login.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }
    
}