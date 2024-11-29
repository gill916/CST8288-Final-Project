package AcademicExchangePlatform.model;

public class SMSNotificationDecorator extends NotificationDecorator {
    public SMSNotificationDecorator(Subject wrappedNotifier) {
        super(wrappedNotifier);
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
