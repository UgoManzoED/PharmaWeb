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