<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Errore del server - PharmaWeb" />
    <jsp:param name="pageCss" value="error.css" />
</jsp:include>

<main class="error-page">
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">⚠️</div>
            <h1 class="error-code">500</h1>
            <h2 class="error-title">Errore interno del server</h2>
            <p class="error-message">
                Si è verificato un errore imprevisto. Ci scusiamo per il disagio.
            </p>
            
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn-primary">
                    Torna alla Home
                </a>
                <button onclick="window.location.reload()" class="btn-secondary">
                    Ricarica la pagina
                </button>
            </div>
            
            <div class="error-help">
                <p>Il problema è stato segnalato al nostro team tecnico.</p>
                <p class="error-tip">Suggerimento: Prova a ricaricare la pagina o torna più tardi.</p>
            </div>
            
            <!-- Debug info (solo in sviluppo) -->
            <c:if test="${not empty pageContext.errorData.throwable}">
                <details class="error-debug">
                    <summary>Dettagli tecnici</summary>
                    <div class="debug-info">
                        <p><strong>Tipo:</strong> ${pageContext.errorData.throwable.class.name}</p>
                        <p><strong>Messaggio:</strong> ${pageContext.errorData.throwable.message}</p>
                        <c:if test="${not empty pageContext.errorData.requestURI}">
                            <p><strong>URI:</strong> ${pageContext.errorData.requestURI}</p>
                        </c:if>
                    </div>
                </details>
            </c:if>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>
