package AcademicExchangePlatform.model;

public class EmailNotificationDecorator extends NotificationDecorator {
    public EmailNotificationDecorator(Notification decoratedNotification) {
        super(decoratedNotification);
    }

    @Override
    public void sendNotification() {
        super.sendNotification();
        sendEmail();
    }

    private void sendEmail() {
        System.out.println("Email sent: " + decoratedNotification.getMessage());
    }
}