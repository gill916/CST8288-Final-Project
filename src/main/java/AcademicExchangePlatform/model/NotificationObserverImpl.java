package AcademicExchangePlatform.model;

import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.service.NotificationService;

public class NotificationObserverImpl implements NotificationObserver {
    @Override
    public void update(String message, int userId, String type, String entityId) {
        Notification notification = new Notification(
            userId,
            type,
            message,
            type,
            entityId
        );
        
        NotificationService.getInstance().createNotification(notification);
    }
}
