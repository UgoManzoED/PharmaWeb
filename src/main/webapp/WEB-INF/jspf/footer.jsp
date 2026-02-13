<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<footer class="footer">
    <div class="footer-content">
        <div class="footer-section chi-siamo">
            <h3>Chi Siamo</h3>
            <ul class="social-links">
                <li>
                    <span>Ugo Manzo:</span>
                    <a href="https://github.com/UgoManzoED" target="_blank" rel="noopener noreferrer">
                        <img src="https://cdn.jsdelivr.net/npm/simple-icons@latest/icons/github.svg" alt="GitHub" width="20" style="vertical-align:middle;">
                    </a>
                    <a href="https://www.linkedin.com/in/ugo-manzo" target="_blank" rel="noopener noreferrer">
                        <img src="https://cdn.jsdelivr.net/npm/simple-icons@latest/icons/linkedin.svg" alt="LinkedIn" width="20" style="vertical-align:middle;">
                    </a>
                </li>
                <li>
                    <span>Davide Pio Lazzarini:</span>
                    <a href="https://github.com/davidelazz" target="_blank" rel="noopener noreferrer">
                        <img src="https://cdn.jsdelivr.net/npm/simple-icons@latest/icons/github.svg" alt="GitHub" width="20" style="vertical-align:middle;">
                    </a>
                    <a href="https://www.linkedin.com/in/davidepiolazzarini" target="_blank" rel="noopener noreferrer">
                        <img src="https://cdn.jsdelivr.net/npm/simple-icons@latest/icons/linkedin.svg" alt="LinkedIn" width="20" style="vertical-align:middle;">
                    </a>
                </li>
            </ul>
        </div>

        <%-- Sezione ultime recensioni --%>
        <div class="footer-section latest-reviews">
		    <h3>Ultime Recensioni</h3>
		    <div id="footer-reviews-container">
		        <c:choose>
		            <c:when test="${not empty latestReviews}">
		                <ul class="footer-review-list">
		                    <c:forEach var="rev" items="${latestReviews}">
		                        <li class="footer-review-item">
		                            <span class="rev-product">
		                                <a href="${pageContext.request.contextPath}/prodotto?id=${rev.idProdotto}">
		                                    <c:out value="${rev.nomeProdotto}"/>
		                                </a>
		                            </span>
		                            <div class="rev-stars">
		                                <c:forEach begin="1" end="${rev.voto}">★</c:forEach>
		                            </div>
		                            <p class="rev-snippet">
		                                <%-- Tagliamo il testo se è troppo lungo --%>
		                                <c:choose>
		                                    <c:when test="${fn:length(rev.testo) > 50}">
		                                        <c:out value="${fn:substring(rev.testo, 0, 50)}"/>...
		                                    </c:when>
		                                    <c:otherwise>
		                                        <c:out value="${rev.testo}"/>
		                                    </c:otherwise>
		                                </c:choose>
		                            </p>
		                            <small class="rev-author">Da: <c:out value="${rev.nomeUtente}"/></small>
		                        </li>
		                    </c:forEach>
		                </ul>
		            </c:when>
		            <c:otherwise>
		                <p>Nessuna recensione recente.</p>
		            </c:otherwise>
		        </c:choose>
		    </div>
		</div>

        <div class="footer-section contacts">
            <h3>Contatti</h3>
            <ul>
                <li>📞 +39 089 123 4567</li>
                <li>✉️ supporto@pharmaweb.it</li>
            </ul>
        </div>
    </div>
    
    <div class="footer-bottom">
        <p>&copy; 2025-2026 PharmaWeb S.r.l. | Tutti i diritti riservati | P.IVA 12345678901</p>
    </div>
</footer>
