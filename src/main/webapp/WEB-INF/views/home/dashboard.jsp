<%--
    Author: Jiajun Cai
    Student Number: 041127296
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="title" value="Dashboard" scope="request"/>

<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="dashboard-header">
        <h2>Welcome, 
            <c:if test="${sessionScope.user.userType == 'PROFESSIONAL'}">
                ${sessionScope.user.firstName} ${sessionScope.user.lastName}
            </c:if>
            <c:if test="${sessionScope.user.userType == 'INSTITUTION'}">
                ${sessionScope.user.institutionName}
            </c:if>
        </h2>
    </div>

    <!-- Professional Dashboard -->
    <c:if test="${sessionScope.user.userType == 'PROFESSIONAL'}">
        <div class="dashboard-grid">
            <!-- Profile Status -->
            <div class="dashboard-card">
                <h3>Profile Completion</h3>
                <div class="progress">
                    <div class="progress-bar ${sessionScope.user.profileComplete ? 'bg-success' : 'bg-warning'}" 
                         role="progressbar" 
                         style="width: ${sessionScope.user.profileComplete ? '100' : '50'}%" 
                         aria-valuenow="${sessionScope.user.profileComplete ? '100' : '50'}" 
                         aria-valuemin="0" 
                         aria-valuemax="100">
                        ${sessionScope.user.profileComplete ? 'Complete' : 'Incomplete'}
                    </div>
                </div>
                <c:if test="${!sessionScope.user.profileComplete}">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary mt-2">Complete Profile</a>
                </c:if>
            </div>
            <!-- Applications Overview -->
            <div class="dashboard-card">
                <h3>My Applications</h3>
                <div class="stats">
                    <div class="stat-item">
                        <span class="stat-number">${applicationStats.pending}</span>
                        <span class="stat-label">Pending</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number">${applicationStats.accepted}</span>
                        <span class="stat-label">Accepted</span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number">${applicationStats.rejected}</span>
                        <span class="stat-label">Rejected</span>
                    </div>
                </div>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/application/my-applications" 
                       class="btn btn-primary">View All Applications</a>
                </div>
            </div>
        </div>
    </c:if>
</div>

<%@ include file="../../common/footer.jsp" %> 
<%@ include file="../../common/footer.jsp" %> 