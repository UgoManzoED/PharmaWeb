<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
  <jsp:param name="pageTitle" value="Registrati" />
  <jsp:param name="pageCss" value="forms.css" />
</jsp:include>

<main>
  <div class="form-container">
    <h1>Crea un nuovo Account</h1>

    <form id="registrationForm" action="register" method="post" novalidate>
      <!-- Token CSRF: FONDAMENTALE per la sicurezza! -->
      <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

      <div class="form-group">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" value="<c:out value='${form_nome}'/>">
        <!-- Errore da Server -->
        <c:if test="${not empty errors.nome}"><span class="error-message">${errors.nome}</span></c:if>
      </div>

      <div class="form-group">
        <label for="cognome">Cognome</label>
        <input type="text" id="cognome" name="cognome" value="<c:out value='${form_cognome}'/>">
        <c:if test="${not empty errors.cognome}"><span class="error-message">${errors.cognome}</span></c:if>
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${form_email}'/>">
        <c:if test="${not empty errors.email}"><span class="error-message">${errors.email}</span></c:if>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password">
        <c:if test="${not empty errors.password}"><span class="error-message">${errors.password}</span></c:if>
      </div>

      <div class="form-group">
        <label for="confermaPassword">Conferma Password</label>
        <input type="password" id="confermaPassword" name="confermaPassword">
        <c:if test="${not empty errors.confermaPassword}"><span class="error-message">${errors.confermaPassword}</span></c:if>
      </div>

      <button type="submit" class="form-button">Registrati</button>
    </form>

    <div class="form-switch-link">
      <p>Hai già un account? <a href="${pageContext.request.contextPath}/login">Accedi</a></p>
    </div>
  </div>
</main>

<!-- Includiamo lo script di validazione -->
<script src="${pageContext.request.contextPath}/js/register.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>