<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Gestione Catalogo - Admin" />
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
            <span>Gestione Catalogo</span>
        </div>

        <h1>Gestione Catalogo Prodotti</h1>

        <!-- FORM DI INSERIMENTO / MODIFICA -->
        <section class="admin-form-section" id="form-prodotto">
            <h2>
                <c:choose>
                    <c:when test="${not empty productToEdit}">Modifica Prodotto: <c:out value="${productToEdit.nomeProdotto}"/></c:when>
                    <c:otherwise>Aggiungi Nuovo Prodotto</c:otherwise>
                </c:choose>
            </h2>
            
            <form action="${pageContext.request.contextPath}/admin/prodotti" method="post" enctype="multipart/form-data" class="admin-form">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <input type="hidden" name="action" value="${not empty productToEdit ? 'update' : 'save'}">
                
                <c:if test="${not empty productToEdit}">
                    <input type="hidden" name="id" value="${productToEdit.idProdotto}">
                    <input type="hidden" name="oldImmagine" value="${productToEdit.urlImmagine}">
                </c:if>

                <div class="form-grid">
                    <div class="field">
                        <label for="nome">Nome Prodotto</label>
                        <input type="text" id="nome" name="nome" value="<c:out value='${productToEdit.nomeProdotto}'/>" required>
                    </div>
                    
                    <div class="field">
                        <label for="idCategoria">Categoria</label>
                        <select id="idCategoria" name="idCategoria" required>
                            <option value="">Seleziona...</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.idCategoria}" ${productToEdit.idCategoria == cat.idCategoria ? 'selected' : ''}>
                                    <c:out value="${cat.nome}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="field">
                        <label for="prezzo">Prezzo di Listino (€)</label>
                        <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" value="${productToEdit.prezzoDiListino}" required>
                    </div>
                    
                    <div class="field">
                        <label for="sconto">Sconto (%)</label>
                        <input type="number" id="sconto" name="sconto" min="0" max="99" value="${not empty productToEdit ? productToEdit.scontoPercentuale : 0}">
                    </div>
                    
                    <div class="field">
                        <label for="quantita">Quantità Stock</label>
                        <input type="number" id="quantita" name="quantita" min="0" value="${not empty productToEdit ? productToEdit.quantitaDisponibile : 0}" required>
                    </div>
                    
                    <div class="field">
                        <label for="immagine">
                            Immagine ${not empty productToEdit ? '(Lascia vuoto per mantenere attuale)' : '*'}
                        </label>
                        <input type="file" id="immagine" name="immagine" accept="image/*" ${empty productToEdit ? 'required' : ''}>
                    </div>
                </div>
                
                <div class="field full-width">
                    <label for="descrizione">Descrizione Prodotto</label>
                    <textarea id="descrizione" name="descrizione" rows="4" required><c:out value="${productToEdit.descrizione}"/></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-save">
                        <i class="fas fa-save"></i> ${not empty productToEdit ? 'Aggiorna' : 'Salva'} Prodotto
                    </button>
                    <c:if test="${not empty productToEdit}">
                        <a href="${pageContext.request.contextPath}/admin/prodotti" class="btn-cancel">Annulla</a>
                    </c:if>
                </div>
            </form>
        </section>

        <!-- TABELLA PRODOTTI -->
        <section class="admin-table-section">
            <div class="section-header">
                <h2>Prodotti nel Catalogo Attivo</h2>
                <span class="results-count">${products.size()} articoli trovati</span>
            </div>
            
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Anteprima</th>
                        <th>Nome</th>
                        <th>Prezzo Finale</th>
                        <th>Stock</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/${p.urlImmagine}" 
                                     alt="Foto" class="admin-thumb-small" 
                                     onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
                            </td>
                            <td class="cell-name">
                                <strong><c:out value="${p.nomeProdotto}"/></strong>
                                <c:if test="${p.scontoPercentuale > 0}">
                                    <span class="table-discount-tag">-${p.scontoPercentuale}%</span>
                                </c:if>
                            </td>
                            <td>
                                <fmt:formatNumber value="${p.prezzoFinale}" type="currency" currencySymbol="€"/>
                            </td>
                            <td>
                                <%-- Feedback visivo per stock basso --%>
                                <span class="stock-badge ${p.quantitaDisponibile < 10 ? 'stock-low' : 'stock-ok'}">
                                    <c:out value="${p.quantitaDisponibile}"/>
                                </span>
                            </td>
                            <td class="actions-cell">
                                <%-- Edit richiama il doGet della Servlet con action=edit --%>
                                <a href="${pageContext.request.contextPath}/admin/prodotti?action=edit&id=${p.idProdotto}#form-prodotto" 
                                   class="btn-edit-sm" title="Modifica">
                                    <i class="fas fa-edit"></i>
                                </a>
                                
                                <%-- Delete implementato come Soft Delete --%>
                                <form action="${pageContext.request.contextPath}/admin/prodotti" method="post" style="display:inline;" 
                                      onsubmit="return confirm('Vuoi disattivare questo prodotto? Non sarà più visibile nel catalogo ma rimarrà registrato negli ordini passati.')">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${p.idProdotto}">
                                    <button type="submit" class="btn-delete-sm" title="Elimina (Soft Delete)">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</main>

<script src="${pageContext.request.contextPath}/js/admin-validation.js"></script>
<%@ include file="/WEB-INF/jspf/footer.jsp" %>