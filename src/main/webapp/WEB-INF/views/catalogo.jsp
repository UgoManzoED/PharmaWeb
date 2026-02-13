<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="${pageTitle}" />
    <jsp:param name="pageCss" value="catalog.css" />
</jsp:include>

<main class="catalog-page">
    <div class="catalog-container">
        <!-- Breadcrumb e titolo -->
        <div class="catalog-header">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <span>&gt;</span>
                <span>Catalogo</span>
            </div>
            <h1>${pageTitle}</h1>
            <c:if test="${not empty searchQuery}">
                <p class="search-info">Hai cercato: <strong>"${searchQuery}"</strong></p>
            </c:if>
        </div>

        <div class="catalog-content">
            <!-- Sidebar con filtri -->
            <aside class="catalog-sidebar">
                <div class="filter-section">
                    <h3>Categorie</h3>
                    <ul class="category-list">
                        <li>
                            <a href="${pageContext.request.contextPath}/catalogo" 
                               class="category-link ${empty param.cat ? 'active' : ''}">
                                Tutti i Prodotti
                            </a>
                        </li>
                        
                        <%-- Categorie Dinamiche dal Database --%>
                        <c:if test="${not empty categories}">
                            <c:forEach var="cat" items="${categories}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/catalogo?cat=${cat.idCategoria}" 
                                       class="category-link ${param.cat == cat.idCategoria ? 'active' : ''}">
                                        ${cat.nome}
                                    </a>
                                </li>
                            </c:forEach>
                        </c:if>
                        
                    </ul>
                </div>

                <div class="filter-section">
                    <h3>Filtri</h3>
                    <div class="filter-group">
                        <label>
                            <input type="checkbox" id="filter-discount" class="filter-checkbox">
                            Solo prodotti in sconto
                        </label>
                    </div>
                    <div class="filter-group">
                        <label>
                            <input type="checkbox" id="filter-available" class="filter-checkbox">
                            Solo disponibili
                        </label>
                    </div>
                </div>

                <div class="filter-section">
                    <h3>Ordina per</h3>
                    <select id="sort-select" class="sort-select">
                        <option value="default">Predefinito</option>
                        <option value="price-asc">Prezzo: basso → alto</option>
                        <option value="price-desc">Prezzo: alto → basso</option>
                        <option value="name-asc">Nome: A → Z</option>
                        <option value="name-desc">Nome: Z → A</option>
                    </select>
                </div>
            </aside>

            <!-- Griglia prodotti -->
            <div class="catalog-main">
                <div class="catalog-info">
                    <p class="results-count">
                        <c:choose>
                            <c:when test="${not empty products}">
                                ${products.size()} prodotto/i trovato/i
                            </c:when>
                            <c:otherwise>
                                Nessun prodotto trovato
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <c:choose>
                    <c:when test="${not empty products}">
                        <div class="products-grid" id="products-grid">
                            <c:forEach var="prodotto" items="${products}">
                                <div class="product-card" 
                                     data-price="${prodotto.prezzoFinale}" 
                                     data-name="${prodotto.nomeProdotto}"
                                     data-discount="${prodotto.scontoPercentuale}"
                                     data-available="${prodotto.quantitaDisponibile}">
                                    
                                    <!-- Badge sconto -->
                                    <c:if test="${prodotto.scontoPercentuale > 0}">
                                        <span class="discount-badge">-${prodotto.scontoPercentuale}%</span>
                                    </c:if>
                                    
                                    <!-- Badge disponibilità -->
                                    <c:if test="${prodotto.quantitaDisponibile == 0}">
                                        <span class="out-of-stock-badge">Non disponibile</span>
                                    </c:if>
                                    
                                    <!-- Link al dettaglio prodotto -->
                                    <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.idProdotto}" class="product-link">
                                        <img class="product-image" 
                                             src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" 
                                             alt="${prodotto.nomeProdotto}"
                                             onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
                                        <h3 class="product-name">${prodotto.nomeProdotto}</h3>
                                    </a>
                                    
                                    <!-- Descrizione breve -->
                                    <p class="product-description-short">
                                        <c:choose>
                                            <c:when test="${prodotto.descrizione.length() > 80}">
                                                ${prodotto.descrizione.substring(0, 80)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${prodotto.descrizione}
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    
                                    <!-- Prezzo -->
                                    <div class="product-price">
                                        <c:if test="${prodotto.scontoPercentuale > 0}">
                                            <span class="original-price">
                                                <fmt:formatNumber value="${prodotto.prezzoDiListino}" type="currency" currencySymbol="€"/>
                                            </span>
                                            <span class="discounted-price">
                                                <fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${prodotto.scontoPercentuale <= 0}">
                                            <span class="normal-price">
                                                <fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </c:if>
                                    </div>
                                    
                                    <!-- Bottoni azione -->
                                    <div class="product-actions">
                                        <c:choose>
                                            <c:when test="${prodotto.quantitaDisponibile > 0}">
                                                <%-- Aggiunto tipo="button" per evitare submit involontari e onclick per la logica carrello --%>
                                                <button type="button" class="add-to-cart-button" data-product-id="${prodotto.idProdotto}">
                                                    Aggiungi al carrello
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="add-to-cart-button" disabled>
                                                    Non disponibile
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                        <button class="add-to-wishlist-button" data-product-id="${prodotto.idProdotto}">
                                            ❤️
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Messaggio nessun risultato -->
                        <div class="no-results">
                            <div class="no-results-icon">🔍</div>
                            <h2>Nessun prodotto trovato</h2>
                            <p>Prova a modificare i filtri o effettua una nuova ricerca</p>
                            <a href="${pageContext.request.contextPath}/catalogo" class="back-to-catalog-btn">
                                Visualizza tutti i prodotti
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/catalog.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>