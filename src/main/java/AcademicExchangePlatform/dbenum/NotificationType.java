/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the type of notification.
 */
public enum NotificationType {
    APPLICATION_STATUS,
    NEW_COURSE,
    DEADLINE_REMINDER,
    SYSTEM;

    @Override
    public String toString() {
        return name();
    }
} 