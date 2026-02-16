<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Errore del server - PharmaWeb" />
    <jsp:param name="pageCss" value="error.css" />
</jsp:include>

<main class="error-page">
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">
                <i class="fas fa-exclamation-triangle"></i>
            </div>
            <h1 class="error-code">500</h1>
            <h2 class="error-title">Errore interno</h2>
            <p class="error-message">
                Si è verificato un problema tecnico imprevisto. I nostri farmacisti digitali sono già al lavoro per risolvere.
            </p>
            
            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/" class="btn-primary">
                    Torna alla Home
                </a>
                <button onclick="window.location.reload()" class="btn-secondary">
                    Riprova a caricare
                </button>
            </div>
            
            <div class="error-help">
                <p>Il problema è stato tracciato nei nostri log di sistema.</p>
                <p class="error-tip">Suggerimento: Se il problema persiste, prova a svuotare la cache del browser o a tornare tra qualche minuto.</p>
            </div>
            
            <%-- 
                In un vero ambiente di produzione questa sezione andrebbe nascosta 
                o mostrata solo agli amministratori.
            --%>
            <c:if test="${not empty requestScope['jakarta.servlet.error.exception']}">
                <details class="error-debug">
                    <summary>Informazioni tecniche per l'assistenza</summary>
                    <div class="debug-info">
                        <p><strong>Tipo:</strong> <c:out value="${requestScope['jakarta.servlet.error.exception_type']}"/></p>
                        <p><strong>Messaggio:</strong> <c:out value="${requestScope['jakarta.servlet.error.message']}"/></p>
                        <p><strong>Risorsa:</strong> <c:out value="${requestScope['jakarta.servlet.error.request_uri']}"/></p>
                        <c:if test="${not empty requestScope['jakarta.servlet.error.servlet_name']}">
                            <p><strong>Componente:</strong> <c:out value="${requestScope['jakarta.servlet.error.servlet_name']}"/></p>
                        </c:if>
                    </div>
                </details>
            </c:if>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>