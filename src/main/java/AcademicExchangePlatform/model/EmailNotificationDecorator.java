package AcademicExchangePlatform.model;

public class EmailNotificationDecorator extends NotificationDecorator {
    public EmailNotificationDecorator(Notification decoratedNotification) {
        super(decoratedNotification);
    }

    @Override
    public void notifyObservers(String message) {
        super.notifyObservers(message);
        sendEmail(message);
    }

    private void sendEmail(String message) {
        System.out.println("Email sent: " + message);
    }
}