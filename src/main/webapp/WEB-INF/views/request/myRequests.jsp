<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="title" value="My Requests" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <h2>My Course Requests</h2>
    
    <div class="filter-section">
        <form action="" method="GET" class="form-group">
            <div class="form-group">
                <label for="status">Filter by Status:</label>
                <select id="status" name="status" onchange="this.form.submit()">
                    <option value="">All Statuses</option>
                    <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="ACCEPTED" ${param.status == 'ACCEPTED' ? 'selected' : ''}>Accepted</option>
                    <option value="REJECTED" ${param.status == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                    <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                </select>
            </div>
        </form>
    </div>

    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>Course Title</th>
                    <th>Status</th>
                    <th>Request Date</th>
                    <th>Decision Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestsByUserId}" var="request">
                    <tr>
                        <td>${request.courseTitle}</td>
                        <td><span class="status-${fn:toLowerCase(request.status)}">${request.status}</span></td>
                        <td>${request.requestDate}</td>
                        <td>${request.decisionDate}</td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/course/view/${request.courseId}" 
                               class="btn btn-secondary">View Course</a>
                            
                            <c:if test="${request.status == 'PENDING'}">
                                <form action="${pageContext.request.contextPath}/request/cancel" method="POST" 
                                      style="display: inline;">
                                    <input type="hidden" name="requestId" value="${request.requestId}">
                                    <button type="submit" class="btn btn-danger" 
                                            onclick="return confirm('Are you sure you want to cancel this request?')">
                                        Cancel Request
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="?page=${currentPage - 1}&status=${param.status}" class="btn btn-secondary">&laquo; Previous</a>
        </c:if>
        <c:if test="${hasNextPage}">
            <a href="?page=${currentPage + 1}&status=${param.status}" class="btn btn-secondary">Next &raquo;</a>
        </c:if>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 