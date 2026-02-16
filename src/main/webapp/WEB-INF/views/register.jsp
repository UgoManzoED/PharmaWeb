<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
  <jsp:param name="pageTitle" value="Registrati - PharmaWeb" />
  <jsp:param name="pageCss" value="forms.css" />
</jsp:include>

<main>
  <div class="form-container">
    <h1>Crea un nuovo Account</h1>

    <%-- Messaggio di errore generale --%>
    <c:if test="${not empty error}">
      <div class="server-error" role="alert">
        <c:out value="${error}"/>
      </div>
    </c:if>

    <%-- 
        'novalidate' perché la validazione è gestita 
        da register.js e dal backend.
    --%>
    <form id="registrationForm" action="${pageContext.request.contextPath}/register" method="post" novalidate>
      
      <!-- Token CSRF: FONDAMENTALE per la sicurezza! -->
      <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

      <div class="form-group">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" 
               value="<c:out value='${form_nome}'/>" 
               placeholder="Inserisci il tuo nome">
        
        <!-- Errore da Server per il campo Nome -->
        <c:if test="${not empty errors.nome}">
          <span class="error-message" role="status"><c:out value="${errors.nome}"/></span>
        </c:if>
      </div>

      <div class="form-group">
        <label for="cognome">Cognome</label>
        <input type="text" id="cognome" name="cognome" 
               value="<c:out value='${form_cognome}'/>" 
               placeholder="Inserisci il tuo cognome">
        
        <!-- Errore da Server per il campo Cognome -->
        <c:if test="${not empty errors.cognome}">
          <span class="error-message" role="status"><c:out value="${errors.cognome}"/></span>
        </c:if>
      </div>

      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" 
               value="<c:out value='${form_email}'/>" 
               placeholder="esempio@mail.it">
        
        <!-- Errore da Server per il campo Email -->
        <c:if test="${not empty errors.email}">
          <span class="error-message" role="status"><c:out value="${errors.email}"/></span>
        </c:if>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" name="password" 
               placeholder="Almeno 8 caratteri, una maiuscola e un numero">
        
        <!-- Errore da Server per il campo Password -->
        <c:if test="${not empty errors.password}">
          <span class="error-message" role="status"><c:out value="${errors.password}"/></span>
        </c:if>
      </div>

      <div class="form-group">
        <label for="confermaPassword">Conferma Password</label>
        <input type="password" id="confermaPassword" name="confermaPassword" 
               placeholder="Ripeti la password scelta">
        
        <!-- Errore da Server per la corrispondenza password -->
        <c:if test="${not empty errors.confermaPassword}">
          <span class="error-message" role="status"><c:out value="${errors.confermaPassword}"/></span>
        </c:if>
      </div>

      <button type="submit" class="form-button">Registrati</button>
    </form>

    <div class="form-switch-link">
      <p>Hai già un account? <a href="${pageContext.request.contextPath}/login">Accedi</a></p>
    </div>
  </div>
</main>

<script src="${pageContext.request.contextPath}/js/register.js"></script>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>