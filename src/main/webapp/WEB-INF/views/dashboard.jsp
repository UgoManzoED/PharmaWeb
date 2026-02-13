<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Area Personale - PharmaWeb" />
    <jsp:param name="pageCss" value="dashboard.css" />
</jsp:include>

<main class="dashboard-page">
    <div class="dashboard-container">
        <aside class="dashboard-sidebar">
            <div class="user-profile-brief">
                <div class="user-avatar"><i class="fas fa-user-circle"></i></div>
                <h3><c:out value="${sessionScope.utente.nome} ${sessionScope.utente.cognome}"/></h3>
                <p class="user-email"><c:out value="${sessionScope.utente.email}"/></p>
                <div class="points-badge">
                    <i class="fas fa-coins"></i> ${sessionScope.utente.puntiFedelta} Punti
                </div>
            </div>
            
            <nav class="dashboard-nav">
                <a href="#ordini" class="nav-link active" data-tab="ordini">
                    <i class="fas fa-box"></i> I miei ordini
                </a>
                <a href="#indirizzi" class="nav-link" data-tab="indirizzi">
                    <i class="fas fa-map-marker-alt"></i> Indirizzi salvati
                </a>
                <a href="#pagamenti" class="nav-link" data-tab="pagamenti">
                    <i class="fas fa-credit-card"></i> Metodi di pagamento
                </a>
            </nav>
        </aside>

        <section class="dashboard-content">
            
            <!-- TAB 1: STORICO ORDINI -->
            <div id="tab-ordini" class="tab-content active">
                <h2>I miei ordini</h2>
                <c:choose>
                    <c:when test="${not empty storicoOrdini}">
                        <div class="orders-list">
                            <c:forEach var="ordine" items="${storicoOrdini}">
                                <div class="order-card">
                                    <div class="order-header">
                                        <span>Ordine #<strong>${ordine.idOrdine}</strong></span>
                                        <span>Data: <fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy"/></span>
                                        <span class="order-status">${ordine.stato}</span>
                                    </div>
                                    <div class="order-body">
                                        <p>Spedito a: <em>${ordine.indirizzoSpedizione}</em></p>
                                        <div class="order-footer">
                                            <strong>Totale: <fmt:formatNumber value="${ordine.importoTotale}" type="currency" currencySymbol="€"/></strong>
                                            <a href="ordine-confermato?id=${ordine.idOrdine}" class="btn-outline-sm">Dettagli</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="empty-msg">Non hai ancora effettuato ordini.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- TAB 2: INDIRIZZI -->
            <div id="tab-indirizzi" class="tab-content">
                <h2>Indirizzi di spedizione</h2>
                <div class="address-grid">
                    <c:forEach var="addr" items="${indirizzi}">
                        <div class="address-item-card">
                            <strong>${addr.nomeDestinatario}</strong>
                            <p>${addr.via}<br>${addr.cap} ${addr.citta} (${addr.provincia})</p>
                            <form action="${pageContext.request.contextPath}/area-riservata/indirizzi" method="post">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="idIndirizzo" value="${addr.idIndirizzo}">
                                <button type="submit" class="btn-delete" onclick="return confirm('Eliminare questo indirizzo?')">Elimina</button>
                            </form>
                        </div>
                    </c:forEach>
                    
                    <!-- Form aggiunta indirizzo -->
                    <div class="add-new-card">
                        <h3>Aggiungi Nuovo</h3>
                        <form action="${pageContext.request.contextPath}/area-riservata/indirizzi" method="post" class="mini-form">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="add">
                            <input type="text" name="destinatario" placeholder="Nome Destinatario" required>
                            <input type="text" name="via" placeholder="Via e Numero" required>
                            <div class="form-row">
                                <input type="text" name="cap" placeholder="CAP" maxlength="5" required>
                                <input type="text" name="citta" placeholder="Città" required>
                            </div>
                            <input type="text" name="provincia" placeholder="Provincia (es. NA)" maxlength="2" required>
                            <button type="submit" class="btn-save">Salva Indirizzo</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- TAB 3: PAGAMENTI -->
            <div id="tab-pagamenti" class="tab-content">
                <h2>Metodi di pagamento</h2>
                <div class="payment-grid">
                    <c:forEach var="pay" items="${pagamenti}">
                        <div class="payment-item-card">
                            <div class="card-brand"><i class="fas fa-credit-card"></i> ${pay.tipoCarta}</div>
                            <p>**** **** **** ${pay.ultime4Cifre}</p>
                            <small>Scadenza: ${pay.meseScadenza}/${pay.annoScadenza}</small>
                            <form action="${pageContext.request.contextPath}/area-riservata/pagamenti" method="post">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="idMetodo" value="${pay.idMetodo}">
                                <button type="submit" class="btn-delete">Rimuovi</button>
                            </form>
                        </div>
                    </c:forEach>

                    <!-- Form aggiunta pagamento -->
                    <div class="add-new-card">
                        <h3>Aggiungi Carta</h3>
                        <form action="${pageContext.request.contextPath}/area-riservata/pagamenti" method="post" class="mini-form">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="add">
                            <select name="tipo" required>
                                <option value="Visa">Visa</option>
                                <option value="Mastercard">Mastercard</option>
                                <option value="Paypal">PayPal</option>
                            </select>
                            <input type="text" name="titolare" placeholder="Titolare della carta" required>
                            <input type="text" name="numero" placeholder="Numero Carta (16 cifre)" maxlength="16" required>
                            <div class="form-row">
                                <input type="number" name="mese" placeholder="MM" min="1" max="12" required>
                                <input type="number" name="anno" placeholder="AAAA" min="2025" required>
                            </div>
                            <button type="submit" class="btn-save">Salva Carta</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
<%@ include file="/WEB-INF/jspf/footer.jsp" %>