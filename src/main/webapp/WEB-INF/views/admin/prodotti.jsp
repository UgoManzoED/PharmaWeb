<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Gestione Catalogo - Admin" />
    <jsp:param name="pageCss" value="admin.css" />
</jsp:include>

<main class="admin-page">
    <h1>Gestione Catalogo Prodotti</h1>

    <!-- FORM DI INSERIMENTO / MODIFICA -->
    <section class="admin-form-section">
        <h2>${not empty productToEdit ? 'Modifica Prodotto' : 'Aggiungi Nuovo Prodotto'}</h2>
        <form action="prodotti" method="post" enctype="multipart/form-data" class="admin-form">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="action" value="${not empty productToEdit ? 'update' : 'save'}">
            <c:if test="${not empty productToEdit}">
                <input type="hidden" name="id" value="${productToEdit.idProdotto}">
                <input type="hidden" name="oldImmagine" value="${productToEdit.urlImmagine}">
            </c:if>

            <div class="form-grid">
                <div class="field">
                    <label>Nome Prodotto</label>
                    <input type="text" name="nome" value="${productToEdit.nomeProdotto}" required>
                </div>
                <div class="field">
                    <label>Categoria</label>
                    <select name="idCategoria" required>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.idCategoria}" ${productToEdit.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                ${cat.nome}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label>Prezzo (€)</label>
                    <input type="number" name="prezzo" step="0.01" value="${productToEdit.prezzoDiListino}" required>
                </div>
                <div class="field">
                    <label>Sconto (%)</label>
                    <input type="number" name="sconto" min="0" max="99" value="${productToEdit.scontoPercentuale != null ? productToEdit.scontoPercentuale : 0}">
                </div>
                <div class="field">
                    <label>Quantità Stock</label>
                    <input type="number" name="quantita" value="${productToEdit.quantitaDisponibile}" required>
                </div>
                <div class="field">
                    <label>Immagine ${not empty productToEdit ? '(Lascia vuoto per non cambiare)' : '*'}</label>
                    <input type="file" name="immagine" accept="image/*" ${empty productToEdit ? 'required' : ''}>
                </div>
            </div>
            
            <div class="field full-width">
                <label>Descrizione</label>
                <textarea name="descrizione" rows="4" required>${productToEdit.descrizione}</textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-save">Salva Prodotto</button>
                <c:if test="${not empty productToEdit}">
                    <a href="prodotti" class="btn-cancel">Annulla</a>
                </c:if>
            </div>
        </form>
    </section>

    <!-- TABELLA PRODOTTI -->
    <section class="admin-table-section">
        <h2>Prodotti in Catalogo</h2>
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Foto</th>
                    <th>Nome</th>
                    <th>Prezzo</th>
                    <th>Stock</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td><img src="${pageContext.request.contextPath}/${p.urlImmagine}" width="50"></td>
                        <td>${p.nomeProdotto}</td>
                        <td>€ ${p.prezzoFinale}</td>
                        <td>
                            <span class="${p.quantitaDisponibile < 10 ? 'low-stock' : ''}">
                                ${p.quantitaDisponibile}
                            </span>
                        </td>
                        <td>
                            <a href="prodotti?action=edit&id=${p.idProdotto}" class="btn-edit-sm">📝</a>
                            <form action="prodotti" method="post" style="display:inline;" onsubmit="return confirm('Eliminare definitivamente?')">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${p.idProdotto}">
                                <button type="submit" class="btn-delete-sm">🗑️</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </section>
</main>

<script src="${pageContext.request.contextPath}/js/admin-validation.js"></script>
<%@ include file="/WEB-INF/jspf/footer.jsp" %>