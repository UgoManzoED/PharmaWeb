<%@ tag description="Card singolo prodotto" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Definizione degli attributi che questo tag accetta --%>
<%@ attribute name="product" type="it.unisa.pharmaweb.model.bean.ProductBean" required="true" %>

<div class="product-card">
    <img class="product-image" 
         src="${pageContext.request.contextPath}/${product.urlImmagine}" 
         alt="${product.nomeProdotto}">
    
    <h3 class="product-name">
        <a href="${pageContext.request.contextPath}/prodotto?id=${product.idProdotto}" style="color: inherit; text-decoration: none;">
            ${product.nomeProdotto}
        </a>
    </h3>
    
    <div class="product-price">
        <c:choose>
            <%-- Caso Sconto --%>
            <c:when test="${product.scontoPercentuale > 0}">
                <span class="original-price">
                    <fmt:formatNumber value="${product.prezzoDiListino}" type="currency" currencySymbol="€"/>
                </span>
                <span class="discounted-price">
                    <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                </span>
                <span class="discount-badge">-${product.scontoPercentuale}%</span>
            </c:when>
            <%-- Caso Prezzo Pieno --%>
            <c:otherwise>
                <span class="normal-price">
                    <fmt:formatNumber value="${product.prezzoFinale}" type="currency" currencySymbol="€"/>
                </span>
            </c:otherwise>
        </c:choose>
    </div>
    
    <div class="card-actions">
        <button class="add-to-cart-button" data-product-id="${product.idProdotto}">Aggiungi al carrello</button>
        <button class="add-to-wishlist-button" data-product-id="${product.idProdotto}">❤️</button>
    </div>
</div>