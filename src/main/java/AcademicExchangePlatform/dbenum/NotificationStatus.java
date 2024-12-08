package AcademicExchangePlatform.dbenum;

public enum NotificationStatus {
    UNREAD,
    READ;

    @Override
    public String toString() {
        return name();
    }
}
