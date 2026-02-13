<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="PharmaWeb - Home" />
    <jsp:param name="pageCss" value="home.css" />
</jsp:include>

<main>
    <!-- Sezione Categorie Dinamica -->
    <section class="categories-bar">
        <!-- Controllo di sicurezza se la lista esiste -->
        <c:if test="${not empty categories}">
            <c:forEach var="cat" items="${categories}">
                <%-- 
                    Il link punta alla CatalogServlet passando l'ID della categoria.
                    Es: /catalogo?cat=1 
                --%>
                <a href="${pageContext.request.contextPath}/catalogo?cat=${cat.idCategoria}" class="category">
                    ${cat.nome}
                </a>
            </c:forEach>
        </c:if>
    </section>

    <!-- 1. Offerte Imperdibili -->
    <my:productCarousel title="Offerte Imperdibili" items="${discountedProducts}" />

    <!-- 2. Le Nostre Novità -->
    <my:productCarousel title="Le nostre novità" items="${newProducts}" />

    <!-- 3. I Più Venduti -->
    <my:productCarousel title="I nostri prodotti più venduti" items="${popularProducts}" />

</main>

<%-- Inclusione JS --%>
<script src="${pageContext.request.contextPath}/js/home.js"></script>

<%-- Inclusione Footer --%>
<jsp:include page="/WEB-INF/jspf/footer.jsp" />

</body>
</html>
