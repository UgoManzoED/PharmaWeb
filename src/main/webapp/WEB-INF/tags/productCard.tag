<%@ tag description="Card singolo prodotto" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" type="it.unisa.pharmaweb.model.bean.ProductBean" required="true" %>

<%-- 
    Aggiungiamo i data-attributes necessari per il JS del catalogo
    e una classe 'out-of-stock' se la quantità è 0
--%>
<div class="product-card ${product.quantitaDisponibile == 0 ? 'out-of-stock' : ''}" 
     data-id="${product.idProdotto}"
     data-name="<c:out value='${product.nomeProdotto}'/>"
     data-price="${product.prezzoFinale}"
     data-discount="${product.scontoPercentuale}"
     data-available="${product.quantitaDisponibile}">

    <div class="product-image-container">
        <c:if test="${product.scontoPercentuale > 0}">
            <span class="discount-badge">-${product.scontoPercentuale}%</span>
        </c:if>
        
        <a href="${pageContext.request.contextPath}/prodotto?id=${product.idProdotto}">
            <img class="product-image" 
                 src="${pageContext.request.contextPath}/${product.urlImmagine}" 
                 alt="<c:out value='${product.nomeProdotto}'/>"
                 onerror="this.src='${pageContext.request.contextPath}/img/placeholder.jpg'">
        </a>
    </div>
    
    <h3 class="product-name">
        <a href="${pageContext.request.contextPath}/prodotto?id=${product.idProdotto}">
            <c:out value="${product.nomeProdotto}"/>
        </a>
    </h3>
    
    <div class="product-price">
        <c:choose>
            <c:when test="${product.scontoPercentuale > 0}">
                <span class="original-price">
                    <fmt:formatNumber value="${product.prezzoDiListino}" type="currency" currencySymbol="€"/>
                </span>
                <span class="discounted-price">
                    <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                </span>
            </c:when>
            <c:otherwise>
                <span class="normal-price">
                    <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                </span>
            </c:otherwise>
        </c:choose>
    </div>
    
    <div class="card-actions">
        <c:choose>
            <c:when test="${product.quantitaDisponibile > 0}">
                <button type="button" class="add-to-cart-button" data-product-id="${product.idProdotto}">
                    <i class="fas fa-cart-plus"></i> Aggiungi
                </button>
            </c:when>
            <c:otherwise>
                <button type="button" class="add-to-cart-button" disabled title="Prodotto momentaneamente esaurito">
                    Esaurito
                </button>
            </c:otherwise>
        </c:choose>
        
        <button type="button" class="add-to-wishlist-button" data-product-id="${product.idProdotto}" title="Aggiungi ai preferiti">
            <i class="far fa-heart"></i>
        </button>
    </div>

    <%-- Azioni Admin --%>
    <c:if test="${sessionScope.utente.ruolo == 'admin'}">
        <div class="admin-actions">
            <a href="${pageContext.request.contextPath}/admin/prodotti?action=edit&id=${product.idProdotto}#form-prodotto" 
               class="btn-admin-edit">
                <i class="fas fa-edit"></i> MODIFICA
            </a>
        </div>
    </c:if>
</div>