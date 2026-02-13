<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="${product.nomeProdotto} - PharmaWeb" />
    <jsp:param name="pageCss" value="product-detail.css" />
</jsp:include>

<main class="product-detail-page">
    <div class="product-detail-container">
        
        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Home</a>
            <span>&gt;</span>
            <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a>
            <c:if test="${not empty product.nomeCategoria}">
                <span>&gt;</span>
                <a href="${pageContext.request.contextPath}/catalogo?cat=${product.idCategoria}">
                    <c:out value="${product.nomeCategoria}"/>
                </a>
            </c:if>
            <span>&gt;</span>
            <span><c:out value="${product.nomeProdotto}"/></span>
        </div>

        <!-- Sezione principale prodotto -->
        <div class="product-main-section">
            
            <!-- Colonna immagine -->
            <div class="product-image-section">
                <div class="product-image-container">
                    <c:if test="${product.scontoPercentuale > 0}">
                        <span class="discount-badge-large">-${product.scontoPercentuale}%</span>
                    </c:if>
                    <img src="${pageContext.request.contextPath}/${product.urlImmagine}" 
                         alt="${product.nomeProdotto}" 
                         class="product-image-large"
                         onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
                </div>
            </div>

            <!-- Colonna informazioni -->
            <div class="product-info-section">
                <h1 class="product-title"><c:out value="${product.nomeProdotto}"/></h1>
                
                <c:if test="${not empty product.nomeCategoria}">
                    <div class="product-category">
                        <span class="category-label">Categoria:</span>
                        <a href="${pageContext.request.contextPath}/catalogo?cat=${product.idCategoria}" class="category-link">
                            <c:out value="${product.nomeCategoria}"/>
                        </a>
                    </div>
                </c:if>

                <!-- Prezzo -->
                <div class="product-price-section">
                    <c:choose>
                        <c:when test="${product.scontoPercentuale > 0}">
                            <div class="price-with-discount">
                                <span class="original-price-large">
                                    <fmt:formatNumber value="${product.prezzoDiListino}" type="currency" currencySymbol="€"/>
                                </span>
                                <span class="discounted-price-large">
                                    <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                                </span>
                                <span class="savings-text">
                                    Risparmi <fmt:formatNumber value="${product.prezzoDiListino - product.prezzoFinale}" type="currency" currencySymbol="€"/>
                                </span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <span class="normal-price-large">
                                <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Disponibilità -->
                <div class="availability-section">
                    <c:choose>
                        <c:when test="${product.quantitaDisponibile > 10}">
                            <span class="availability in-stock">✓ Disponibile</span>
                            <span class="stock-info">(${product.quantitaDisponibile} pezzi disponibili)</span>
                        </c:when>
                        <c:when test="${product.quantitaDisponibile > 0}">
                            <span class="availability low-stock">⚠ Ultimi ${product.quantitaDisponibile} pezzi</span>
                        </c:when>
                        <c:otherwise>
                            <span class="availability out-of-stock">✗ Non disponibile</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Azioni -->
                <div class="product-actions-section">
                    <c:choose>
                        <c:when test="${product.quantitaDisponibile > 0}">
                            <button type="button" class="add-to-cart-btn-large" data-product-id="${product.idProdotto}">
                                🛒 Aggiungi al carrello
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="add-to-cart-btn-large" disabled>
                                Non disponibile
                            </button>
                        </c:otherwise>
                    </c:choose>
                    
                    <button type="button" class="add-to-wishlist-btn-large" data-product-id="${product.idProdotto}">
                        ❤️ Aggiungi ai preferiti
                    </button>
                </div>

                <!-- Descrizione -->
                <div class="product-description-section">
                    <h2>Descrizione</h2>
                    <p class="product-description-text"><c:out value="${product.descrizione}"/></p>
                </div>
            </div>
        </div>

        <!-- Sezione recensioni -->
        <div class="reviews-section">
            <h2 class="reviews-title">Recensioni clienti</h2>
            
            <c:choose>
                <c:when test="${not empty recensioni}">
                    <div class="reviews-list">
                        <c:forEach var="recensione" items="${recensioni}">
                            <div class="review-card">
                                <div class="review-header">
                                    <div class="review-author">
                                        <%-- Good Practice: XSS Prevention con c:out --%>
                                        <span class="author-name"><c:out value="${recensione.nomeUtente}"/></span>
                                        <span class="review-date">
                                            <fmt:formatDate value="${recensione.dataRecensione}" pattern="dd/MM/yyyy" />
                                        </span>
                                    </div>
                                    <div class="review-rating">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= recensione.voto}">
                                                    <span class="star filled">★</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="star empty">☆</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        <span class="rating-number">(${recensione.voto}/5)</span>
                                    </div>
                                </div>
                                <c:if test="${not empty recensione.testo}">
                                    <p class="review-text"><c:out value="${recensione.testo}"/></p>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="no-reviews">
                        <p>Nessuna recensione ancora. Sii il primo a recensire questo prodotto!</p>
                    </div>
                </c:otherwise>
            </c:choose>

            <!-- Form per aggiungere recensione -->
            <c:if test="${canReview}">
                <div class="add-review-section">
                    <h3>Lascia una recensione</h3>
                    
                    <c:if test="${not empty error}">
                        <div class="review-error">${error}</div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/prodotto" method="post" class="review-form" id="reviewForm">
                        <input type="hidden" name="idProdotto" value="${product.idProdotto}">
                        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        
                        <div class="form-group">
                            <label for="voto">Valutazione *</label>
                            <div class="star-rating-input">
                                <input type="radio" name="voto" value="5" id="star5" required>
                                <label for="star5">★</label>
                                <input type="radio" name="voto" value="4" id="star4">
                                <label for="star4">★</label>
                                <input type="radio" name="voto" value="3" id="star3">
                                <label for="star3">★</label>
                                <input type="radio" name="voto" value="2" id="star2">
                                <label for="star2">★</label>
                                <input type="radio" name="voto" value="1" id="star1">
                                <label for="star1">★</label>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="testo">Commento (facoltativo)</label>
                            <textarea name="testo" id="reviewText" rows="5" 
                                      placeholder="Condividi la tua esperienza con questo prodotto..."
                                      maxlength="1000"></textarea>
                            <span class="char-count">0/1000</span>
                        </div>
                        
                        <button type="submit" class="submit-review-btn">Invia recensione</button>
                    </form>
                </div>
            </c:if>
            
            <c:if test="${not canReview and not empty sessionScope.utente}">
                <div class="cannot-review-notice">
                    <p>Puoi recensire questo prodotto solo dopo averlo acquistato.</p>
                </div>
            </c:if>
            
            <c:if test="${empty sessionScope.utente}">
                <div class="login-to-review-notice">
                    <p>
                        <a href="${pageContext.request.contextPath}/login">Accedi</a> 
                        per lasciare una recensione.
                    </p>
                </div>
            </c:if>
        </div>

    </div>
</main>

<script src="${pageContext.request.contextPath}/js/product-detail.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>