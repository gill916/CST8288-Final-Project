<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="title" value="Manage Courses" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="page-header">
        <h2>Manage Courses</h2>
        <a href="${pageContext.request.contextPath}/course/create" class="btn btn-primary">Create New Course</a>
    </div>

    <div class="filter-section">
        <form action="" method="GET" class="form-group">
            <div class="form-group">
                <label for="status">Filter by Status:</label>
                <select id="status" name="status" onchange="this.form.submit()">
                    <option value="">All Statuses</option>
                    <option value="ACTIVE" ${param.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                    <option value="DRAFT" ${param.status == 'DRAFT' ? 'selected' : ''}>Draft</option>
                    <option value="CLOSED" ${param.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                    <option value="ARCHIVED" ${param.status == 'ARCHIVED' ? 'selected' : ''}>Archived</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="term">Filter by Term:</label>
                <select id="term" name="term" onchange="this.form.submit()">
                    <option value="">All Terms</option>
                    <c:forEach items="${terms}" var="term">
                        <option value="${term}" ${param.term == term ? 'selected' : ''}>${term}</option>
                    </c:forEach>
                </select>
            </div>
        </form>
    </div>

    <div class="courses-grid">
        <c:forEach items="${courses}" var="course">
            <div class="course-card">
                <div class="course-header">
                    <h3>${course.courseTitle}</h3>
                    <span class="status-badge ${fn:toLowerCase(course.status)}">${course.status}</span>
                </div>
                
                <div class="course-info">
                    <p><strong>Course Code:</strong> ${course.courseCode}</p>
                    <p><strong>Term:</strong> ${course.term}</p>
                    <p><strong>Applications:</strong> ${course.applicationCount}</p>
                    <p><strong>Deadline:</strong> ${course.applicationDeadline}</p>
                </div>
                
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/course/view/${course.courseId}" 
                       class="btn btn-secondary">View Details</a>
                    <a href="${pageContext.request.contextPath}/application/manage/${course.courseId}" 
                       class="btn btn-primary">Manage Applications 
                       <c:if test="${course.pendingApplications > 0}">
                           <span class="badge">${course.pendingApplications}</span>
                       </c:if>
                    </a>
                    
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle">More Actions</button>
                        <div class="dropdown-content">
                            <a href="${pageContext.request.contextPath}/course/edit/${course.courseId}">Edit Course</a>
                            <c:choose>
                                <c:when test="${course.status == 'ACTIVE'}">
                                    <a href="#
</file> 