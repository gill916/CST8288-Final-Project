<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="${empty course ? 'Create Course' : 'Edit Course'}" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <div class="card">
                <div class="card-header">
                    <h2 class="mb-0">${empty course ? 'Create New Course' : 'Edit Course'}</h2>
                </div>
                <div class="card-body">
                    <!-- Debug Info -->
                    <div class="alert alert-info">
                        Debug Info:<br>
                        Course Object: ${not empty course ? course : 'No course object'}<br>
                        Schedules Array: ${not empty schedules ? schedules : 'No schedules'}<br>
                        Delivery Methods Array: ${not empty deliveryMethods ? deliveryMethods : 'No delivery methods'}<br>
                    </div>

                    <form action="${pageContext.request.contextPath}/course/${empty course ? 'create' : 'edit'}" 
                          method="post" class="course-form needs-validation" novalidate>
                        <c:if test="${not empty course}">
                            <input type="hidden" name="courseId" value="${course.courseId}">
                        </c:if>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Course Title</label>
                                    <input type="text" name="courseTitle" class="form-control" 
                                           value="${course.courseTitle}" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Course Code</label>
                                    <input type="text" name="courseCode" class="form-control" 
                                           value="${course.courseCode}" required>
                                </div>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="form-label">Schedule</label>
                                    <select name="schedule" class="form-control" required>
                                        <option value="">Select Schedule</option>
                                        <c:if test="${not empty schedules}">
                                            <c:forEach items="${schedules}" var="schedule">
                                                <option value="${schedule.name()}">${schedule.name().replace('_', ' ')}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="form-label">Delivery Method</label>
                                    <select name="deliveryMethod" class="form-control" required>
                                        <option value="">Select Delivery Method</option>
                                        <c:if test="${not empty deliveryMethods}">
                                            <c:forEach items="${deliveryMethods}" var="method">
                                                <option value="${method.name()}">${method.name().replace('_', ' ')}</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="form-label">Term</label>
                                    <input type="text" name="term" class="form-control" 
                                           value="${course.term}" required>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Course Outline</label>
                            <textarea name="outline" class="form-control" rows="5" required>${course.outline}</textarea>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Preferred Qualifications</label>
                            <textarea name="preferredQualifications" class="form-control" 
                                      rows="3">${course.preferredQualifications}</textarea>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Compensation</label>
                                    <input type="number" name="compensation" class="form-control" 
                                           value="${course.compensation}" step="0.01" required>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label class="form-label">Application Deadline</label>
                                    <input type="date" name="applicationDeadline" class="form-control" 
                                           value="${course.applicationDeadline}" required>
                                </div>
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <h5>Form Data Debug:</h5>
                            <pre id="formDebug"></pre>
                        </div>

                        <div class="form-actions mt-3">
                            <button type="submit" class="btn btn-primary">
                                ${empty course ? 'Create Course' : 'Update Course'}
                            </button>
                            <a href="${pageContext.request.contextPath}/course/manage" 
                               class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="notificationToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <strong class="me-auto">Notification</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body"></div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    console.log('Form loaded');
    console.log('Course:', '${course}');
    console.log('Schedules:', '${schedules}');
    console.log('Delivery Methods:', '${deliveryMethods}');

    const form = document.querySelector('.course-form');
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Basic validation
        const requiredFields = form.querySelectorAll('[required]');
        let isValid = true;
        
        requiredFields.forEach(field => {
            if (!field.value) {
                isValid = false;
                field.classList.add('is-invalid');
            } else {
                field.classList.remove('is-invalid');
            }
        });
        
        // Check if compensation is a valid number
        const compensation = form.querySelector('[name="compensation"]');
        if (isNaN(compensation.value) || compensation.value <= 0) {
            isValid = false;
            compensation.classList.add('is-invalid');
        }
        
        if (isValid) {
            form.submit();
        }
    });
});

function updateDebug() {
    const form = document.querySelector('.course-form');
    const formData = new FormData(form);
    let debugText = '';
    for (let pair of formData.entries()) {
        debugText += pair[0] + ': ' + pair[1] + '\n';
    }
    document.getElementById('formDebug').textContent = debugText;
}

document.querySelectorAll('.course-form input, .course-form select, .course-form textarea')
    .forEach(el => el.addEventListener('change', updateDebug));
</script>

<%@ include file="../../common/footer.jsp" %> 