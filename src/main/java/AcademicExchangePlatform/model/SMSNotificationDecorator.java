package AcademicExchangePlatform.model;

/**
 * Decorator class for adding SMS notification functionality.
 * Extends NotificationDecorator to add SMS-specific notification behavior.
 * Part of the Decorator pattern implementation for notification system.
 */
public class SMSNotificationDecorator extends NotificationDecorator {
    public SMSNotificationDecorator(Subject wrappedNotifier) {
        super(wrappedNotifier);
    }

    @Override
    public void notifyObservers(String message, int userId, String type, String entityId) {
        super.notifyObservers(message, userId, type, entityId);
        sendSMS(message);
    }

    private void sendSMS(String message) {
        System.out.println("SMS sent: " + message);
    }
}
