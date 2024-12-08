<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to Academic Exchange Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="welcome-page">
    <div class="welcome-container">
        <div class="welcome-content text-center">
            <h1 class="display-4 mb-4">Academic Exchange Platform</h1>
            <p class="lead mb-5">Connect with academic institutions worldwide and explore exchange opportunities</p>
            
            <div class="welcome-buttons">
                <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-primary btn-lg me-3">Login</a>
                <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-outline-primary btn-lg">Register</a>
            </div>
            
            <div class="features mt-5">
                <div class="row">
                    <div class="col-md-4">
                        <div class="feature-item">
                            <h3>For Professionals</h3>
                            <p>Find and apply for academic exchange opportunities worldwide</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-item">
                            <h3>For Institutions</h3>
                            <p>Post exchange opportunities and find qualified academics</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="feature-item">
                            <h3>Global Network</h3>
                            <p>Connect with institutions and professionals globally</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 