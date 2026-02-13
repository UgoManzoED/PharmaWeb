<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <%-- Token CSRF per chiamate AJAX (es. carrello/wishlist in home.js) --%>
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    
    <title>
        <c:out value="${not empty param.pageTitle ? param.pageTitle : 'PharmaWeb - La tua farmacia online'}" />
    </title>
    
    <%-- Font Awesome per le Icone --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <%-- CSS Principale --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    
    <%-- Caricamento condizionale del CSS della pagina specifica --%>
    <c:if test="${not empty param.pageCss}">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/${param.pageCss}">
    </c:if>
</head>
<body>

<header class="main-header">
    <div class="logo-container">
        <a href="${pageContext.request.contextPath}/">
            <%-- In Futuro Logo --%>
            <i class="fas fa-mortar-pestle"></i>
            <span class="logo-text">PharmaWeb</span>
        </a>
    </div>

    <div class="search-container">
        <form action="${pageContext.request.contextPath}/catalogo" method="get" class="search-form">
            <input type="text" name="q" placeholder="Cerca farmaci, integratori..." 
                   value="<c:out value='${param.q}'/>" required>
            <button type="submit" class="search-button">
                <i class="fas fa-search"></i>
            </button>
        </form>
    </div>

    <div class="actions-container">
        <c:choose>
            <c:when test="${not empty sessionScope.utente}">
                <div class="user-menu">
                    <span class="welcome-msg">Ciao, <strong><c:out value="${sessionScope.utente.nome}"/></strong></span>
                    <div class="user-links">
                        <a href="${pageContext.request.contextPath}/area-riservata/dashboard">Il mio account</a>
                        <a href="${pageContext.request.contextPath}/logout" class="logout-link">Esci</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="login-button">
                    <i class="fas fa-user"></i> Accedi
                </a>
            </c:otherwise>
        </c:choose>

        <div class="nav-icons">
            <%-- Wishlist --%>
            <a href="${pageContext.request.contextPath}/wishlist" class="icon-link" title="Wishlist">
                <i class="far fa-heart"></i>
                <span class="icon-counter" id="wishlist-count">
                    ${not empty sessionScope.wishlist ? fn:length(sessionScope.wishlist.items) : 0}
                </span>
            </a>
            
            <%-- Carrello --%>
            <a href="${pageContext.request.contextPath}/cart" class="icon-link" title="Carrello">
                <i class="fas fa-shopping-basket"></i>
                <span class="icon-counter" id="cart-count">
                    ${not empty sessionScope.cart ? fn:length(sessionScope.cart.items) : 0}
                </span>
            </a>
        </div>
    </div>
</header>