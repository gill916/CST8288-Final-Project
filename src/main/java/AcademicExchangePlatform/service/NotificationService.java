package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.NotificationDAO;
import AcademicExchangePlatform.dao.NotificationDAOImpl;
import AcademicExchangePlatform.model.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class for managing notifications.
 * Implements the Subject interface for notification distribution.
 * Manages notification creation, retrieval, and processing.
 * Uses decorators for email and SMS notification functionality.
 */
public class NotificationService implements Subject {
    private static NotificationService instance;
    private final NotificationDAO notificationDAO;
    private final List<NotificationObserver> observers = new ArrayList<>();
    private Subject emailDecorator;
    private Subject smsDecorator;

    /**
     * Private constructor for singleton pattern.
     * Initializes notificationDAO and decorators.
     */
    private NotificationService() {
        this.notificationDAO = NotificationDAOImpl.getInstance();
        // Initialize decorators
        this.emailDecorator = new EmailNotificationDecorator(this);
        this.smsDecorator = new SMSNotificationDecorator(emailDecorator);
    }

    /**
     * Gets the singleton instance of NotificationService.
     * @return The NotificationService instance
     */
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

    /**
     * Registers an observer to receive notifications.
     * @param observer The observer to register
     */
    @Override
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     * @param observer The observer to remove
     */
    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about an event.
     * @param message The notification message
     * @param userId The target user ID
     * @param type The notification type
     * @param entityId Reference to the related entity
     */
    @Override
    public void notifyObservers(String message, int userId, String type, String entityId) {
        for (NotificationObserver observer : observers) {
            observer.update(message, userId, type, entityId);
        }
    }

    /**
     * Creates a new notification and stores it in the database.
     * @param notification The notification to create
     */
    public void createNotification(Notification notification) {
        notificationDAO.createNotification(notification);
    }

    // Database operations
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    /**
     * Retrieves unread notifications for a specific user.
     * @param userId The ID of the user to retrieve notifications for
     * @return List of unread notifications
     */
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.getUnreadNotificationsByUserId(userId);
    }

    /**
     * Marks a notification as read.
     * @param notificationId The ID of the notification to mark as read
     * @return True if the notification was marked as read, false otherwise
     */
    public boolean markAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    /**
     * Marks all notifications for a user as read.
     * @param userId The ID of the user to mark notifications for
     * @return True if all notifications were marked as read, false otherwise
     */
    public boolean markAllAsRead(int userId) {
        return notificationDAO.markAllAsRead(userId);
    }

    /**
     * Deletes a notification from the database.
     * @param notificationId The ID of the notification to delete
     * @return True if the notification was deleted, false otherwise
     */
    public boolean deleteNotification(int notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }

    /**
     * Retrieves the count of unread notifications for a user.
     * @param userId The ID of the user to retrieve unread count for
     * @return The count of unread notifications
     */
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

    /**
     * Notifies about a new application for a course.
     * @param application The course application
     * @param course The course the application is for
     */
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

    /**
     * Notifies about an approaching application deadline for a course.
     * @param course The course with the approaching deadline
     * @param daysRemaining The number of days remaining until the deadline
     */
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

    /**
     * Notifies about the need to complete a user's profile.
     * @param userId The ID of the user to notify
     */
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

    /**
     * Notifies about a change in a course's status.
     * @param course The course whose status has changed
     * @param oldStatus The previous status of the course
     * @param newStatus The new status of the course
     */
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

    /**
     * Notifies about an update to a request.
     * @param userId The ID of the user to notify
     * @param message The notification message
     * @param type The notification type
     * @param referenceId Reference to the related entity
     */
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
