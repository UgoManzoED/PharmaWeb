<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Pagina non trovata - PharmaWeb" />
    <jsp:param name="pageCss" value="error.css" />
</jsp:include>

<main class="error-page">
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">
                <i class="fas fa-search"></i>
            </div>
            <h1 class="error-code">404</h1>
            <h2 class="error-title">Pagina non trovata</h2>
            <p class="error-message">
                La pagina che stai cercando non esiste, è stata rimossa o l'indirizzo inserito non è corretto.
            </p>
            
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn-primary">
                    Torna alla Home
                </a>
                <a href="${pageContext.request.contextPath}/catalogo" class="btn-secondary">
                    Vai al Catalogo
                </a>
            </div>
            
            <div class="error-help">
                <p>Potresti trovare quello che cerchi qui:</p>
                <ul class="help-links">
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo">Prodotti</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Carrello</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist">Wishlist</a></li>
                </ul>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>