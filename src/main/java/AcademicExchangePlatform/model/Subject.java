/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.model;
/**
 * Interface defining the contract for notification subjects.
 * Part of the Observer pattern implementation.
 * Provides methods for observer management and notification distribution.
 */
public interface Subject {
    void registerObserver(NotificationObserver observer);
    void removeObserver(NotificationObserver observer);
    void notifyObservers(String message, int userId, String type, String entityId);
}
