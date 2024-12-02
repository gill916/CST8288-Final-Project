<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Request Management</title>
    </head>
    <body>
        <h1>Request Management</h1>
        <div class="requests">
            <table>
                <thead>
                    <tr>
                        <th>Course Title</th>
                        <th>Status</th>
                        <th>Request Date</th>
                        <th>Decision Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <c:if test="${not empty requestsByUserId}">
                    <tbody>
                        <c:forEach var="request" items="${requestsByUserId}">
                            <tr>
                                <td>${request.courseTitle}</td>
                                <td>${request.status}</td>
                                <td>${request.requestDate}</td>
                                <td>${request.decisionDate}</td>
                                <td>
                                    <form action="/Requests" method="post" style="display:inline">
                                        <input type="hidden" name="action" value="cancel" />
                                        <input type="hiddem" name="requestId" value="${request.requestId}" />
                                        <button type="submit">Cancel</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </c:if>
            </table>
            <div class="buttons">
                <c:if test="${not empty param.page or param.page > 0}">
                    <a href="/Requests?page=${param.page - 1}"></a>
                </c:if>
                <a href="/Requests?page=${param.page + 1}"></a>
            </div>
        </div>
    </body>
</html>