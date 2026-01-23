<%--
  Created by IntelliJ IDEA.
  User: Er Kebab
  Date: 23/10/2025
  Time: 23:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Importo la libreria JSTL Core per usare cicli e condizioni -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Importo la libreria JSTL Formatting per formattare i numeri (prezzi) -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Seconda implementazione della homepage  -->

<!DOCTYPE html>
<html>
<head>
  <!-- metadati e link al CSS -->
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="csrf-token" content="${sessionScope.csrfToken}">
  <title>${not empty param.pageTitle ? param.pageTitle : 'PharmaWeb'}</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
  <c:if test="${not empty param.pageCss}">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/${param.pageCss}">
  </c:if>
</head>
<body>

<!-- header -->
<header class="main-header">
  <div class="logo-container">
    <a href="${pageContext.request.contextPath}/">LOGO</a>
  </div>
  <div class="search-container">
    <form action="${pageContext.request.contextPath}/catalogo" method="get" class="search-form">
      <input type="text" name="q" placeholder="Cerca prodotti..." value="${param.q}" required>
      <button type="submit" class="search-button">🔍</button>
    </form>
  </div>
  <!-- seconda implementazione di carrello e wishlist -->
  <div class="actions-container">
    <c:choose>
      <c:when test="${not empty sessionScope.utente}">
        <span>Benvenuto, ${sessionScope.utente.nome}!</span>
        <a href="${pageContext.request.contextPath}/logout" class="login-button">LOGOUT</a>
      </c:when>
      <c:otherwise>
        <a href="${pageContext.request.contextPath}/login" class="login-button">LOGIN ACCOUNT</a>
      </c:otherwise>
    </c:choose>

    <!-- Icone Wishlist e Carrello -->
    <a href="${pageContext.request.contextPath}/wishlist" class="icon-link">
      <div class="icon-placeholder wishlist">
        <span class="icon-counter" id="wishlist-count">
          ${not empty sessionScope.wishlist ? sessionScope.wishlist.size : 0}
        </span>
      </div>
    </a>
    <a href="${pageContext.request.contextPath}/cart" class="icon-link">
      <div class="icon-placeholder cart">
        <span class="icon-counter" id="cart-count">
          ${not empty sessionScope.cart ? sessionScope.cart.items.size() : 0}
        </span>
      </div>
    </a>
  </div>
</header>