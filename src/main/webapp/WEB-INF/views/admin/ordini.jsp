<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Gestione Ordini - Admin" />
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
            <span>Storico Ordini Globale</span>
        </div>

        <h1>Storico Ordini Globale</h1>

        <!-- FILTRI DI RICERCA -->
        <section class="admin-form-section">
            <form action="${pageContext.request.contextPath}/admin/ordini" method="get" class="filter-form">
                <div class="form-grid">
                    <div class="field">
                        <label for="startDate">Dal (Data)</label>
                        <input type="date" id="startDate" name="startDate" value="<c:out value='${param.startDate}'/>">
                    </div>
                    <div class="field">
                        <label for="endDate">Al (Data)</label>
                        <input type="date" id="endDate" name="endDate" value="<c:out value='${param.endDate}'/>">
                    </div>
                    <div class="field">
                        <label for="email">Email Cliente</label>
                        <input type="text" id="email" name="email" placeholder="Cerca email..." value="<c:out value='${param.email}'/>">
                    </div>
                    <div class="field filter-actions" style="justify-content: flex-end; flex-direction: row; align-items: flex-end; gap: 10px;">
                        <button type="submit" class="btn-save">
                            <i class="fas fa-filter"></i> Filtra Ordini
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/ordini" class="btn-cancel" style="text-decoration:none;">
                            Reset
                        </a>
                    </div>
                </div>
            </form>
        </section>

        <!-- TABELLA RISULTATI -->
        <section class="admin-table-section">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Data e Ora</th>
                        <th>Email Cliente</th>
                        <th>Totale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="ord" items="${ordini}">
                        <tr>
                            <td>#<c:out value="${ord.idOrdine}"/></td>
                            <td>
                                <fmt:formatDate value="${ord.dataOrdine}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                            <td>
                            	<c:out value="${ord.emailCliente}" />
                            </td>
                            <td>
                                <strong>
                                    <fmt:formatNumber value="${ord.importoTotale}" type="currency" currencySymbol="€"/>
                                </strong>
                            </td>
                            <td>
                                <span class="status-badge ${ord.stato.toLowerCase().replace(' ', '-')}">
                                    <c:out value="${ord.stato}"/>
                                </span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ordine-confermato?id=${ord.idOrdine}" class="btn-outline-sm">
                                    <i class="fas fa-eye"></i> Vedi Dettagli
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <%-- Messaggio se non ci sono risultati --%>
            <c:if test="${empty ordini}">
                <div class="empty-state" style="text-align:center; padding:40px;">
                    <i class="fas fa-search" style="font-size: 3em; color: #ccc; margin-bottom: 15px; display: block;"></i>
                    <p>Nessun ordine trovato con i filtri selezionati.</p>
                </div>
            </c:if>
        </section>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>