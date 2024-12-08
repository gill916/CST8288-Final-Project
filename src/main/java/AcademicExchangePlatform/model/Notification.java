package AcademicExchangePlatform.model;

import java.util.Date;

public class Notification {
    private int notificationId;
    private int userId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private Date dateSent;

    // Constructors
    public Notification() {}

    public Notification(int userId, String message, String type, String referenceId) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.title = type; // Using type as title temporarily
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
