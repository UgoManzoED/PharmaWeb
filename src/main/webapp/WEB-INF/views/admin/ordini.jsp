<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Gestione Ordini - Admin" />
    <jsp:param name="pageCss" value="admin.css" />
</jsp:include>

<main class="admin-page">
    <h1>Storico Ordini Globale</h1>

    <!-- FILTRI DI RICERCA -->
    <section class="admin-form-section">
        <form action="ordini" method="get" class="filter-form">
            <div class="form-grid">
                <div class="field">
                    <label>Dal (Data)</label>
                    <input type="date" name="startDate" value="${param.startDate}">
                </div>
                <div class="field">
                    <label>Al (Data)</label>
                    <input type="date" name="endDate" value="${param.endDate}">
                </div>
                <div class="field">
                    <label>Email Cliente</label>
                    <input type="text" name="email" placeholder="Esempio: mario@rossi.it" value="${param.email}">
                </div>
                <div class="field" style="justify-content: flex-end;">
                    <button type="submit" class="btn-save">Filtra Ordini</button>
                    <a href="ordini" class="btn-cancel" style="margin-left:10px; text-decoration:none;">Reset</a>
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
                <c:forEach var="ord" items="${listaOrdini}">
                    <tr>
                        <td>#${ord.idOrdine}</td>
                        <td><fmt:formatDate value="${ord.dataOrdine}" pattern="dd/MM/yyyy HH:mm" /></td>
                        <td><c:out value="${requestScope['clienteEmail_'.concat(ord.idOrdine)]}" /></td>
                        <td><strong><fmt:formatNumber value="${ord.importoTotale}" type="currency" currencySymbol="€"/></strong></td>
                        <td><span class="order-status-badge">${ord.stato}</span></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/ordine-confermato?id=${ord.idOrdine}" class="btn-outline-sm">Vedi Dettagli</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <c:if test="${empty listaOrdini}">
            <p style="text-align:center; padding:20px;">Nessun ordine trovato con i filtri selezionati.</p>
        </c:if>
    </section>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>