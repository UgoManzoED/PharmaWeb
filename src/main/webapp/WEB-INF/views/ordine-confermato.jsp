<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/jspf/header.jsp">
    <jsp:param name="pageTitle" value="Ordine Confermato - PharmaWeb" />
    <jsp:param name="pageCss" value="checkout.css" />
</jsp:include>

<main class="success-page">
    <div class="success-container" style="max-width: 800px; margin: 40px auto; padding: 20px; text-align: center;">
        
        <!-- Icona di successo -->
        <div class="success-icon" style="font-size: 64px; color: #28a745; margin-bottom: 20px;">
            ✅
        </div>

        <h1 style="color: #003366;">Grazie per il tuo acquisto!</h1>
        <p style="font-size: 18px; color: #555;">
            L'ordine è stato ricevuto correttamente ed è in fase di elaborazione.
        </p>

        <!-- Box riepilogo ordine -->
        <div class="order-summary-box" style="background: #f8f9fa; border: 1px solid #dee2e6; border-radius: 8px; padding: 30px; margin-top: 30px; text-align: left;">
            <h2 style="border-bottom: 2px solid #003366; padding-bottom: 10px; margin-top: 0;">Dettagli Ordine #<c:out value="${ordine.idOrdine}"/></h2>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 20px;">
                <div>
                    <p><strong>Data:</strong> <fmt:formatDate value="${ordine.dataOrdine}" pattern="dd/MM/yyyy HH:mm" /></p>
                    <p><strong>Stato:</strong> <span class="badge" style="background: #e3f2fd; color: #0d47a1; padding: 4px 8px; border-radius: 4px;"><c:out value="${ordine.stato}"/></span></p>
                </div>
                <div>
                    <p><strong>Totale pagato:</strong> 
                        <span style="font-size: 20px; font-weight: bold; color: #28a745;">
                            <fmt:formatNumber value="${ordine.importoTotale}" type="currency" currencySymbol="€"/>
                        </span>
                    </p>
                </div>
            </div>

            <hr style="border: 0; border-top: 1px solid #dee2e6; margin: 20px 0;">

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div>
                    <h3>Spedito a:</h3>
                    <p style="color: #555;"><c:out value="${ordine.indirizzoSpedizione}"/></p>
                </div>
                <div>
                    <h3>Metodo di pagamento:</h3>
                    <p style="color: #555;"><c:out value="${ordine.metodoPagamentoUtilizzato}"/></p>
                </div>
            </div>

            <c:if test="${ordine.puntiGuadagnati > 0 || ordine.puntiUtilizzati > 0}">
                <hr style="border: 0; border-top: 1px solid #dee2e6; margin: 20px 0;">
                <div class="loyalty-info" style="background: #fff; padding: 15px; border-radius: 6px; border-left: 4px solid #ffc107;">
                    <h3 style="margin-top: 0; font-size: 16px;">Programma Fedeltà</h3>
                    <c:if test="${ordine.puntiUtilizzati > 0}">
                        <p style="margin: 5px 0;">Punti utilizzati per questo acquisto: <strong>${ordine.puntiUtilizzati}</strong> (-${ordine.puntiUtilizzati},00€)</p>
                    </c:if>
                    <c:if test="${ordine.puntiGuadagnati > 0}">
                        <p style="margin: 5px 0;">Punti guadagnati con questo acquisto: <strong style="color: #28a745;">+${ordine.puntiGuadagnati}</strong></p>
                    </c:if>
                </div>
            </c:if>
        </div>

        <!-- Azioni post-ordine -->
        <div class="success-actions" style="margin-top: 40px; display: flex; justify-content: center; gap: 20px;">
            <a href="${pageContext.request.contextPath}/" class="btn" style="background: #003366; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                Torna allo shopping
            </a>
            <a href="${pageContext.request.contextPath}/area-riservata/dashboard" class="btn" style="background: #6c757d; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                I miei ordini
            </a>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/footer.jsp" %>