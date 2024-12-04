<%--
  Created by IntelliJ IDEA.
  User: trees
  Date: 2024-12-04
  Time: 5:05 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="AcademicExchangePlatform.model.User" %>
<%@ page session="true" %>
<%
  User loggedUser = (User) session.getAttribute("loggedUser");
  if (loggedUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
</head>
<body>
<h1>Welcome, <%= loggedUser.getEmail() %>!</h1>
<p>Your user type is: <%= loggedUser.getUserType().getValue() %></p>
<a href="LogoutServlet">Logout</a>
</body>
</html>
