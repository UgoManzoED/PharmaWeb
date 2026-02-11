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
            <div class="checkout-error">
                ${error}
            </div>
        </c:if>

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
                                <button type="button" class="add-new-link" onclick="toggleAddressForm()">
                                    + Aggiungi nuovo indirizzo
                                </button>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-notice">
                                    <p>Non hai indirizzi salvati.</p>
                                    <button type="button" class="btn-primary" onclick="toggleAddressForm()">
                                        Aggiungi un indirizzo
                                    </button>
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
                                <button type="button" class="add-new-link" onclick="togglePaymentForm()">
                                    + Aggiungi nuovo metodo di pagamento
                                </button>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-notice">
                                    <p>Non hai metodi di pagamento salvati.</p>
                                    <button type="button" class="btn-primary" onclick="togglePaymentForm()">
                                        Aggiungi un metodo di pagamento
                                    </button>
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
                                    Hai <strong>${sessionScope.utente.puntiFedelta} punti</strong> disponibili
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
                                <span id="subtotal">
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

<!-- Form separati per aggiungere indirizzo e pagamento (FUORI dal form checkout) -->
<div id="addAddressForm" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); z-index: 1000; max-width: 500px; width: 90%;">
    <h3>Nuovo indirizzo</h3>
    <form id="addressForm" method="post" action="${pageContext.request.contextPath}/area-riservata/indirizzi">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        
        <div class="form-group">
            <label for="destinatario">Nome destinatario*</label>
            <input type="text" id="destinatario" name="destinatario" required>
        </div>
        
        <div class="form-group">
            <label for="via">Via e numero civico*</label>
            <input type="text" id="via" name="via" required>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="cap">CAP*</label>
                <input type="text" id="cap" name="cap" pattern="[0-9]{5}" maxlength="5" required>
            </div>
            
            <div class="form-group">
                <label for="citta">Città*</label>
                <input type="text" id="citta" name="citta" required>
            </div>
            
            <div class="form-group">
                <label for="provincia">Provincia*</label>
                <input type="text" id="provincia" name="provincia" maxlength="2" pattern="[A-Z]{2}" placeholder="ES: RM" required>
            </div>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn-primary">Salva indirizzo</button>
            <button type="button" class="btn-secondary" onclick="toggleAddressForm()">Annulla</button>
        </div>
    </form>
</div>

<div id="addPaymentForm" style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); z-index: 1000; max-width: 500px; width: 90%;">
    <h3>Nuovo metodo di pagamento</h3>
    <form id="paymentForm" method="post" action="${pageContext.request.contextPath}/area-riservata/pagamenti">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        
        <div class="form-group">
            <label for="tipo">Tipo carta*</label>
            <select id="tipo" name="tipo" required>
                <option value="">Seleziona tipo</option>
                <option value="Visa">Visa</option>
                <option value="Mastercard">Mastercard</option>
                <option value="Paypal">PayPal</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="titolare">Titolare carta*</label>
            <input type="text" id="titolare" name="titolare" required>
        </div>
        
        <div class="form-group">
            <label for="numero">Numero carta*</label>
            <input type="text" id="numero" name="numero" pattern="[0-9]{13,19}" maxlength="19" required>
        </div>
        
        <div class="form-row">
            <div class="form-group">
                <label for="mese">Mese scadenza*</label>
                <select id="mese" name="mese" required>
                    <option value="">MM</option>
                    <option value="1">01</option>
                    <option value="2">02</option>
                    <option value="3">03</option>
                    <option value="4">04</option>
                    <option value="5">05</option>
                    <option value="6">06</option>
                    <option value="7">07</option>
                    <option value="8">08</option>
                    <option value="9">09</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="anno">Anno scadenza*</label>
                <select id="anno" name="anno" required>
                    <option value="">AAAA</option>
                    <option value="2026">2026</option>
                    <option value="2027">2027</option>
                    <option value="2028">2028</option>
                    <option value="2029">2029</option>
                    <option value="2030">2030</option>
                    <option value="2031">2031</option>
                    <option value="2032">2032</option>
                    <option value="2033">2033</option>
                    <option value="2034">2034</option>
                    <option value="2035">2035</option>
                </select>
            </div>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn-primary">Salva metodo</button>
            <button type="button" class="btn-secondary" onclick="togglePaymentForm()">Annulla</button>
        </div>
    </form>
</div>

<div id="formOverlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999;" onclick="toggleAddressForm(); togglePaymentForm();"></div>

<script src="${pageContext.request.contextPath}/js/checkout.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>