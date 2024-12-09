<%--
    Author: Jiajun Cai
    Student Number: 041127296
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Debug Information --%>
<c:if test="${pageContext.request.getParameter('debug') == 'true'}">
    <div class="debug-info" style="background: #f8f9fa; padding: 10px; margin: 10px; border: 1px solid #ddd;">
        <h4>Debug Information</h4>
        <p>Request URI: ${pageContext.request.requestURI}</p>
        <p>Context Path: ${pageContext.request.contextPath}</p>
        <p>Servlet Path: ${pageContext.request.servletPath}</p>
        <p>User Type: ${sessionScope.user.userType}</p>
        <p>Applications Count: ${applications.size()}</p>
    </div>
</c:if>

<c:set var="title" value="My Applications" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="applications-section">
        <h2>My Teaching Applications</h2>

        <div class="applications-grid">
            <c:forEach items="${applications}" var="application">
                <div class="application-card">
                    <div class="application-header">
                        <h3>${not empty application.course ? application.course.courseTitle : 'N/A'}</h3>
                        <span class="badge bg-${application.status == 'PENDING' ? 'warning' : 
                            application.status == 'ACCEPTED' ? 'success' : 
                            application.status == 'WITHDRAWN' ? 'secondary' : 'danger'}">
                            ${application.status}
                        </span>
                    </div>
                    
                    <div class="application-details">
                        <p><strong>Applied:</strong> 
                            <fmt:formatDate value="${application.applicationDate}" 
                                          pattern="MMMM d, yyyy"/>
                        </p>
                        <p><strong>Institution:</strong> ${not empty application.course ? application.course.institutionName : 'N/A'}</p>
                    </div>

                    <div class="application-actions">
                        <a href="${pageContext.request.contextPath}/application/view/${application.applicationId}" 
                           class="btn btn-info">View Details</a>
                        <c:if test="${application.status == 'PENDING'}">
                            <form action="${pageContext.request.contextPath}/application/withdraw" 
                                  method="post" style="display: inline;">
                                <input type="hidden" name="applicationId" 
                                       value="${application.applicationId}">
                                <button type="submit" class="btn btn-warning" 
                                        onclick="return confirm('Are you sure you want to withdraw this application?')">
                                    Withdraw
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 