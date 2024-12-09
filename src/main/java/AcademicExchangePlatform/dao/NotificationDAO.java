package AcademicExchangePlatform.dao;

import java.util.List;
import AcademicExchangePlatform.model.Notification;

/**
 * Data Access Object interface for managing notifications in the Academic Exchange Platform.
 * Provides methods to create, retrieve, and manage notifications.
 */
public interface NotificationDAO {
    /**
     * Creates a new notification in the database.
     * @param notification The Notification object to be persisted
     * @return true if creation was successful, false otherwise
     */
    boolean createNotification(Notification notification);

    /**
     * Retrieves all notifications for a specific user.
     * @param userId The ID of the user whose notifications to retrieve
     * @return List of Notification objects belonging to the user
     */
    List<Notification> getNotificationsByUserId(int userId);

    /**
     * Retrieves all unread notifications for a specific user.
     * @param userId The ID of the user whose unread notifications to retrieve
     * @return List of Notification objects that are unread
     */
    List<Notification> getUnreadNotificationsByUserId(int userId);

    /**
     * Marks a notification as read.
     * @param notificationId The ID of the notification to mark as read
     * @return true if marking was successful, false otherwise
     */
    boolean markAsRead(int notificationId);

    /**
     * Marks all notifications for a user as read.
     * @param userId The ID of the user whose notifications to mark as read
     * @return true if marking was successful, false otherwise
     */
    boolean markAllAsRead(int userId);

    /**
     * Deletes a specific notification.
     * @param notificationId The ID of the notification to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteNotification(int notificationId);

    /**
     * Retrieves the count of unread notifications for a user.
     * @param userId The ID of the user whose unread notifications count to retrieve
     * @return The count of unread notifications
     */
    int getUnreadCount(int userId);
}