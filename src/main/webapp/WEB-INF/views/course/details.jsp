<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" value="Course Details" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="course-details-section">
        <div class="course-header">
            <h2>${course.courseTitle}</h2>
            <p class="course-code">${course.courseCode}</p>
            
            <div class="course-meta">
                <span class="badge bg-${course.status == 'ACTIVE' ? 'success' : 'warning'}">
                    ${course.status}
                </span>
                <span class="badge bg-info">${course.schedule}</span>
                <span class="badge bg-secondary">${course.deliveryMethod}</span>
            </div>
        </div>

        <div class="course-content">
            <div class="info-section">
                <h3>Course Information</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Term:</label>
                        <span>${course.term}</span>
                    </div>
                    <div class="info-item">
                        <label>Compensation:</label>
                        <span>$${course.compensation}</span>
                    </div>
                    <div class="info-item">
                        <label>Application Deadline:</label>
                        <span><fmt:formatDate value="${course.applicationDeadline}" 
                                            pattern="MMMM d, yyyy"/></span>
                    </div>
                </div>
            </div>

            <div class="info-section">
                <h3>Course Outline</h3>
                <p>${course.outline}</p>
            </div>

            <div class="info-section">
                <h3>Preferred Qualifications</h3>
                <p>${course.preferredQualifications}</p>
            </div>

            <c:if test="${sessionScope.user.userType == 'PROFESSIONAL'}">
                <div class="action-section">
                    <c:choose>
                        <c:when test="${hasApplied}">
                            <div class="alert alert-info">You have already applied for this course.</div>
                        </c:when>
                        <c:when test="${course.status == 'ACTIVE'}">
                            <form action="${pageContext.request.contextPath}/application/apply" method="post">
                                <input type="hidden" name="courseId" value="${course.courseId}">
                                <button type="submit" class="btn btn-primary">Apply Now</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning">This course is currently not accepting applications.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 