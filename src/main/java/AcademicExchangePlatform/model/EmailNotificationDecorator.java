package AcademicExchangePlatform.model;

/**
 * Decorator class for adding email notification functionality.
 * Extends NotificationDecorator to add email-specific notification behavior.
 * Part of the Decorator pattern implementation for notification system.
 */
public class EmailNotificationDecorator extends NotificationDecorator {
    public EmailNotificationDecorator(Subject wrappedSubject) {
        super(wrappedSubject);
    }

    @Override
    public void notifyObservers(String message, int userId, String type, String entityId) {
        super.notifyObservers(message, userId, type, entityId);
        sendEmail(message);
    }

    private void sendEmail(String message) {
        System.out.println("Email sent: " + message);
    }
}