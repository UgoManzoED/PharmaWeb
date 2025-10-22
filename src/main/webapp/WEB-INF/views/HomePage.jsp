<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Importo la libreria JSTL Core per usare cicli e condizioni -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Importo la libreria JSTL Formatting per formattare i numeri (prezzi) -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Prima implementazione della homepage  -->

<!DOCTYPE html>
<html>
<head> 
    <!-- metadati e link al CSS -->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PharmaWeb - Home</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>

    <!-- header -->
<header class="main-header">
    <div class="logo-container">LOGO</div>
    <div class="search-container">
        <input type="text" placeholder="Ricerca">
    </div>
    <!-- placeholder di login wishlist e carrello -->
    <div class="actions-container">
        <button class="login-button">LOGIN ACCOUNT</button>
        <div class="icon-placeholder wishlist"></div>
        <div class="icon-placeholder cart"></div>
    </div>
</header>

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
                    <button class="add-to-cart-button">Aggiungi al carrello</button>
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
                    <button class="add-to-cart-button">Aggiungi al carrello</button>
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
                <button class="add-to-cart-button">Aggiungi al carrello</button>
            </div>
        </c:forEach>
    </div>
    <div class="carousel-arrow prev">&lt;</div>
    <div class="carousel-arrow next">&gt;</div>
</section>
</main>

</body>
</html>