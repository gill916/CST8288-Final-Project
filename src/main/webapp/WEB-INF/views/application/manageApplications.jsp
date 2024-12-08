<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" value="Manage Applications" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="manage-applications-section">
        <div class="section-header">
            <h2>Applications for: ${course.courseTitle}</h2>
            <p class="course-code">${course.courseCode}</p>
        </div>

        <div class="applications-table">
            <table class="table">
                <thead>
                    <tr>
                        <th>Applicant</th>
                        <th>Applied Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${applications}" var="application">
                        <tr>
                            <td>
                                ${application.professional.firstName} 
                                ${application.professional.lastName}
                            </td>
                            <td>
                                <fmt:formatDate value="${application.applicationDate}" 
                                              pattern="MMM d, yyyy"/>
                            </td>
                            <td>
                                <span class="badge bg-${application.status == 'PENDING' ? 'warning' : 
                                    application.status == 'ACCEPTED' ? 'success' : 
                                    application.status == 'WITHDRAWN' ? 'secondary' : 'danger'}">
                                    ${application.status}
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/application/view/${application.applicationId}" 
                                       class="btn btn-sm btn-info">View</a>
                                    <c:if test="${application.status == 'PENDING'}">
                                        <form action="${pageContext.request.contextPath}/application/updateStatus" 
                                              method="post" style="display: inline;">
                                            <input type="hidden" name="applicationId" 
                                                   value="${application.applicationId}">
                                            <input type="hidden" name="status" value="ACCEPTED">
                                            <button type="submit" class="btn btn-sm btn-success">Accept</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/application/updateStatus" 
                                              method="post" style="display: inline;">
                                            <input type="hidden" name="applicationId" 
                                                   value="${application.applicationId}">
                                            <input type="hidden" name="status" value="REJECTED">
                                            <button type="submit" class="btn btn-sm btn-danger">Reject</button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 