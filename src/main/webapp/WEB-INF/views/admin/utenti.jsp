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
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard Admin</a>
            <span>&gt;</span>
            <span>Gestione Utenti</span>
        </div>

        <div class="admin-header-actions">
            <h1>Gestione Utenti Registrati</h1>
            <p class="results-count">Totale iscritti nel sistema: <strong>${utenti.size()}</strong></p>
        </div>

        <%-- GESTIONE ERRORI/FEEDBACK --%>
        <c:if test="${not empty error}">
            <div class="alert-error" role="alert">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${error}"/>
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
                                    <span class="badge-self"> (Tu)</span>
                                </c:if>
                            </td>
                            <td><c:out value="${u.email}"/></td>
                            <td>
                                <span class="role-badge ${u.ruolo.toLowerCase()}">
                                    <c:out value="${u.ruolo}"/>
                                </span>
                            </td>
                            <td>
                                <i class="fas fa-coins" style="color: var(--accent-yellow);"></i> 
                                <c:out value="${u.puntiFedelta}"/>
                            </td>
                            <td>
                                <c:choose>
                                    <%-- Impedisce la visualizzazione del tasto elimina per l'utente in sessione --%>
                                    <c:when test="${u.idUtente != sessionScope.utente.idUtente}">
                                        <form action="${pageContext.request.contextPath}/admin/utenti" method="post" 
                                              onsubmit="return confirm('Sei sicuro di voler eliminare l\'utente <c:out value="${u.email}"/>? Questa operazione cancellerà anche i suoi indirizzi e metodi di pagamento.')">
                                            
                                            <%-- Token CSRF obbligatorio per il filtro --%>
                                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${u.idUtente}">
                                            
                                            <button type="submit" class="btn-delete-sm" title="Elimina Utente">
                                                <i class="fas fa-trash-alt"></i> Elimina
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted" style="font-size: 0.85em; font-style: italic;">Sessione attiva</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <%-- Messaggio se non ci sono utenti --%>
            <c:if test="${empty utenti}">
                <div class="empty-state">
                    <i class="fas fa-users-slash" style="font-size: 3em; color: #ccc; margin-bottom: 10px; display: block;"></i>
                    <p>Nessun utente registrato trovato nel database.</p>
                </div>
            </c:if>
        </section>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/admin-validation.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>