package AcademicExchangePlatform.model;

public abstract class NotificationDecorator implements Subject {
    protected Subject wrappedSubject;

    public NotificationDecorator(Subject wrappedSubject) {
        this.wrappedSubject = wrappedSubject;
    }

    @Override
    public void registerObserver(Observer observer) {
        wrappedSubject.registerObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        wrappedSubject.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String message) {
        wrappedSubject.notifyObservers(message);
    }
}
