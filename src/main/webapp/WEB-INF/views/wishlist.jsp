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
        
        <%-- GESTIONE ERRORI: Mostra messaggi provenienti dal backend (es. prodotto esaurito durante spostamento) --%>
        <c:if test="${not empty sessionScope.wishlistError}">
            <div class="alert-error" role="alert" aria-live="assertive" style="background-color: #ffebee; color: #c62828; padding: 15px; margin-bottom: 20px; border-radius: 4px; border: 1px solid #ef9a9a;">
                <i class="fas fa-exclamation-circle"></i> <c:out value="${sessionScope.wishlistError}" />
            </div>
            <%-- Rimuovi l'errore dalla sessione dopo averlo mostrato per non farlo ricomparire al refresh --%>
            <c:remove var="wishlistError" scope="session"/>
        </c:if>

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
                                    onclick="return confirm('Sei sicuro di voler svuotare completamente la tua lista desideri?')">
                                <i class="fas fa-trash-sweep"></i> Svuota lista desideri
                            </button>
                        </form>
                        
                        <%-- Permetti lo spostamento massivo solo se l'utente è loggato --%>
                        <c:if test="${not empty sessionScope.utente}">
                            <form method="post" action="${pageContext.request.contextPath}/wishlist" 
                                  style="display: inline;">
                                <input type="hidden" name="action" value="moveAllToCart">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <button type="submit" class="move-all-to-cart-btn" 
                                        onclick="return confirm('Vuoi spostare tutti i prodotti attualmente disponibili nel carrello?')">
                                    <i class="fas fa-cart-arrow-down"></i> Sposta tutto nel carrello
                                </button>
                            </form>
                        </c:if>
                    </div>
                    
                    <div class="wishlist-grid">
                        <%-- Ciclo per ogni prodotto presente nella wishlist --%>
                        <c:forEach var="prodotto" items="${sessionScope.wishlist.items}">
                            <div class="wishlist-item card">
                                <div class="product-image-wrapper">
                                    <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.idProdotto}">
                                        <img src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" 
                                             alt="<c:out value='${prodotto.nomeProdotto}'/>" 
                                             class="wishlist-product-image"
                                             onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
                                    </a>
                                    
                                    <%-- Badge sconto evidenziato --%>
                                    <c:if test="${prodotto.scontoPercentuale > 0}">
                                        <span class="discount-badge">-${prodotto.scontoPercentuale}%</span>
                                    </c:if>
                                </div>
                                
                                <div class="product-info">
                                    <%-- Good Practice: XSS Prevention tramite c:out --%>
                                    <h3 class="product-name">
                                        <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.idProdotto}">
                                            <c:out value="${prodotto.nomeProdotto}"/>
                                        </a>
                                    </h3>
                                    <p class="product-description"><c:out value="${prodotto.descrizione}"/></p>
                                    
                                    <%-- Visualizzazione Prezzo con gestione Sconto --%>
                                    <div class="product-price">
                                        <c:choose>
                                            <c:when test="${prodotto.scontoPercentuale > 0}">
                                                <span class="original-price" aria-label="Prezzo originale">
                                                    <fmt:formatNumber value="${prodotto.prezzoDiListino}" 
                                                                        type="currency" 
                                                                        currencySymbol="€"/>
                                                </span>
                                                <span class="discounted-price" aria-label="Prezzo scontato">
                                                    <fmt:formatNumber value="${prodotto.prezzoFinale}" 
                                                                        type="currency" 
                                                                        currencySymbol="€"/>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="normal-price">
                                                    <fmt:formatNumber value="${prodotto.prezzoFinale}" 
                                                                        type="currency" 
                                                                        currencySymbol="€"/>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    
                                    <%-- Stato disponibilità in tempo reale --%>
                                    <div class="availability-status">
                                        <c:choose>
                                            <c:when test="${prodotto.quantitaDisponibile > 0}">
                                                <span class="status-tag in-stock"><i class="fas fa-check"></i> Disponibile</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-tag out-of-stock"><i class="fas fa-times"></i> Non disponibile</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                
                                <div class="product-actions">
                                    <%-- Form per rimuovere il singolo prodotto dalla wishlist --%>
                                    <form method="post" action="${pageContext.request.contextPath}/wishlist" class="action-form">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${prodotto.idProdotto}">
                                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                        <button type="submit" class="remove-from-wishlist-btn" title="Rimuovi dalla lista">
                                            <i class="fas fa-heart-broken"></i> Rimuovi
                                        </button>
                                    </form>
                                    
                                    <%-- Permetti lo spostamento al carrello solo se il prodotto è effettivamente disponibile --%>
                                    <c:if test="${prodotto.quantitaDisponibile > 0}">
                                        <form method="post" action="${pageContext.request.contextPath}/wishlist" class="action-form">
                                            <input type="hidden" name="action" value="moveToCart">
                                            <input type="hidden" name="productId" value="${prodotto.idProdotto}">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <button type="submit" class="move-to-cart-btn">
                                                <i class="fas fa-cart-plus"></i> Sposta nel carrello
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <div class="wishlist-summary-info">
                        <p>Hai salvato <strong>${sessionScope.wishlist.size} prodotto/i</strong> nella tua lista.</p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <%-- Visualizzazione stato vuoto --%>
                <div class="empty-wishlist-state">
                    <div class="empty-icon">❤️</div>
                    <h2>La tua lista desideri è vuota</h2>
                    <p>Salva qui i prodotti che ti interessano per ritrovarli più facilmente o acquistarli in seguito.</p>
                    <a href="${pageContext.request.contextPath}/" class="continue-shopping-btn">
                        <i class="fas fa-arrow-left"></i> Continua lo shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/wishlist.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>