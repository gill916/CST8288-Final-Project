package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.NotificationDAO;
import AcademicExchangePlatform.dao.NotificationDAOImpl;
import AcademicExchangePlatform.model.*;
import java.util.List;
import java.util.ArrayList;

public class NotificationService implements Subject {
    private static NotificationService instance;
    private final NotificationDAO notificationDAO;
    private final List<NotificationObserver> observers = new ArrayList<>();
    private Subject emailDecorator;
    private Subject smsDecorator;

    private NotificationService() {
        this.notificationDAO = NotificationDAOImpl.getInstance();
        // Initialize decorators
        this.emailDecorator = new EmailNotificationDecorator(this);
        this.smsDecorator = new SMSNotificationDecorator(emailDecorator);
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            synchronized(NotificationService.class) {
                if (instance == null) {
                    instance = new NotificationService();
                }
            }
        }
        return instance;
    }

    @Override
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message, int userId, String type, String entityId) {
        for (NotificationObserver observer : observers) {
            observer.update(message, userId, type, entityId);
        }
    }

    public void createNotification(Notification notification) {
        notificationDAO.createNotification(notification);
    }

    // Database operations
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotificationsByUserId(userId);
    }

    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    public boolean markAllAsRead(int userId) {
        return notificationDAO.markAllAsRead(userId);
    }

    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }

    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCount(userId);
    }

    // Business notification methods
    public void notifyApplicationStatusChange(CourseApplication application, String newStatus) {
        Notification notification = new Notification(
            application.getProfessionalId(),
            "Application Status Update",
            String.format("Your application status has been updated to: %s", newStatus),
            "APPLICATION_STATUS",
            String.valueOf(application.getApplicationId())
        );
        createNotification(notification);
    }

    public void notifyNewApplication(CourseApplication application, Course course) {
        Notification notification = new Notification(
            course.getInstitutionId(),
            "New Course Application",
            String.format("New application received for course: %s", course.getCourseTitle()),
            "NEW_APPLICATION",
            String.valueOf(application.getApplicationId())
        );
        createNotification(notification);
    }

    public void notifyApplicationDeadlineApproaching(Course course, int daysRemaining) {
        Notification notification = new Notification(
            course.getInstitutionId(),
            String.format("Deadline Approaching"),
            String.format("Application deadline for %s is approaching (%d days remaining)", 
                course.getCourseTitle(), daysRemaining),
            "DEADLINE_REMINDER",
            String.valueOf(course.getCourseId())
        );
        createNotification(notification);
    }

    public void notifyProfileIncomplete(int userId) {
        Notification notification = new Notification(
            userId,
            "Profile Incomplete",
            "Please complete your profile to enhance your application.",
            "SYSTEM",
            String.valueOf(userId)
        );
        createNotification(notification);
    }

    public void notifyCourseStatusChange(Course course, String oldStatus, String newStatus) {
        Notification notification = new Notification(
            course.getInstitutionId(),
            "Course Status Change",
            String.format("Status of course %s has changed from %s to %s", 
                course.getCourseTitle(), oldStatus, newStatus),
            "SYSTEM",
            String.valueOf(course.getCourseId())
        );
        createNotification(notification);
    }

    public void notifyRequestUpdate(int userId, String message, String type, String referenceId) {
        Notification notification = new Notification(
            userId,
            "Request Update",
            message,
            type,
            referenceId
        );
        createNotification(notification);
    }
}
