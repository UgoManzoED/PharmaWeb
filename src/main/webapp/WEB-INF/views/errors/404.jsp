<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Pagina non trovata - PharmaWeb" />
    <jsp:param name="pageCss" value="error.css" />
</jsp:include>

<main class="error-page">
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">🔍</div>
            <h1 class="error-code">404</h1>
            <h2 class="error-title">Pagina non trovata</h2>
            <p class="error-message">
                La pagina che stai cercando non esiste o è stata spostata.
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
                <p>Hai bisogno di aiuto?</p>
                <ul class="help-links">
                    <li><a href="${pageContext.request.contextPath}/">Pagina principale</a></li>
                    <li><a href="${pageContext.request.contextPath}/catalogo">Catalogo prodotti</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Il mio carrello</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist">Lista desideri</a></li>
                </ul>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>
