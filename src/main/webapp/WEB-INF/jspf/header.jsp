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
            <span class="logo-text">PharmaWeb</span>
        </a>
    </div>

    <div class="search-container">
        <form action="${pageContext.request.contextPath}/catalogo" method="get" class="search-form">
            <%-- value con c:out per sicurezza --%>
            <input type="text" name="q" placeholder="Cerca prodotti..." 
                   value="<c:out value='${param.q}'/>" required>
            <button type="submit" class="search-button">🔍</button>
        </form>
    </div>

    <div class="actions-container">
        <%-- Logica Autenticazione --%>
        <c:choose>
            <c:when test="${not empty sessionScope.utente}">
                <div class="user-info">
                    <span>Benvenuto, <strong><c:out value="${sessionScope.utente.nome}"/></strong>!</span>
                    <a href="${pageContext.request.contextPath}/logout" class="login-button">LOGOUT</a>
                    <%-- Link Dashboard --%>
                    <a href="${pageContext.request.contextPath}/area-riservata/dashboard" class="user-link">Area Personale</a>
                </div>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" class="login-button">LOGIN ACCOUNT</a>
            </c:otherwise>
        </c:choose>

        <%-- Icone Wishlist e Carrello con contatori dinamici --%>
        <div class="nav-icons">
            <a href="${pageContext.request.contextPath}/wishlist" class="icon-link" title="La mia Wishlist">
                <div class="icon-placeholder wishlist">
                    <span class="icon-counter" id="wishlist-count">
                        ${not empty sessionScope.wishlist ? fn:length(sessionScope.wishlist.items) : 0}
                    </span>
                </div>
            </a>
            
            <a href="${pageContext.request.contextPath}/cart" class="icon-link" title="Il mio Carrello">
                <div class="icon-placeholder cart">
                    <span class="icon-counter" id="cart-count">
                        ${not empty sessionScope.cart ? fn:length(sessionScope.cart.items) : 0}
                    </span>
                </div>
            </a>
        </div>
    </div>
</header>