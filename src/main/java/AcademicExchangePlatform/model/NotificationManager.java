package AcademicExchangePlatform.model;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Additional methods to interact with notifications
    public void createNotification(Notification notification) {
        // Store notification logic here
        notifyObservers("New notification created: " + notification.getMessage());
    }
}
