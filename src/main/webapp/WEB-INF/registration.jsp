<%--
  Created by IntelliJ IDEA.
  User: trees
  Date: 2024-12-04
  Time: 5:01 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>User Registration</title>
</head>
<body>
<h2>User Registration</h2>
<form action="RegisterServlet" method="post">
  <label for="email">Email:</label>
  <input type="email" id="email" name="email" required><br><br>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" required><br><br>

  <label for="userType">User Type:</label>
  <select id="userType" name="userType" required>
    <option value="Admin">Admin</option>
    <option value="Institution">Institution</option>
    <option value="Professional">Professional</option>
  </select><br><br>

  <button type="submit">Register</button>
</form>
</body>
</html>
