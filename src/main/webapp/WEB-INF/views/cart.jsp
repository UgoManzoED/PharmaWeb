<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Carrello - PharmaWeb" />
    <jsp:param name="pageCss" value="cart.css" />
</jsp:include>

<main class="cart-page">
    <div class="cart-container">
        <h1>Il mio carrello</h1>

        <%-- Se ci sono articoli nel carrello mostra la tabella, altrimenti mostra messaggio carrello vuoto --%>
        <c:choose>
            <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
                <div class="cart-content">
                    <div class="cart-items">
                        <table class="cart-table">
                            <thead>
                                <tr>
                                    <th>Prodotto</th>
                                    <th>Quantità</th>
                                    <th>Prezzo unitario</th>
                                    <th>Subtotale</th>
                                    <th>Azioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%-- Ciclo su ogni articolo presente nel carrello --%>
                                <c:forEach var="item" items="${sessionScope.cart.items}">
                                    <tr class="cart-item-row">
                                        <td class="product-info">
                                            <%-- Immagine e dettagli del prodotto --%>
                                            <img src="${pageContext.request.contextPath}/${item.product.urlImmagine}" 
                                                 alt="${item.product.nomeProdotto}" 
                                                 class="product-image-small">
                                            <div class="product-details">
                                                <h3>${item.product.nomeProdotto}</h3>
                                                <p class="product-description">${item.product.descrizione}</p>
                                            </div>
                                        </td>
                                        <td class="quantity-cell">
                                            <%-- Form per aggiornare la quantità di un singolo prodotto --%>
                                            <form method="post" action="${pageContext.request.contextPath}/cart" 
                                                  class="quantity-form">
                                                <input type="hidden" name="action" value="update">
                                                <input type="hidden" name="productId" value="${item.product.idProdotto}">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                
                                                <%-- Pulsante per diminuire la quantità --%>
                                                <button 
                                                    type="button" 
                                                    class="quantity-btn decrease-btn"
                                                    onclick="updateQty('${item.product.idProdotto}', -1, '${item.product.quantitaDisponibile}')"
                                                >−</button>
                                                
                                                <%-- Campo input per mostrare la quantità corrente --%>
                                                <input type="number" 
                                                       name="quantity" 
                                                       value="${item.quantity}" 
                                                       min="1" 
                                                       max="${item.product.quantitaDisponibile}"
                                                       id="qty-${item.product.idProdotto}"
                                                       class="quantity-input" 
                                                       readonly>
                                                
                                                <%-- Pulsante per aumentare la quantità --%>
                                                <button 
                                                    type="button" 
                                                    class="quantity-btn increase-btn"
                                                    onclick="updateQty('${item.product.idProdotto}', 1, '${item.product.quantitaDisponibile}')"
                                                >+</button>
                                            <%-- Quantità disponibile a magazzino --%>
                                            <span class="available-quantity">Disponibili: ${item.product.quantitaDisponibile}</span>
                                        </td>
                                        <td class="price-cell">
                                            <%-- Prezzo unitario del prodotto --%>
                                            <fmt:formatNumber value="${item.product.prezzoFinale}" 
                                                            type="currency" 
                                                            currencySymbol="€" />
                                        </td>
                                        <td class="subtotal-cell">
                                            <%-- Subtotale relativo a questo articolo --%>
                                            <fmt:formatNumber value="${item.subtotal}" 
                                                            type="currency" 
                                                            currencySymbol="€" />
                                        </td>
                                        <td class="actions-cell">
                                            <%-- Form per rimuovere il prodotto dal carrello --%>
                                            <form method="post" action="${pageContext.request.contextPath}/cart" 
                                                  style="display: inline;">
                                                <input type="hidden" name="action" value="remove">
                                                <input type="hidden" name="productId" value="${item.product.idProdotto}">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                <button type="submit" class="remove-btn">Rimuovi</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                        <div class="cart-actions">
                            <%-- Pulsante per svuotare l'intero carrello --%>
                            <form method="post" action="${pageContext.request.contextPath}/cart" 
                                  onsubmit="return confirm('Sei sicuro di voler svuotare il carrello?')">
                                <input type="hidden" name="action" value="clear">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <button type="submit" class="clear-cart-btn">Svuota carrello</button>
                            </form>
                        </div>
                    </div>
                    
                    <%-- Riepilogo dell'ordine nella colonna laterale --%>
                    <div class="cart-summary">
                        <div class="summary-card">
                            <h2>Riepilogo ordine</h2>
                            <div class="summary-details">
                                <div class="summary-row">
                                    <span>Articoli: <strong>${sessionScope.cart.items.size()}</strong></span>
                                </div>
                                <div class="summary-divider"></div>
                                <div class="summary-row total">
                                    <span>Totale:</span>
                                    <span class="final-total">
                                        <fmt:formatNumber value="${sessionScope.cart.total}" 
                                                        type="currency" 
                                                        currencySymbol="€" />
                                    </span>
                                </div>
                            </div>
                            <%-- Mostra bottone checkout solo se l'utente è loggato, altrimenti chiede il login --%>
                            <c:choose>
                                <c:when test="${not empty sessionScope.utente}">
                                    <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn">
                                        Procedi all'ordine
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/login" class="checkout-btn">
                                        Accedi per ordinare
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <%-- Messaggio e invito all'acquisto se il carrello è vuoto --%>
                <div class="empty-cart">
                    <div class="empty-cart-icon">🛒</div>
                    <h2>Il tuo carrello è vuoto</h2>
                    <p>Inizia ad aggiungere prodotti dal nostro catalogo!</p>
                    <a href="${pageContext.request.contextPath}/HomePage" class="continue-shopping-btn">
                        Continua lo shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>


<script src="${pageContext.request.contextPath}/js/cart.js"></script>


<%@ include file="/WEB-INF/jspf/footer.jsp" %>
