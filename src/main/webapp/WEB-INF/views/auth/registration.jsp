<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Register" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="auth-form">
        <h2>Register</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/register" method="post" id="registrationForm" onsubmit="return validateForm()">
            <div class="form-group">
                <label>User Type</label>
                <select name="userType" class="form-control" onchange="toggleUserTypeFields()" required>
                    <option value="">Select Type</option>
                    <option value="Professional">Academic Professional</option>
                    <option value="Institution">Academic Institution</option>
                </select>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>

            <!-- Professional Fields -->
            <div id="professionalFields" style="display: none;">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" name="firstName" class="form-control">
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" name="lastName" class="form-control">
                </div>
                <div class="form-group">
                    <label>Current Institution</label>
                    <input type="text" name="currentInstitution" class="form-control">
                </div>
                <div class="form-group">
                    <label>Academic Position</label>
                    <input type="text" name="academicPosition" class="form-control">
                </div>
            </div>

            <!-- Institution Fields -->
            <div id="institutionFields" style="display: none;">
                <div class="form-group">
                    <label>Institution Name</label>
                    <input type="text" name="institutionName" class="form-control">
                </div>
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address" class="form-control">
                </div>
                <div class="form-group">
                    <label>Contact Email</label>
                    <input type="email" name="contactEmail" class="form-control">
                </div>
            </div>

            <button type="submit" class="btn btn-primary">Register</button>
            <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-link">Back to Login</a>
        </form>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 