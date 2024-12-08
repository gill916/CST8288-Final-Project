<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Search Courses" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="search-section">
        <h2>Search Courses</h2>
        
        <form action="${pageContext.request.contextPath}/course/search" method="get" class="search-form">
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label>Keyword</label>
                        <input type="text" name="keyword" class="form-control" value="${param.keyword}">
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group">
                        <label>Schedule</label>
                        <select name="schedule" class="form-control">
                            <option value="">All Schedules</option>
                            <option value="MORNING">Morning</option>
                            <option value="AFTERNOON">Afternoon</option>
                            <option value="EVENING">Evening</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="form-group">
                        <label>Delivery Method</label>
                        <select name="deliveryMethod" class="form-control">
                            <option value="">All Methods</option>
                            <option value="IN_PERSON">In Person</option>
                            <option value="REMOTE">Remote</option>
                            <option value="HYBRID">Hybrid</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary mt-4">Search</button>
                </div>
            </div>
        </form>
    </div>

    <div class="course-grid">
        <c:forEach items="${courses}" var="course">
            <div class="course-card">
                <h3>${course.courseTitle}</h3>
                <p class="course-code">${course.courseCode}</p>
                <div class="course-details">
                    <span class="badge bg-info">${course.schedule}</span>
                    <span class="badge bg-secondary">${course.deliveryMethod}</span>
                </div>
                <p class="course-compensation">Compensation: $${course.compensation}</p>
                <div class="card-actions">
                    <a href="${pageContext.request.contextPath}/course/view/${course.courseId}" 
                       class="btn btn-primary">View Details</a>
                    <form action="${pageContext.request.contextPath}/application/apply" method="post" class="d-inline">
                        <input type="hidden" name="courseId" value="${course.courseId}">
                        <button type="submit" class="btn btn-success" 
                                onclick="return confirm('Are you sure you want to apply for this course?')">
                            Apply Now
                        </button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 