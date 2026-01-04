package it.unisa.pharmaweb.control;

import it.unisa.pharmaweb.model.bean.*;
import it.unisa.pharmaweb.model.dao.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/area-riservata/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la visualizzazione della pagina di riepilogo del checkout.
     * Prepara i dati necessari (carrello, indirizzi, pagamenti) e li inoltra alla JSP.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/WEB-INF/views/cart.jsp");
            return;
        }

        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
        
        request.setAttribute("indirizzi", indirizzoDAO.getAllByUserId(utente.getIdUtente()));
        request.setAttribute("pagamenti", pagamentoDAO.getAllByUserId(utente.getIdUtente()));
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/checkout.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String indirizzoIdStr = request.getParameter("indirizzoId");
        String pagamentoIdStr = request.getParameter("pagamentoId");
        String puntiDaUsareStr = request.getParameter("puntiDaUsare");

        if (indirizzoIdStr == null || indirizzoIdStr.trim().isEmpty() || 
            pagamentoIdStr == null || pagamentoIdStr.trim().isEmpty()) {
            
            request.setAttribute("error", "Per favore, seleziona un indirizzo e un metodo di pagamento.");
            doGet(request, response);
            return;
        }

        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
        
        IndirizzoBean indirizzoScelto = indirizzoDAO.getById(Integer.parseInt(indirizzoIdStr));
        MetodoPagamentoBean pagamentoScelto = pagamentoDAO.getById(Integer.parseInt(pagamentoIdStr));

        if (indirizzoScelto == null || indirizzoScelto.getIdUtente() != utente.getIdUtente() ||
            pagamentoScelto == null || pagamentoScelto.getIdUtente() != utente.getIdUtente()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato ai dati.");
            return;
        }

        String indirizzoCompleto = String.format("%s, %s, %s %s (%s)", 
                                    indirizzoScelto.getNomeDestinatario(), indirizzoScelto.getVia(), 
                                    indirizzoScelto.getCap(), indirizzoScelto.getCitta(), indirizzoScelto.getProvincia());
        String infoPagamento = String.format("%s terminante in %s", 
                                    pagamentoScelto.getTipoCarta(), pagamentoScelto.getUltime4Cifre());

        // Validazione stock
        ProductDAO productDAO = new ProductDAO();
        for (CartItemBean item : cart.getItems()) {
            if (!productDAO.isAvailable(item.getProduct().getIdProdotto(), item.getQuantity())) {
                session.setAttribute("cartError", "Prodotto '" + item.getProduct().getNomeProdotto() + "' non più disponibile.");
                response.sendRedirect(request.getContextPath() + "/WEB-INF/views/cart.jsp");
                return;
            }
        }
        
        int puntiDaUsare = (puntiDaUsareStr != null && !puntiDaUsareStr.isEmpty()) ? Integer.parseInt(puntiDaUsareStr) : 0;
        if (puntiDaUsare > utente.getPuntiFedelta() || puntiDaUsare < 0) {
            puntiDaUsare = 0;
        }

        double importoFinale = cart.getTotal() - puntiDaUsare;
        if(importoFinale < 0) importoFinale = 0;
        int puntiGuadagnati = (int) Math.floor(importoFinale / 20);

        Connection conn = null;
        try {
            conn = DriverManagerConnectionPool.getConnection();
            conn.setAutoCommit(false); // INIZIA TRANSAZIONE

            OrdineBean ordine = new OrdineBean();
            ordine.setIdUtente(utente.getIdUtente());
            ordine.setDataOrdine(new Date());
            ordine.setImportoTotale(importoFinale);
            ordine.setIndirizzoSpedizione(indirizzoCompleto);
            ordine.setMetodoPagamentoUtilizzato(infoPagamento);
            ordine.setStato("In elaborazione");
            ordine.setPuntiGuadagnati(puntiGuadagnati);
            ordine.setPuntiUtilizzati(puntiDaUsare);
            
            List<RigaOrdineBean> righe = new ArrayList<>();
            for (CartItemBean item : cart.getItems()) {
                righe.add(new RigaOrdineBean(item.getProduct(), item.getQuantity(), item.getProduct().getPrezzoFinale()));
            }
            ordine.setRighe(righe);

            // 1. Salva ordine
            OrdineDAO ordineDAO = new OrdineDAO();
            ordineDAO.saveOrdine(conn, ordine);

            // 2. Aggiorna stock
            for (CartItemBean item : cart.getItems()) {
                productDAO.decreaseStock(conn, item.getProduct().getIdProdotto(), item.getQuantity());
            }

            // 3. Aggiorna punti utente
            int nuovoSaldoPunti = utente.getPuntiFedelta() - puntiDaUsare + puntiGuadagnati;
            UtenteDAO utenteDAO = new UtenteDAO();
            utenteDAO.updatePuntiFedelta(conn, utente.getIdUtente(), nuovoSaldoPunti);

            conn.commit(); // CONFERMA TRANSAZIONE

            // Aggiorna utente in sessione e svuota carrello
            utente.setPuntiFedelta(nuovoSaldoPunti);
            session.setAttribute("utente", utente);
            session.removeAttribute("cart");
            
            response.sendRedirect(request.getContextPath() + "/ordine-confermato.jsp?id=" + ordine.getIdOrdine());

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            response.sendRedirect(request.getContextPath() + "/WEB-INF/views/errore500.jsp");
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}