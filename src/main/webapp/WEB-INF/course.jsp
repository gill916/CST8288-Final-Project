<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Course Management</title>
</head>
<body>
<form action="course" method="post">
  <input type="hidden" name="action" value="create" />
  <input type="text" name="courseTitle" placeholder="Course Title" required />
  <input type="text" name="courseCode" placeholder="Course Code" required />
  <input type="text" name="term" placeholder="Term" required />
  <select name="schedule">
    <option value="Morning">Morning</option>
    <option value="Afternoon">Afternoon</option>
    <option value="Evening">Evening</option>
  </select>
  <select name="deliveryMethod">
    <option value="In-Person">In-Person</option>
    <option value="Remote">Remote</option>
    <option value="Hybrid">Hybrid</option>
  </select>
  <textarea name="outline" placeholder="Outline"></textarea>
  <textarea name="preferredQualifications" placeholder="Preferred Qualifications"></textarea>
  <input type="number" name="compensation" step="0.01" placeholder="Compensation" required />
  <input type="number" name="institutionId" placeholder="Institution ID" required />
  <button type="submit">Create Course</button>
</form>
</body>
</html>
