package AcademicExchangePlatform.model;

/**
 * Abstract decorator class for notification systems.
 * Extends the Subject interface to add notification functionality.
 * Part of the Decorator pattern implementation for notification system.
 */
public abstract class NotificationDecorator implements Subject {
    protected Subject wrappedSubject;

    public NotificationDecorator(Subject wrappedSubject) {
        this.wrappedSubject = wrappedSubject;
    }

    @Override
    public void registerObserver(NotificationObserver observer) {
        wrappedSubject.registerObserver(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        wrappedSubject.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String message, int userId, String type, String entityId) {
        wrappedSubject.notifyObservers(message, userId, type, entityId);
    }
}
