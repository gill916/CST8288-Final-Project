<%--
  Created by IntelliJ IDEA.
  User: trees
  Date: 2024-12-04
  Time: 5:02 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>User Login</title>
</head>
<body>
<h2>User Login</h2>
<form action="LoginServlet" method="post">
  <label for="email">Email:</label>
  <input type="email" id="email" name="email" required><br><br>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" required><br><br>

  <button type="submit">Login</button>
</form>
</body>
</html>
