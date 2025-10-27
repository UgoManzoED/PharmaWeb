<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Lista Desideri - PharmaWeb" />
    <jsp:param name="pageCss" value="wishlist.css" />
</jsp:include>

<main class="wishlist-page">
    <div class="wishlist-container">
        <h1>La mia lista desideri</h1>
        
        <%-- Se ci sono prodotti nella wishlist mostra il contenuto, altrimenti mostra messaggio --%>
        <c:choose>
            <c:when test="${not empty sessionScope.wishlist and not empty sessionScope.wishlist.items}">
                <div class="wishlist-content">
                    <%-- Azioni globali sulla wishlist --%>
                    <div class="global-actions">
                        <form method="post" action="${pageContext.request.contextPath}/wishlist" 
                              style="display: inline;">
                            <input type="hidden" name="action" value="clear">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <button type="submit" class="clear-wishlist-btn" 
                                    onclick="return confirm('Sei sicuro di voler svuotare la lista desideri?')">
                                Svuota lista desideri
                            </button>
                        </form>
                        
                        <c:if test="${not empty sessionScope.utente}">
                            <form method="post" action="${pageContext.request.contextPath}/wishlist" 
                                  style="display: inline;">
                                <input type="hidden" name="action" value="moveAllToCart">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <button type="submit" class="move-all-to-cart-btn" 
                                        onclick="return confirm('Vuoi spostare tutti i prodotti disponibili nel carrello?')">
                                    Sposta tutto nel carrello
                                </button>
                            </form>
                        </c:if>
                    </div>
                    
                    <div class="wishlist-grid">
                        <%-- Ciclo per ogni prodotto nella wishlist --%>
                        <c:forEach var="prodotto" items="${sessionScope.wishlist.items}">
                            <div class="wishlist-item">
                                <div class="product-image-wrapper">
                                    <img src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" 
                                         alt="${prodotto.nomeProdotto}" 
                                         class="wishlist-product-image">
                                    <%-- Mostra badge sconto se presente --%>
                                    <c:if test="${prodotto.scontoPercentuale > 0}">
                                        <span class="discount-badge">-${prodotto.scontoPercentuale}%</span>
                                    </c:if>
                                </div>
                                
                                <div class="product-info">
                                    <h3 class="product-name">${prodotto.nomeProdotto}</h3>
                                    <p class="product-description">${prodotto.descrizione}</p>
                                    
                                    <%-- Mostra prezzo, se c'è sconto mostra anche prezzo originale --%>
                                    <div class="product-price">
                                        <c:if test="${prodotto.scontoPercentuale > 0}">
                                            <span class="original-price">
                                                <fmt:formatNumber value="${prodotto.prezzoDiListino}" 
                                                                    type="currency" 
                                                                    currencySymbol="€"/>
                                            </span>
                                            <span class="discounted-price">
                                                <fmt:formatNumber value="${prodotto.prezzoFinale}" 
                                                                    type="currency" 
                                                                    currencySymbol="€"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${prodotto.scontoPercentuale <= 0}">
                                            <span class="normal-price">
                                                <fmt:formatNumber value="${prodotto.prezzoFinale}" 
                                                                    type="currency" 
                                                                    currencySymbol="€"/>
                                            </span>
                                        </c:if>
                                    </div>
                                    
                                    <%-- Mostra se il prodotto è disponibile o meno --%>
                                    <div class="availability-status">
                                        <c:choose>
                                            <c:when test="${prodotto.quantitaDisponibile > 0}">
                                                <span class="in-stock">Disponibile</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="out-of-stock">Non disponibile</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                
                                <div class="product-actions">
                                    <%-- Form per rimuovere il prodotto dalla wishlist --%>
                                    <form method="post" action="${pageContext.request.contextPath}/wishlist" style="display: inline;">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${prodotto.idProdotto}">
                                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                        <button type="submit" class="remove-from-wishlist-btn">Rimuovi</button>
                                    </form>
                                    
                                    <%-- Aggiungi al carrello solo se il prodotto è disponibile --%>
                                    <c:if test="${prodotto.quantitaDisponibile > 0}">
                                        <form method="post" action="${pageContext.request.contextPath}/wishlist" style="display: inline;">
                                            <input type="hidden" name="action" value="moveToCart">
                                            <input type="hidden" name="productId" value="${prodotto.idProdotto}">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <button type="submit" class="move-to-cart-btn">Aggiungi al carrello</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="wishlist-info">
                        <p><strong>${sessionScope.wishlist.size} prodotto/i</strong> nella tua lista desideri</p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <%-- Messaggio e invito ad aggiungere prodotti se la wishlist è vuota --%>
                <div class="empty-wishlist">
                    <div class="empty-wishlist-icon">❤️</div>
                    <h2>La tua lista desideri è vuota</h2>
                    <p>Aggiungi i tuoi prodotti preferiti </p>
                    <a href="${pageContext.request.contextPath}/HomePage" class="continue-shopping-btn">
                        Continua lo shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>


<script src="${pageContext.request.contextPath}/js/wishlist.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>
