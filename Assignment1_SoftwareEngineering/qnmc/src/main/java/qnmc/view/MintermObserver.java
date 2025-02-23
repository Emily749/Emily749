package qnmc.view;

public class MintermObserver implements Observer {
    private final GUI gui;

    public MintermObserver(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void update(String currentInput) {
        gui.updateMintermDisplay();
    }
}
