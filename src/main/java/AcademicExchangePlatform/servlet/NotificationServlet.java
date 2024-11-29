package AcademicExchangePlatform.servlet;

import AcademicExchangePlatform.service.NotificationService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NotificationServlet extends HttpServlet {
    private NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        req.setAttribute("notifications", notificationService.getUserNotifications(userId));
        req.getRequestDispatcher("/notifications.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        String message = req.getParameter("message");
        notificationService.sendNotification(userId, message);
        resp.sendRedirect("/notifications?userId=" + userId);
    }
}
