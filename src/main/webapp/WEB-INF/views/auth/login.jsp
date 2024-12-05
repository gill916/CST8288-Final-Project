<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="title" value="Login" scope="request"/>
<%@ include file="common/header.jsp" %>

<div class="form-container">
    <h2>Login</h2>
    <form action="${pageContext.request.contextPath}/login" method="post" class="form-group">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit" class="submit-btn">Login</button>
        
        <div class="form-footer">
            <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
        </div>
    </form>
</div>

<%@ include file="common/footer.jsp" %>
