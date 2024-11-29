package AcademicExchangePlatform.model;

public class SMSNotificationDecorator extends NotificationDecorator {
    public SMSNotificationDecorator(Notification decoratedNotification) {
        super(decoratedNotification);
    }

    @Override
    public void notifyObservers(String message) {
        super.notifyObservers(message);
        sendSMS(message);
    }

    private void sendSMS(String message) {
        System.out.println("SMS sent: " + message);
    }
}
