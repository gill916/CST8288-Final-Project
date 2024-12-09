package AcademicExchangePlatform.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface defining the contract for notification subjects.
 * Part of the Observer pattern implementation.
 * Provides methods for observer management and notification distribution.
 */
public abstract class NotificationSubject {
    private List<NotificationObserver> observers = new ArrayList<>();

    public void attach(NotificationObserver observer) {
        observers.add(observer);
    }

    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(String message, int userId, String type, String entityId) {
        for (NotificationObserver observer : observers) {
            observer.update(message, userId, type, entityId);
        }
    }
}
