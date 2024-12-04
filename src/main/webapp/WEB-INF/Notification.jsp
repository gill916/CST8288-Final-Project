<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Import necessary classes
    import AcademicExchangePlatform.service.NotificationService;
    import AcademicExchangePlatform.model.Notification;

    // Fetch notifications for the current user (for testing, hardcoding userId = 1)
    int userId = 1; // Replace with session data in production
    NotificationService notificationService = new NotificationService();
    List<Notification> notifications = notificationService.getNotificationsByUserId(userId);

    request.setAttribute("notifications", notifications);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notifications</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1 style="text-align: center;">Notifications</h1>
<table>
    <thead>
    <tr>
        <th>Notification ID</th>
        <th>Message</th>
        <th>Date Sent</th>
        <th>Status</th>
        <th class="actions">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="notification" items="${notifications}">
        <tr>
            <td>${notification.notificationId}</td>
            <td>${notification.message}</td>
            <td>${notification.dateSent}</td>
            <td>${notification.status}</td>
            <td class="actions">
                <form action="${pageContext.request.contextPath}/notification" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="markRead">
                    <input type="hidden" name="notificationId" value="${notification.notificationId}">
                    <button type="submit">Mark as Read</button>
                </form>
                <form action="NotificationServlet" method="post" style="display: inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="notificationId" value="${notification.notificationId}">
                    <button type="submit" style="color: red;">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
