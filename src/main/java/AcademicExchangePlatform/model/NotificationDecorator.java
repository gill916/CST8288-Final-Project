package AcademicExchangePlatform.model;

public abstract class NotificationDecorator extends Notification {
    protected Notification decoratedNotification;

    public NotificationDecorator(Notification decoratedNotification) {
        this.decoratedNotification = decoratedNotification;
    }

    @Override
    public void notifyObservers(String message) {
        decoratedNotification.notifyObservers(message);
    }
}