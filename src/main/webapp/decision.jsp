<%@ taglib prefix="c" uri="java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Decisions Management</title>
    </head>
    <body>
        <h1>Decisions Management</h1>
        <div class="decisions">
            <table>
                <thead>
                    <tr>
                        <th>Course Title</th>
                        <th>Request Date</th>
                        <th>Decision Date</th>
                        <th>Current Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <c:if test="${not empty requestsBycourseId}">
                    <tbody>
                        <c:forEach var="request" items="${requestsBycourseId}">
                            <tr>
                                <td>${request.courseTitle}</td>
                                <td>${request.requestDate}</td>
                                <td>${request.decisionDate}</td>
                                <td>${request.status}</td>
                                <td>
                                    <form action="/Requests" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="accept" />
                                        <input type="hidden" name="requestId" value="${request.requestId}" />
                                        <button type="submit">Accept</button>
                                    </form>
                                    <form action="/Requests" method="post" style="display:inline;">
                                        <input type="hidden" name="action" value="reject" />
                                        <input type="hidden" name="requestId" value="${request.requestId}" />
                                        <button type="submit">Reject</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                <c:if>
            </table>
        </div>
    </body>
</html>