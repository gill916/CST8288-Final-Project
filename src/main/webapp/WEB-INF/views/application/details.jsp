<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" value="Application Details" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="application-details-section">
        <div class="application-header">
            <h2>${application.course.courseTitle}</h2>
            <div class="meta-info">
                <span class="badge bg-${application.status == 'PENDING' ? 'warning' : 
                    application.status == 'ACCEPTED' ? 'success' : 
                    application.status == 'WITHDRAWN' ? 'secondary' : 'danger'}">
                    ${application.status}
                </span>
                <span class="course-code">${application.course.courseCode}</span>
            </div>
        </div>

        <div class="application-content">
            <div class="info-section">
                <h3>Application Information</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Applied Date:</label>
                        <span><fmt:formatDate value="${application.applicationDate}" pattern="MMMM d, yyyy"/></span>
                    </div>
                    <div class="info-item">
                        <label>Institution:</label>
                        <span>${application.course.institutionName}</span>
                    </div>
                    <c:if test="${application.decisionDate != null}">
                        <div class="info-item">
                            <label>Decision Date:</label>
                            <span><fmt:formatDate value="${application.decisionDate}" pattern="MMMM d, yyyy"/></span>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="info-section">
                <h3>Cover Letter</h3>
                <div class="content-box">
                    <p>${application.coverLetter}</p>
                </div>
            </div>

            <c:if test="${not empty application.additionalDocuments}">
                <div class="info-section">
                    <h3>Additional Documents</h3>
                    <div class="content-box">
                        <p>${application.additionalDocuments}</p>
                    </div>
                </div>
            </c:if>

            <div class="action-section">
                <c:choose>
                    <c:when test="${sessionScope.user.userType == 'INSTITUTION'}">
                        <a href="${pageContext.request.contextPath}/application/manage" 
                           class="btn btn-secondary">Back to Applications</a>
                        
                        <c:if test="${application.status == 'PENDING' && sessionScope.user.userType == 'INSTITUTION'}">
                            <form action="${pageContext.request.contextPath}/application/updateStatus" 
                                  method="post" style="display: inline;">
                                <input type="hidden" name="applicationId" value="${application.applicationId}">
                                <input type="hidden" name="status" value="ACCEPTED">
                                <button type="submit" class="btn btn-success">Accept</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/application/updateStatus" 
                                  method="post" style="display: inline;">
                                <input type="hidden" name="applicationId" value="${application.applicationId}">
                                <input type="hidden" name="status" value="REJECTED">
                                <button type="submit" class="btn btn-danger">Reject</button>
                            </form>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/application/my-applications" 
                           class="btn btn-secondary">Back to My Applications</a>
                        
                        <c:if test="${application.status == 'PENDING'}">
                            <form action="${pageContext.request.contextPath}/application/withdraw" 
                                  method="post" style="display: inline;">
                                <input type="hidden" name="applicationId" value="${application.applicationId}">
                                <button type="submit" class="btn btn-warning" 
                                        onclick="return confirm('Are you sure you want to withdraw this application?')">
                                    Withdraw Application
                                </button>
                            </form>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 