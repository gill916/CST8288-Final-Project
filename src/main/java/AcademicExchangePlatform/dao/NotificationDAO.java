package AcademicExchangePlatform.dao;

import java.util.List;
import AcademicExchangePlatform.model.Notification;

public interface NotificationDAO {
    void createNotification(Notification notification);
    List<Notification> getNotificationsByUserId(int userId);
    void updateNotificationStatus(int notificationId, String status);
    void deleteNotification(int notificationId);
}