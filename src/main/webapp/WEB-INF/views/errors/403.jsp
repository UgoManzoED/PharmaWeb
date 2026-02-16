<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Accesso negato - PharmaWeb" />
    <jsp:param name="pageCss" value="error.css" />
</jsp:include>

<main class="error-page">
    <div class="error-container">
        <div class="error-content">
            <div class="error-icon">
                <i class="fas fa-user-shield"></i>
            </div>
            <h1 class="error-code">403</h1>
            <h2 class="error-title">Accesso negato</h2>
            <p class="error-message">
                Spiacenti, non hai i permessi necessari per accedere a questa risorsa o la tua sessione è scaduta.
            </p>
            
            <div class="error-actions">
                <c:choose>
                    <%-- Se l'utente non è loggato, suggeriamo il login --%>
                    <c:when test="${empty sessionScope.utente}">
                        <a href="${pageContext.request.contextPath}/login" class="btn-primary">
                            Effettua il login
                        </a>
                        <a href="${pageContext.request.contextPath}/" class="btn-secondary">
                            Torna alla Home
                        </a>
                    </c:when>
                    <%-- Se è loggato ma ha provato ad accedere a pagine admin o altrui --%>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/" class="btn-primary">
                            Torna alla Home
                        </a>
                        <a href="${pageContext.request.contextPath}/area-riservata/dashboard" class="btn-secondary">
                            La mia Dashboard
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="error-help">
                <p>Se ritieni che questo sia un errore del sistema, contatta il supporto tecnico.</p>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>
