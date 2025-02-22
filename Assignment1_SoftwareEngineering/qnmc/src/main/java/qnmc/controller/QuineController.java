package qnmc.controller;

import qnmc.model.ExceptionQuine;
import qnmc.model.GetMintermList;
import qnmc.model.Quine;
import qnmc.view.GUI;

import java.util.Set;

public class QuineController {

    private final GUI gui;
    private final Set<String> mintermSet;

    public QuineController(GUI gui) {
        this.gui = gui;
        this.mintermSet = GetMintermList.getMin();
    }

    public void handleNextButtonClick(String currentInput) {
        try {
            // Validate input and add it to the minterm list
            gui.setMintermSet(currentInput);
        } catch (Exception e) {
            gui.showErrorMessage("Invalid input. Please enter a valid minterm.");
        }
    }

    public void handleCalculateButtonClick() {
        try {
            Quine quine = new Quine();
            processMinterms(quine);
            quine.simplifyMinterms();
            updateResult(quine);
        } catch (ExceptionQuine e) {
            gui.showErrorMessage("An error occurred during the Quine-McCluskey operation. Please check your input.");
        } catch (Exception e) {
            gui.showErrorMessage("An unexpected error occurred. Please try again.");
        }
    }

    private void processMinterms(Quine quine) throws ExceptionQuine {
        for (String minterm : mintermSet) {
            quine.addMinTerms(getBinary(minterm));
        }
    }

    private void updateResult(Quine quine) {
        gui.updateResultTextArea(quine.toString());
    }

    private String getBinary(String input) {
        int bitCount = gui.getBitCount();
        int maxIndex = (int) Math.pow(2, bitCount) - 1;
        String[] binaryValues = new String[maxIndex + 1];

        if (binaryValues[0] == null) {
            for (int i = 0; i <= maxIndex; i++) {
                binaryValues[i] = String.format("%0" + bitCount + "d", Integer.valueOf(Integer.toBinaryString(i)));
            }
        }

        try {
            int index = Integer.parseInt(input);
            if (index < 0 || index > maxIndex) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return binaryValues[index];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            gui.showErrorMessage("Invalid input. Please enter a number between 0 and " + maxIndex + ".");
            return null;
        }
    }
}