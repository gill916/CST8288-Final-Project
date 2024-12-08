<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" value="Notifications" scope="request"/>
<%@ include file="../../common/header.jsp" %>

<div class="container">
    <div class="notifications-section">
        <div class="section-header d-flex justify-content-between align-items-center">
            <h2>Notifications</h2>
            <c:if test="${not empty notifications}">
                <form action="${pageContext.request.contextPath}/notification" method="post" class="d-inline">
                    <input type="hidden" name="action" value="markAllRead">
                    <button type="submit" class="btn btn-secondary">Mark All as Read</button>
                </form>
            </c:if>
        </div>

        <div class="notifications-list">
            <c:choose>
                <c:when test="${empty notifications}">
                    <div class="alert alert-info">No notifications to display.</div>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${notifications}" var="notification">
                        <div class="notification-item ${notification.read ? '' : 'unread'}">
                            <div class="notification-content">
                                <div class="notification-header">
                                    <span class="badge bg-${notification.type == 'APPLICATION_STATUS' ? 'primary' : 
                                                         notification.type == 'NEW_COURSE' ? 'success' : 
                                                         notification.type == 'DEADLINE_REMINDER' ? 'warning' : 'info'}">
                                        ${notification.type}
                                    </span>
                                    <span class="notification-date">
                                        <fmt:formatDate value="${notification.dateSent}" 
                                                      pattern="MMM d, yyyy HH:mm"/>
                                    </span>
                                </div>
                                <p class="notification-message">${notification.message}</p>
                            </div>
                            
                            <div class="notification-actions">
                                <c:if test="${not notification.read}">
                                    <form action="${pageContext.request.contextPath}/notification" 
                                          method="post" class="d-inline">
                                        <input type="hidden" name="action" value="markRead">
                                        <input type="hidden" name="notificationId" 
                                               value="${notification.notificationId}">
                                        <button type="submit" class="btn btn-sm btn-outline-primary">
                                            Mark as Read
                                        </button>
                                    </form>
                                </c:if>
                                <form action="${pageContext.request.contextPath}/notification" 
                                      method="post" class="d-inline">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="notificationId" 
                                           value="${notification.notificationId}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" 
                                            onclick="return confirm('Delete this notification?')">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../../common/footer.jsp" %> 