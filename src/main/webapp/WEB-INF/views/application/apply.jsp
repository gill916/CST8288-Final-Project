<%--
    Author: Jiajun Cai
    Student Number: 041127296
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Apply to Teach" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="application-section">
        <h2>Apply to Teach: ${course.courseTitle}</h2>
        <p class="course-code">${course.courseCode}</p>

        <form action="${pageContext.request.contextPath}/application/submit" method="post" 
              class="application-form" onsubmit="return validateApplicationForm()">
            <input type="hidden" name="courseId" value="${course.courseId}">
            
            <div class="form-group">
                <label>Cover Letter</label>
                <textarea name="coverLetter" class="form-control" rows="6" required
                          placeholder="Explain why you're interested and qualified to teach this course..."></textarea>
            </div>

            <div class="form-group">
                <label>Additional Documents (Optional)</label>
                <textarea name="additionalDocs" class="form-control" rows="4"
                          placeholder="List any additional qualifications, certifications, or relevant experience..."></textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Submit Application</button>
                <a href="${pageContext.request.contextPath}/course/view/${course.courseId}" 
                   class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 