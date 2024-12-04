package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.NotificationDAO;
import AcademicExchangePlatform.dao.NotificationDAOImpl;
import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.model.Subject;
import AcademicExchangePlatform.model.Observer;
import AcademicExchangePlatform.model.NotificationManager;
import AcademicExchangePlatform.dbenum.NotificationStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Subject {
    private NotificationDAO notificationDAO = new NotificationDAOImpl();
    private List<Observer> observers = new ArrayList<>();
    private NotificationManager notificationManager = new NotificationManager();

    @Override
    public void registerObserver(Observer observer) {
        notificationManager.registerObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        notificationManager.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String message) {
        notificationManager.notifyObservers(message);
    }

    public void sendNotification(int userId, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setDateSent(LocalDateTime.now());
        notification.setStatus(NotificationStatus.Unread);
        notificationDAO.createNotification(notification);
        notifyObservers(message);
    }

    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    public void markNotificationAsRead(int notificationId) {
        notificationDAO.updateNotificationStatus(notificationId, NotificationStatus.Read);
    }

    public void deleteNotification(int notificationId) {
        notificationDAO.deleteNotification(notificationId);
    }
}
