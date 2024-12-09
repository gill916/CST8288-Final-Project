/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.Notification;
import AcademicExchangePlatform.model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the NotificationDAO interface.
 * Provides methods to create, update, retrieve, and manage notifications.
 */
public class NotificationDAOImpl implements NotificationDAO {
    private static NotificationDAOImpl instance;
    
    private NotificationDAOImpl() {}
    
    public static NotificationDAOImpl getInstance() {
        if (instance == null) {
            synchronized(NotificationDAOImpl.class) {
                if (instance == null) {
                    instance = new NotificationDAOImpl();
                }
            }
        }
        return instance;
    }
    /**
     * Creates a new notification.
     * @param notification The Notification object to create
     * @return true if creation was successful, false otherwise
     */
    @Override
    public boolean createNotification(Notification notification) {
        String query = "INSERT INTO notifications (userId, title, message, type, isRead, createdAt) " +
                      "VALUES (?, ?, ?, ?, false, NOW())";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getTitle());
            stmt.setString(3, notification.getMessage());
            
            // Map notification type to valid ENUM value
            String mappedType = mapNotificationType(notification.getType());
            stmt.setString(4, mappedType);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Debug - Notification creation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Maps a notification type to a valid ENUM value.
     * @param originalType The original notification type string
     * @return The mapped notification type string
     */
    private String mapNotificationType(String originalType) {
        switch (originalType.toUpperCase()) {
            case "NEW_APPLICATION":
                return "SYSTEM";
            case "APPLICATION_STATUS":
                return "APPLICATION_STATUS";
            case "DEADLINE_REMINDER":
                return "DEADLINE_REMINDER";
            default:
                return "SYSTEM";
        }
    }

    /**
     * Retrieves all notifications for a specific user.
     * @param userId The ID of the user whose notifications to retrieve
     * @return List of Notification objects
     */
    @Override
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE userId = ? ORDER BY createdAt DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notifications;
    }

    /**
     * Retrieves all unread notifications for a specific user.
     * @param userId The ID of the user whose unread notifications to retrieve
     * @return List of Notification objects that are unread
     */
    @Override
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications WHERE userId = ? AND isRead = false ORDER BY dateSent DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notifications;
    }

    /**
     * Marks a notification as read.
     * @param notificationId The ID of the notification to mark as read
     * @return true if marking was successful, false otherwise
     */
    @Override
    public boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET isRead = true WHERE notificationId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Marks all notifications for a user as read.
     * @param userId The ID of the user whose notifications to mark as read
     * @return true if marking was successful, false otherwise
     */
    @Override
    public boolean markAllAsRead(int userId) {
        String query = "UPDATE notifications SET isRead = true WHERE userId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a specific notification.
     * @param notificationId The ID of the notification to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteNotification(int notificationId) {
        String query = "DELETE FROM notifications WHERE notificationId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the count of unread notifications for a user.
     * @param userId The ID of the user whose unread notifications count to retrieve
     * @return The count of unread notifications
     */
    @Override
    public int getUnreadCount(int userId) {
        String query = "SELECT COUNT(*) FROM notifications WHERE userId = ? AND isRead = false";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    /**
     * Extracts a Notification object from a ResultSet.
     * @param rs The ResultSet containing the notification data
     * @return The Notification object
     * @throws SQLException If there is an error accessing the ResultSet
     */
    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notificationId"));
        notification.setUserId(rs.getInt("userId"));
        notification.setMessage(rs.getString("message"));
        notification.setDateSent(rs.getTimestamp("createdAt"));
        notification.setRead(rs.getBoolean("isRead"));
        notification.setType(rs.getString("type"));
        return notification;
    }
}
