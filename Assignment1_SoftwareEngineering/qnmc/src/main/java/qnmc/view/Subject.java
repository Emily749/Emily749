package qnmc.view;

public interface Subject {
    void registerObserver(Observer observer);
    void notifyObservers();
}