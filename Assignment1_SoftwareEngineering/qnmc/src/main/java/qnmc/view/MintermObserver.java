package qnmc.view;

public class MintermObserver implements Observer {

    @Override
    public void update(String minterm) {
        System.out.println("Updated Minterm: " + minterm);
    }
}