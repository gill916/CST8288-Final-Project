<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="title" value="Dashboard" scope="request"/>

<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="dashboard-header">
        <h2>Welcome, 
            <c:if test="${user.userType == 'PROFESSIONAL'}">
                ${user.firstName} ${user.lastName}
            </c:if>
            <c:if test="${user.userType == 'INSTITUTION'}">
                ${user.institutionName}
            </c:if>
        </h2>
    </div>

    <!-- Professional Dashboard -->
    <c:if test="${user.userType == 'PROFESSIONAL'}">
        <div class="dashboard-grid">
            <!-- Profile Status -->
            <div class="dashboard-card">
                <h3>Profile Completion</h3>
                <div class="progress">
                    <div class="progress-bar" role="progressbar" 
                         style="width: ${user.isProfileComplete() ? '100' : '50'}%">
                        ${user.isProfileComplete() ? 'Complete' : 'Incomplete'}
                    </div>
                </div>
            </div>
            <!-- Applications Overview -->
            <div class="dashboard-card">
                <h3>My Applications</h3>
                <div class="stats">
                    <div class="stat-item">
                        <span class="stat-number">${pendingApplications}</span>
                        <span class="stat-label">Pending</span>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>

<%@ include file="../../common/footer.jsp" %> 