<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Error" scope="request"/>
<%@ include file="../common/header.jsp" %>

<div class="container">
    <div class="error-section profile-section">
        <div class="text-center">
            <h2 class="text-danger mb-4">Oops! Something went wrong</h2>
            
            <div class="error-details mb-4">
                <c:if test="${not empty pageContext.errorData.statusCode}">
                    <div class="error-code mb-3">
                        <span class="display-4 text-danger">
                            ${pageContext.errorData.statusCode}
                        </span>
                    </div>
                </c:if>
                
                <p class="lead text-muted">
                    <c:choose>
                        <c:when test="${not empty error}">
                            ${error}
                        </c:when>
                        <c:when test="${pageContext.errorData.statusCode == 404}">
                            The page you're looking for could not be found.
                        </c:when>
                        <c:when test="${pageContext.errorData.statusCode == 403}">
                            You don't have permission to access this resource.
                        </c:when>
                        <c:otherwise>
                            An unexpected error occurred. Please try again later.
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <div class="error-actions">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary me-3">
                    <i class="fas fa-home me-2"></i>Return to Dashboard
                </a>
                <button onclick="history.back()" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left me-2"></i>Go Back
                </button>
            </div>
        </div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %> 