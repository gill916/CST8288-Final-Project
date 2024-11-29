<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Search Results</title>
</head>
<body>
    <h1>Search Results</h1>

    <form action="/search" method="get">
        <input type="text" name="keyword" placeholder="Search for courses..." required>
        <button type="submit">Search</button>
    </form>

    <c:if test="${not empty courses}">
        <table border="1">
            <thead>
                <tr>
                    <th>Course Title</th>
                    <th>Course Code</th>
                    <th>Term</th>
                    <th>Schedule</th>
                    <th>Delivery Method</th>
                    <th>Outline</th>
                    <th>Preferred Qualifications</th>
                    <th>Compensation</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courses}">
                    <tr>
                        <td>${course.courseTitle}</td>
                        <td>${course.courseCode}</td>
                        <td>${course.term}</td>
                        <td>${course.schedule}</td>
                        <td>${course.deliveryMethod}</td>
                        <td>${course.outline}</td>
                        <td>${course.preferredQualifications}</td>
                        <td>${course.compensation}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${empty courses}">
        <p>No results found for your search.</p>
    </c:if>
</body>
</html>
