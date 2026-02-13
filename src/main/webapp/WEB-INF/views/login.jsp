<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
  <jsp:param name="pageTitle" value="Accedi" />
  <jsp:param name="pageCss" value="forms.css" />
</jsp:include>

<main>
  <div class="form-container">
    <h1>Accedi al tuo Account</h1>

    <!-- Mostra errore dal server, se presente -->
    <c:if test="${not empty error}">
      <div class="server-error">${error}</div>
    </c:if>

    <form action="login" method="post">
      <!-- Token CSRF: FONDAMENTALE per la sicurezza! -->
      <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" required>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>
      </div>

      <button type="submit" class="form-button">Accedi</button>
    </form>

    <div class="form-switch-link">
      <p>Non hai un account? <a href="${pageContext.request.contextPath}/register">Registrati ora</a></p>
    </div>
  </div>
</main>

<script src="${pageContext.request.contextPath}/js/login.js"></script>
<%@ include file="/WEB-INF/jspf/footer.jsp" %>
