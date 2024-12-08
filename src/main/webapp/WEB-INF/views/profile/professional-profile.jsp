<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Professional Profile" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="profile-section">
        <h2>Professional Profile</h2>
        
        <!-- Add message display -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success">
                ${sessionScope.successMessage}
                <% session.removeAttribute("successMessage"); %>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger">
                ${sessionScope.errorMessage}
                <% session.removeAttribute("errorMessage"); %>
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/profile" method="post" 
              onsubmit="return validateProfileForm('PROFESSIONAL')">
            <!-- Personal Information -->
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label>First Name</label>
                        <input type="text" name="firstName" class="form-control" 
                               value="${user.firstName}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label>Last Name</label>
                        <input type="text" name="lastName" class="form-control" 
                               value="${user.lastName}" required>
                    </div>
                </div>
            </div>

            <!-- Professional Information -->
            <div class="form-group">
                <label>Current Institution</label>
                <input type="text" name="currentInstitution" class="form-control" 
                       value="${user.currentInstitution}" required>
            </div>

            <div class="form-group">
                <label>Academic Position</label>
                <input type="text" name="position" class="form-control" 
                       value="${user.position}" required>
            </div>

            <!-- Qualifications -->
            <div class="form-group">
                <label>Education Background</label>
                <textarea name="educationBackground" class="form-control" 
                          rows="3" required>${user.educationBackground}</textarea>
            </div>

            <div class="form-group">
                <label>Areas of Expertise</label>
                <input type="text" name="expertise" class="form-control" 
                       value="${not empty user.expertise ? String.join(', ', user.expertise) : ''}" 
                       placeholder="Separate with commas" required>
            </div>

            <button type="submit" class="btn btn-primary">Update Profile</button>
        </form>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 