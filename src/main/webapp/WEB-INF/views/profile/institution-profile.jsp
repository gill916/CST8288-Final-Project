<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="title" value="Institution Profile" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="profile-section">
        <h2>Institution Profile</h2>
        
        <form action="${pageContext.request.contextPath}/profile/update" method="post" 
              onsubmit="return validateProfileForm('INSTITUTION')">
            <!-- Institution Information -->
            <div class="form-group">
                <label>Institution Name</label>
                <input type="text" name="institutionName" class="form-control" 
                       value="${user.institutionName}" required>
            </div>

            <!-- Contact Information -->
            <div class="form-group">
                <label>Address</label>
                <input type="text" name="address" class="form-control" 
                       value="${user.address}" required>
            </div>

            <div class="form-group">
                <label>Contact Email</label>
                <input type="email" name="contactEmail" class="form-control" 
                       value="${user.contactEmail}" required>
            </div>

            <div class="form-group">
                <label>Contact Phone</label>
                <input type="tel" name="contactPhone" class="form-control" 
                       value="${user.contactPhone}" required>
            </div>

            <!-- Additional Information -->
            <div class="form-group">
                <label>Website</label>
                <input type="url" name="website" class="form-control" 
                       value="${user.website}">
            </div>

            <div class="form-group">
                <label>Institution Description</label>
                <textarea name="description" class="form-control" 
                          rows="4">${user.description}</textarea>
            </div>

            <button type="submit" class="btn btn-primary">Update Profile</button>
        </form>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 