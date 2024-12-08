package AcademicExchangePlatform.model;

import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.service.NotificationService;

public class NotificationObserverImpl implements NotificationObserver {
    private final String observerType;
    
    public NotificationObserverImpl(String type) {
        this.observerType = type;
    }
    
    @Override
    public void update(String message, int userId, String type, String entityId) {
        if (type.equals(observerType) || observerType.equals("ALL")) {
            Notification notification = new Notification(
                userId,
                "Notification Update",
                message,
                type,
                entityId
            );
            NotificationService.getInstance().createNotification(notification);
        }
    }
}
