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
        // L'utente deve essere loggato per vedere questa pagina (controllato dal filtro)
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }

        IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
        MetodoPagamentoDAO pagamentoDAO = new MetodoPagamentoDAO();
        request.setAttribute("indirizzi", indirizzoDAO.getAllByUserId(utente.getIdUtente()));
        request.setAttribute("pagamenti", pagamentoDAO.getAllByUserId(utente.getIdUtente()));
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Processa l'ordine dopo che l'utente ha confermato il checkout.
     * Esegue tutte le operazioni in una transazione di database.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // --- VALIDAZIONE STOCK PRELIMINARE ---
        ProductDAO productDAO = new ProductDAO();
        for (CartItemBean item : cart.getItems()) {
            if (!productDAO.isAvailable(item.getProduct().getIdProdotto(), item.getQuantity())) {
                session.setAttribute("cartError", "Prodotto '" + item.getProduct().getNomeProdotto() + "' non più disponibile nella quantità richiesta. Rimuovilo o modifica la quantità.");
                response.sendRedirect(request.getContextPath() + "/cart.jsp");
                return;
            }
        }
        
        // --- RECUPERO DATI DAL FORM ---
        String puntiDaUsareStr = request.getParameter("puntiDaUsare");
        int puntiDaUsare = 0;
        if (puntiDaUsareStr != null && !puntiDaUsareStr.isEmpty()) {
            try {
                puntiDaUsare = Integer.parseInt(puntiDaUsareStr);
            } catch (NumberFormatException e) {
                // L'utente ha inserito un valore non numerico, ignoriamo
                puntiDaUsare = 0;
            }
        }
        // Sicurezza: non si possono usare più punti di quelli che si possiedono
        if (puntiDaUsare > utente.getPuntiFedelta() || puntiDaUsare < 0) {
            puntiDaUsare = 0;
        }

        // TODO: Recupera l'indirizzo e il metodo di pagamento scelti dall'utente
        // Esempio: String indirizzoScelto = "Via Roma 1...";
        String indirizzoScelto = "Indirizzo non implementato"; // Placeholder
        String pagamentoScelto = "Pagamento non implementato"; // Placeholder

        // --- CALCOLO LOGICA DI BUSINESS ---
        double subtotale = cart.getTotal();
        double scontoPunti = puntiDaUsare; // 1 punto = 1 euro
        double importoFinale = subtotale - scontoPunti;
        if(importoFinale < 0) importoFinale = 0; // Un ordine non può avere totale negativo

        int puntiGuadagnati = (int) Math.floor(importoFinale / 20); // 1 punto ogni 20 euro spesi

        Connection conn = null;
        try {
            // --- PREPARAZIONE OGGETTI PER IL DB ---
            OrdineBean ordine = new OrdineBean();
            ordine.setIdUtente(utente.getIdUtente());
            ordine.setDataOrdine(new Date());
            ordine.setImportoTotale(importoFinale);
            ordine.setIndirizzoSpedizione(indirizzoScelto);
            ordine.setMetodoPagamentoUtilizzato(pagamentoScelto);
            ordine.setStato("In elaborazione");
            ordine.setPuntiGuadagnati(puntiGuadagnati);
            ordine.setPuntiUtilizzati(puntiDaUsare);
            
            List<RigaOrdineBean> righe = new ArrayList<>();
            for (CartItemBean item : cart.getItems()) {
                righe.add(new RigaOrdineBean(item.getProduct(), item.getQuantity(), item.getProduct().getPrezzoFinale()));
            }
            ordine.setRighe(righe);

            // --- ESECUZIONE DELLA TRANSAZIONE ---
            conn = DriverManagerConnectionPool.getConnection();
            conn.setAutoCommit(false); // INIZIA TRANSAZIONE

            // 1. Salva ordine e righe
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

            // --- OPERAZIONI POST-SUCCESSO ---
            // Aggiorna l'oggetto utente nella sessione con il nuovo saldo punti
            utente.setPuntiFedelta(nuovoSaldoPunti);
            session.setAttribute("utente", utente);
            // Svuota il carrello
            session.removeAttribute("cart"); 
            
            // Reindirizza alla pagina di conferma
            response.sendRedirect(request.getContextPath() + "/ordine-confermato.jsp?id=" + ordine.getIdOrdine());

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // ANNULLA TUTTO IN CASO DI ERRORE
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.sendRedirect(request.getContextPath() + "/errore.jsp");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}