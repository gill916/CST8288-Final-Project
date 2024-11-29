package AcademicExchangePlatform.model;

public abstract class NotificationDecorator extends Notification {
    protected Notification decoratedNotification;

    public NotificationDecorator(Notification decoratedNotification) {
        this.decoratedNotification = decoratedNotification;
    }

    public void sendNotification() {
        // Base behavior
        System.out.println("Sending notification: " + decoratedNotification.getMessage());
    }
}
