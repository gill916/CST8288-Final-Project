package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the read status of notifications.
 */
public enum NotificationStatus {
    UNREAD,
    READ;

    @Override
    public String toString() {
        return name();
    }
}
