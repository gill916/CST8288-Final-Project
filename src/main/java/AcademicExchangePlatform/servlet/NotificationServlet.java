/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.service.NotificationService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling notifications.
 * Manages the display and management of notifications for users.
 */
@WebServlet(urlPatterns = {"/notification", "/notification/*"})
public class NotificationServlet extends HttpServlet {
    private final NotificationService notificationService = NotificationService.getInstance();

    /**
     * Handles GET requests for notification management.
     * Validates user session and displays notifications.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If the request cannot be handled
     * @throws IOException If an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj == null) {
            System.out.println("DEBUG: No userId in session");
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        int userId = (int) userIdObj;
        String pathInfo = request.getPathInfo();

        if ("/count".equals(pathInfo)) {
            // AJAX endpoint for getting unread count
            response.setContentType("application/json");
            response.getWriter().write("{\"count\":" + notificationService.getUnreadCount(userId) + "}");
            return;
        }

        try {
            // Get notifications for display
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            request.setAttribute("notifications", notifications);
            request.setAttribute("unreadCount", notificationService.getUnreadCount(userId));
            
            request.getRequestDispatcher("/WEB-INF/views/notification/notifications.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            System.out.println("DEBUG: Error in NotificationServlet: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Error loading notifications: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    /**
     * Handles POST requests for notification management.
     * Validates user session and processes notification actions.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws ServletException If the request cannot be handled
     * @throws IOException If an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        int userId = (int) userIdObj;
        String action = request.getParameter("action");

        switch (action) {
            case "markRead":
                int notificationId = Integer.parseInt(request.getParameter("notificationId"));
                notificationService.markAsRead(notificationId);
                break;

            case "markAllRead":
                notificationService.markAllAsRead(userId);
                break;

            case "delete":
                notificationId = Integer.parseInt(request.getParameter("notificationId"));
                notificationService.deleteNotification(notificationId);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }

        // If it's an AJAX request, send JSON response
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true}");
        } else {
            // Otherwise redirect back to notifications page
            response.sendRedirect(request.getContextPath() + "/notification");
        }
    }
}
