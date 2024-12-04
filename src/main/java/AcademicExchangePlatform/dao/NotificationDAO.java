package AcademicExchangePlatform.dao;

import java.util.List;
import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.dbenum.NotificationStatus;
public interface NotificationDAO {
    void createNotification(Notification notification);
    List<Notification> getNotificationsByUserId(int userId);
    void updateNotificationStatus(int notificationId, NotificationStatus status);
    void deleteNotification(int notificationId);
}