package AcademicExchangePlatform.service;




import AcademicExchangePlatform.dao.NotificationDAO;
import AcademicExchangePlatform.dao.NotificationDAOImpl;
import AcademicExchangePlatform.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationService {
    private NotificationDAO notificationDAO = new NotificationDAOImpl();

    public void sendNotification(int userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setDateSent(LocalDateTime.now());
        notification.setStatus("Unread");
        notificationDAO.createNotification(notification);
    }

    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    public void markNotificationAsRead(int notificationId) {
        notificationDAO.updateNotificationStatus(notificationId, "Read");
    }

    public void deleteNotification(int notificationId) {
        notificationDAO.deleteNotification(notificationId);
    }
}
