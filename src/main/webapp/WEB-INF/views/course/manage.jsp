<%--
    Author: Jiajun Cai
    Student Number: 041127296
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Manage Courses" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="manage-header">
        <h2>Manage Courses</h2>
        <a href="${pageContext.request.contextPath}/course/create" 
           class="btn btn-primary">Create New Course</a>
    </div>

    <div class="course-list">
        <c:forEach items="${courses}" var="course">
            <div class="course-item">
                <div class="course-info">
                    <h3>${course.courseTitle}</h3>
                    <p class="course-code">${course.courseCode}</p>
                    <div class="course-meta">
                        <span class="badge bg-${course.status == 'ACTIVE' ? 'success' : 'warning'}">
                            ${course.status}
                        </span>
                        <span class="badge bg-info">${course.schedule}</span>
                        <span class="badge bg-secondary">${course.deliveryMethod}</span>
                    </div>
                </div>
                <div class="course-actions">
                    <a href="${pageContext.request.contextPath}/course/edit?id=${course.courseId}" 
                       class="btn btn-secondary">Edit</a>
                    <a href="${pageContext.request.contextPath}/application/manage?courseId=${course.courseId}" 
                       class="btn btn-info">View Applications</a>
                    <button onclick="updateCourseStatus(${course.courseId}, 
                            '${course.status == 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}')" 
                            class="btn btn-${course.status == 'ACTIVE' ? 'warning' : 'success'}">
                        ${course.status == 'ACTIVE' ? 'Deactivate' : 'Activate'}
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 