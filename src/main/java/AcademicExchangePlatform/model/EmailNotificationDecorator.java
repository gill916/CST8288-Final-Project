package AcademicExchangePlatform.model;

public class EmailNotificationDecorator extends NotificationDecorator {
    public EmailNotificationDecorator(Subject wrappedSubject) {
        super(wrappedSubject);
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