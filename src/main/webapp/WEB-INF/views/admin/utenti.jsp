<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Gestione Utenti - Admin" />
    <jsp:param name="pageCss" value="admin.css" />
</jsp:include>

<main class="admin-page">
    <div class="admin-container">
        
        <!-- Breadcrumb -->
        <div class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Home</a>
            <span>&gt;</span>
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <span>&gt;</span>
            <span>Gestione Utenti</span>
        </div>

        <div class="admin-header-actions">
            <h1>Gestione Utenti Registrati</h1>
            <p class="results-count">Totale iscritti: <strong>${utenti.size()}</strong></p>
        </div>

        <%-- Messaggi di errore o successo --%>
        <c:if test="${not empty error}">
            <div class="alert-error" style="background: #fee; color: #c00; padding: 15px; border-radius: 4px; margin-bottom: 20px; border: 1px solid #fcc;">
                <i class="fas fa-exclamation-triangle"></i> ${error}
            </div>
        </c:if>

        <section class="admin-table-section">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nominativo</th>
                        <th>Email</th>
                        <th>Ruolo</th>
                        <th>Punti Fedeltà</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${utenti}">
                        <tr class="${u.idUtente == sessionScope.utente.idUtente ? 'row-current-user' : ''}">
                            <td>#<c:out value="${u.idUtente}"/></td>
                            <td>
                                <strong><c:out value="${u.cognome} ${u.nome}"/></strong>
                                <c:if test="${u.idUtente == sessionScope.utente.idUtente}">
                                    <span class="badge-self">(Tu)</span>
                                </c:if>
                            </td>
                            <td><c:out value="${u.email}"/></td>
                            <td>
                                <span class="role-badge ${u.ruolo}">
                                    <c:out value="${u.ruolo}"/>
                                </span>
                            </td>
                            <td>
                                <i class="fas fa-coins" style="color: #ffc107;"></i> 
                                <c:out value="${u.puntiFedelta}"/>
                            </td>
                            <td>
                                <c:choose>
                                    <%-- Impedisce di mostrare il tasto elimina per se stessi --%>
                                    <c:when test="${u.idUtente != sessionScope.utente.idUtente}">
                                        <form action="${pageContext.request.contextPath}/admin/utenti" method="post" 
                                              onsubmit="return confirm('Sei sicuro di voler eliminare l\'utente ${u.email}? L\'azione è irreversibile.')">
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${u.idUtente}">
                                            <button type="submit" class="btn-delete-sm" title="Elimina Utente">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted" style="font-size: 0.8em; font-style: italic;">In uso</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty utenti}">
                <div class="empty-state">
                    <p>Non ci sono utenti registrati nel sistema.</p>
                </div>
            </c:if>
        </section>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/admin-validation.js"></script>
<%@ include file="/WEB-INF/jspf/footer.jsp" %>