/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.model;

/**
 * Observer interface for notification system.
 * Part of the Observer pattern implementation.
 * Defines contract for objects that need to be notified of system events.
 */
public interface NotificationObserver {
    void update(String message, int userId, String type, String entityId);
}
