<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="PharmaWeb - Home" />
    <jsp:param name="pageCss" value="home.css" />
</jsp:include>

<main>
    <!-- sezione categorie (per ora statica) -->
    <section class="categories-bar">
        <div class="category">CATEGORIA 1</div>
        <div class="category">CATEGORIA 2</div>
        <div class="category">CATEGORIA 3</div>
        <div class="category">CATEGORIA 4</div>
        <div class="category">CATEGORIA 5</div>
    </section>

    <!-- sezione prodotti scontati -->
    <section class="product-carousel">
        <h2>Offerte Imperdibili</h2>
        <div class="products-container">
            <!--Ciclo for per i prodotti scontati-->
            <c:forEach var="prodotto" items="${discountedProducts}">
                <div class="product-card">
                    <img class="product-image" src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" alt="${prodotto.nomeProdotto}">
                    <h3 class="product-name">${prodotto.nomeProdotto}</h3>
                    <div class="product-price">
                            <!-- controllo se c'è uno sconto -->
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
                    <button class="add-to-cart-button" data-product-id="${prodotto.idProdotto}">Aggiungi al carrello</button>
                    <button class="add-to-wishlist-button" data-product-id="${prodotto.idProdotto}">❤️</button>
                </div>
            </c:forEach>
        </div>
        <!-- frecce per lo scorrimento -->
        <div class="carousel-arrow prev">&lt;</div>
        <div class="carousel-arrow next">&gt;</div>
    </section>

    <!-- sezione nuovi prodotti -->
    <section class="product-carousel">
        <h2>Le nostre novità</h2>
        <div class="products-container">
            <c:forEach var="prodotto" items="${newProducts}">
                <div class="product-card">
                    <img class="product-image" src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" alt="${prodotto.nomeProdotto}">
                    <h3 class="product-name">${prodotto.nomeProdotto}</h3>
                    <div class="product-price">
                        <c:if test="${prodotto.scontoPercentuale > 0}">
                            <span class="original-price"><fmt:formatNumber value="${prodotto.prezzoDiListino}" type="currency" currencySymbol="€"/></span>
                            <span class="discounted-price"><fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/></span>
                        </c:if>
                        <c:if test="${prodotto.scontoPercentuale <= 0}">
                            <span class="normal-price"><fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/></span>
                        </c:if>
                    </div>
                    <button class="add-to-cart-button" data-product-id="${prodotto.idProdotto}">Aggiungi al carrello</button>
                    <button class="add-to-wishlist-button" data-product-id="${prodotto.idProdotto}">❤️</button>
                </div>
            </c:forEach>
        </div>
        <div class="carousel-arrow prev">&lt;</div>
        <div class="carousel-arrow next">&gt;</div>
    </section>

    <!-- Da aggiungere la sezione per i prodotti più venduti, copiando la struttura della sezione 'newProducts' e cambiando l'items in ${popularProducts} -->

<!-- sezione prodotti più venduti -->
<section class="product-carousel">
    <h2>I nostri prodotti più venduti</h2>
    <div class="products-container">
        <c:forEach var="prodotto" items="${popularProducts}">
            <div class="product-card">
                <img class="product-image" src="${pageContext.request.contextPath}/${prodotto.urlImmagine}" alt="${prodotto.nomeProdotto}">
                <h3 class="product-name">${prodotto.nomeProdotto}</h3>
                <div class="product-price">
                    <c:if test="${prodotto.scontoPercentuale > 0}">
                        <span class="original-price"><fmt:formatNumber value="${prodotto.prezzoDiListino}" type="currency" currencySymbol="€"/></span>
                        <span class="discounted-price"><fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/></span>
                    </c:if>
                    <c:if test="${prodotto.scontoPercentuale <= 0}">
                        <span class="normal-price"><fmt:formatNumber value="${prodotto.prezzoFinale}" type="currency" currencySymbol="€"/></span>
                    </c:if>
                </div>
                <button class="add-to-cart-button" data-product-id="${prodotto.idProdotto}">Aggiungi al carrello</button>
                <button class="add-to-wishlist-button" data-product-id="${prodotto.idProdotto}">❤️</button>
            </div>
        </c:forEach>
    </div>
    <div class="carousel-arrow prev">&lt;</div>
    <div class="carousel-arrow next">&gt;</div>
</section>
</main>

<%--inclusione footer--%>
<$@ include file="/WEB-INF/jspf/footer.jsp" %>


</body>
</html>
