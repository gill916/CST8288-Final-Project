<%--
    Author: Jiajun Cai
    Student Number: 041127296
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <%
    String path = request.getRequestURI().substring(request.getContextPath().length());
    boolean isPublicPage = path.endsWith("/login.jsp") || 
                          path.endsWith("/registration.jsp") || 
                          path.endsWith("/index.jsp");
    boolean isNotificationRequest = path.contains("/notification");
    boolean isApplicationPath = path.equals("/application/manage") || path.equals("/application/my-applications");

    if (session.getAttribute("user") == null && 
        !isPublicPage && 
        !isNotificationRequest && 
        !isApplicationPath) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
    %>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Academic Exchange</a>
            
            <c:if test="${not empty sessionScope.user}">
                <div class="navbar-nav">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Dashboard</a>
                    
                    <c:if test="${sessionScope.user.userType == 'PROFESSIONAL'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/course/search">Search Courses</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/application/my-applications">My Applications</a>
                    </c:if>
                    
                    <c:if test="${sessionScope.user.userType == 'INSTITUTION'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/course/manage">Manage Courses</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/application/manage">Manage Applications</a>
                    </c:if>
                    
                    <a class="nav-link" href="${pageContext.request.contextPath}/profile">Profile</a>
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/notification/notifications">
                        Notifications
                        <c:if test="${sessionScope.unreadNotifications > 0}">
                            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                ${sessionScope.unreadNotifications}
                                <span class="visually-hidden">unread notifications</span>
                            </span>
                        </c:if>
                    </a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/auth/logout">Logout</a>
                </div>
            </c:if>
        </div>
    </nav>

    <div class="container mt-4">
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger">
                ${sessionScope.error}
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success">
                ${sessionScope.success}
                <c:remove var="success" scope="session"/>
            </div>
        </c:if>
    </div>