<%--
  Created by IntelliJ IDEA.
  User: trees
  Date: 2024-11-28
  Time: 3:57 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Notifications</title>
</head>
<body>
<h1>Your Notifications</h1>
<ul>
    <c:forEach var="notification" items="${notifications}">
        <li>
                ${notification.message} - ${notification.status} (Sent: ${notification.dateSent})
        </li>
    </c:forEach>
</ul>
</body>
</html>

