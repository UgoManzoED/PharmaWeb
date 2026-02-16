<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Dashboard Amministratore - PharmaWeb" />
    <jsp:param name="pageCss" value="admin.css" />
</jsp:include>

<main class="admin-page">
    <div class="admin-container">
        
        <div class="dashboard-header">
            <h1>Pannello di Controllo</h1>
            <p>Benvenuto, <strong><c:out value="${sessionScope.utente.nome}"/></strong>. Da qui puoi gestire l'intera piattaforma.</p>
        </div>

        <div class="dashboard-grid">
            
            <!-- Card Gestione Prodotti -->
            <a href="${pageContext.request.contextPath}/admin/prodotti" class="dash-card">
                <div class="dash-icon">
                    <i class="fas fa-pills"></i>
                </div>
                <h2>Catalogo Prodotti</h2>
                <p>Aggiungi nuovi farmaci, modifica prezzi, gestisci lo stock e carica immagini.</p>
                <span class="btn-link">Vai al Catalogo &rarr;</span>
            </a>

            <!-- Card Gestione Ordini -->
            <a href="${pageContext.request.contextPath}/admin/ordini" class="dash-card">
                <div class="dash-icon">
                    <i class="fas fa-shipping-fast"></i>
                </div>
                <h2>Storico Ordini</h2>
                <p>Visualizza tutti gli ordini ricevuti, filtra per data o cerca ordini specifici dei clienti.</p>
                <span class="btn-link">Vedi Ordini &rarr;</span>
            </a>

            <!-- Card Gestione Utenti -->
            <a href="${pageContext.request.contextPath}/admin/utenti" class="dash-card">
                <div class="dash-icon">
                    <i class="fas fa-users-cog"></i>
                </div>
                <h2>Utenti Registrati</h2>
                <p>Visualizza la lista dei clienti iscritti e gestisci gli account amministratore.</p>
                <span class="btn-link">Gestisci Utenti &rarr;</span>
            </a>

        </div>

    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>