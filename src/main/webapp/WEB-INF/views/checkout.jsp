<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Checkout - PharmaWeb" />
    <jsp:param name="pageCss" value="checkout.css" />
</jsp:include>

<main class="checkout-page">
    <div class="checkout-container">
        <h1>Completa il tuo ordine</h1>

        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Home</a>
            <span>&gt;</span>
            <a href="${pageContext.request.contextPath}/cart">Carrello</a>
            <span>&gt;</span>
            <span>Checkout</span>
        </div>

        <!-- Messaggio di errore -->
        <c:if test="${not empty error}">
            <div class="checkout-error" style="background-color: #ffebee; color: #c62828; padding: 15px; margin-bottom: 20px; border-radius: 4px; border: 1px solid #ef9a9a;">
                ${error}
            </div>
        </c:if>

        <%-- L'action deve puntare a /area-riservata/checkout come definito nella Servlet --%>
        <form action="${pageContext.request.contextPath}/area-riservata/checkout" method="post" id="checkoutForm" class="checkout-form">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div class="checkout-content">
                
                <!-- Colonna sinistra: Indirizzi e Pagamenti -->
                <div class="checkout-left">
                    
                    <!-- Sezione Indirizzo Spedizione -->
                    <div class="checkout-section">
                        <h2>1. Indirizzo di spedizione</h2>
                        
                        <c:choose>
                            <c:when test="${not empty indirizzi}">
                                <div class="address-list">
                                    <c:forEach var="indirizzo" items="${indirizzi}">
                                        <label class="address-card">
                                            <input type="radio" 
                                                   name="indirizzoId" 
                                                   value="${indirizzo.idIndirizzo}" 
                                                   required
                                                   ${indirizzo.idIndirizzo == param.indirizzoId ? 'checked' : ''}>
                                            <div class="address-content">
                                                <div class="address-name">${indirizzo.nomeDestinatario}</div>
                                                <div class="address-details">
                                                    ${indirizzo.via}<br>
                                                    ${indirizzo.cap} ${indirizzo.citta} (${indirizzo.provincia})
                                                </div>
                                            </div>
                                        </label>
                                    </c:forEach>
                                </div>
                                <a href="${pageContext.request.contextPath}/area-riservata/dashboard#indirizzi" class="add-new-link">
                                    + Aggiungi nuovo indirizzo
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-notice">
                                    <p>Non hai indirizzi salvati.</p>
                                    <a href="${pageContext.request.contextPath}/area-riservata/dashboard#indirizzi" class="btn-primary">
                                        Aggiungi un indirizzo
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Sezione Metodo Pagamento -->
                    <div class="checkout-section">
                        <h2>2. Metodo di pagamento</h2>
                        
                        <c:choose>
                            <c:when test="${not empty pagamenti}">
                                <div class="payment-list">
                                    <c:forEach var="pagamento" items="${pagamenti}">
                                        <label class="payment-card">
                                            <input type="radio" 
                                                   name="pagamentoId" 
                                                   value="${pagamento.idMetodo}" 
                                                   required
                                                   ${pagamento.idMetodo == param.pagamentoId ? 'checked' : ''}>
                                            <div class="payment-content">
                                                <div class="payment-type">
                                                    <c:choose>
                                                        <c:when test="${pagamento.tipoCarta == 'Visa'}">
                                                            <span class="card-icon visa">💳 VISA</span>
                                                        </c:when>
                                                        <c:when test="${pagamento.tipoCarta == 'Mastercard'}">
                                                            <span class="card-icon mastercard">💳 Mastercard</span>
                                                        </c:when>
                                                        <c:when test="${pagamento.tipoCarta == 'Paypal'}">
                                                            <span class="card-icon paypal">💰 PayPal</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="card-icon generic">💳 Carta</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="payment-details">
                                                    <div class="payment-holder">${pagamento.titolare}</div>
                                                    <div class="payment-number">**** **** **** ${pagamento.ultime4Cifre}</div>
                                                    <div class="payment-expiry">Scadenza: ${pagamento.meseScadenza}/${pagamento.annoScadenza}</div>
                                                </div>
                                            </div>
                                        </label>
                                    </c:forEach>
                                </div>
                                <a href="${pageContext.request.contextPath}/area-riservata/dashboard#pagamenti" class="add-new-link">
                                    + Aggiungi nuovo metodo di pagamento
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-notice">
                                    <p>Non hai metodi di pagamento salvati.</p>
                                    <a href="${pageContext.request.contextPath}/area-riservata/dashboard#pagamenti" class="btn-primary">
                                        Aggiungi un metodo di pagamento
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Sezione Punti Fedeltà -->
                    <c:if test="${sessionScope.utente.puntiFedelta > 0}">
                        <div class="checkout-section">
                            <h2>3. Punti Fedeltà</h2>
                            <div class="loyalty-points-section">
                                <p class="points-available">
                                    <%-- Aggiunto id per recupero facile via JS --%>
                                    Hai <strong id="user-points-display">${sessionScope.utente.puntiFedelta}</strong> punti disponibili
                                    <span class="points-info">(1 punto = 1€)</span>
                                </p>
                                
                                <div class="points-input-group">
                                    <label for="puntiDaUsare">Punti da utilizzare:</label>
                                    <input type="number" 
                                           id="puntiDaUsare" 
                                           name="puntiDaUsare" 
                                           min="0" 
                                           max="${sessionScope.utente.puntiFedelta}"
                                           value="0"
                                           class="points-input">
                                    <button type="button" id="useAllPoints" class="use-all-btn">Usa tutti</button>
                                </div>
                                
                                <p class="points-discount" id="pointsDiscount">Sconto: 0,00€</p>
                            </div>
                        </div>
                    </c:if>

                </div>

                <!-- Colonna destra: Riepilogo Ordine -->
                <div class="checkout-right">
                    <div class="order-summary">
                        <h2>Riepilogo ordine</h2>
                        
                        <!-- Lista prodotti -->
                        <div class="order-items">
                            <c:forEach var="item" items="${sessionScope.cart.items}">
                                <div class="order-item">
                                    <img src="${pageContext.request.contextPath}/${item.product.urlImmagine}" 
                                         alt="${item.product.nomeProdotto}" 
                                         class="order-item-image">
                                    <div class="order-item-details">
                                        <div class="order-item-name">${item.product.nomeProdotto}</div>
                                        <div class="order-item-quantity">Quantità: ${item.quantity}</div>
                                        <div class="order-item-price">
                                            <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="€"/>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Totali -->
                        <div class="order-totals">
                            <div class="total-row">
                                <span>Subtotale:</span>
                                <%-- Aggiunto data-value per leggere il numero grezzo in JS --%>
                                <span id="subtotal" data-value="${sessionScope.cart.total}">
                                    <fmt:formatNumber value="${sessionScope.cart.total}" type="currency" currencySymbol="€"/>
                                </span>
                            </div>
                            
                            <div class="total-row" id="pointsDiscountRow" style="display: none;">
                                <span>Sconto punti:</span>
                                <span id="pointsDiscountAmount" class="discount-text">-0,00€</span>
                            </div>
                            
                            <div class="total-row shipping">
                                <span>Spedizione:</span>
                                <span>Gratuita</span>
                            </div>
                            
                            <div class="total-divider"></div>
                            
                            <div class="total-row final">
                                <span>Totale:</span>
                                <span id="finalTotal" class="final-total">
                                    <fmt:formatNumber value="${sessionScope.cart.total}" type="currency" currencySymbol="€"/>
                                </span>
                            </div>
                        </div>

                        <!-- Bottone conferma -->
                        <button type="submit" class="confirm-order-btn" id="confirmOrderBtn">
                            Conferma ordine
                        </button>

                        <p class="secure-payment">
                            🔒 Pagamento sicuro e protetto
                        </p>
                    </div>
                </div>

            </div>
        </form>

    </div>
</main>

<script src="${pageContext.request.contextPath}/js/checkout.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>