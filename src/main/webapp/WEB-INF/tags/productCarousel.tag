<%@ tag description="Carosello di prodotti" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Importiamo i nostri tag custom per usare productCard --%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<%@ attribute name="title" required="true" %>
<%@ attribute name="items" type="java.util.List" required="true" description="Lista di ProductBean" %>

<%-- Mostriamo la sezione solo se la lista non è vuota --%>
<c:if test="${not empty items}">
    <section class="product-carousel">
        <h2>${title}</h2>
        <div class="products-container">
            <c:forEach var="p" items="${items}">
                <%-- Richiamiamo il tag creato al passo 1 --%>
                <my:productCard product="${p}" />
            </c:forEach>
        </div>
        <!-- Frecce per lo scorrimento -->
        <div class="carousel-arrow prev">&lt;</div>
        <div class="carousel-arrow next">&gt;</div>
    </section>
</c:if>