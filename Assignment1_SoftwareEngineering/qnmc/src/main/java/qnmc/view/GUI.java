package qnmc.view;

import qnmc.controller.QuineController;
import qnmc.model.GetMintermList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel mainPanel;
    private JLabel mintermLabel;
    private JTextField mintermInputField;
    private JButton nextButton;
    private JTextArea resultTextArea;
    private JButton calculateButton;
    private final int bitCount;
    public static Set<String> mintermSet;
    private String currentInput;
    private final QuineController quineController;

    public GUI(int bitCount) {
        super("Quine McCluskey Prime Implicant Generator");

        this.bitCount = bitCount;
        mintermSet = GetMintermList.getMin();
        quineController = new QuineController(this);

        setLayout(null);
        setSize(550, 500);
        setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, 500, 500);
        mainPanel.setLayout(null);

        initializeMenuBar();
        initializeComponents();

        setVisible(true);
        add(mainPanel);
    }

    private void initializeMenuBar() {
        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }

    private void initializeComponents() {
        mintermLabel = createLabel("Enter Minterm list: ", 50, 100, 150, 30);
        mintermInputField = createTextField(50, 140, 70, 30);
        mintermInputField.addKeyListener(new InputValidationListener());

        nextButton = createButton("Next", 140, 140, 70, 30);
        nextButton.addActionListener((ActionEvent event) -> {
            mintermInputField.setText("");
            quineController.handleNextButtonClick(currentInput);
        });

        resultTextArea = new JTextArea();
        resultTextArea.setBounds(50, 200, 300, 200);
        resultTextArea.setEditable(false);
        mainPanel.add(resultTextArea);

        calculateButton = createButton("Calculate", 400, 250, 100, 50);
        calculateButton.addActionListener((ActionEvent event) -> quineController.handleCalculateButtonClick());

        mainPanel.add(mintermLabel);
        mainPanel.add(mintermInputField);
        mainPanel.add(nextButton);
        mainPanel.add(resultTextArea);
        mainPanel.add(calculateButton);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Verdana", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void updateResultTextArea(String result) {
        resultTextArea.setText(result);
    }

    public int getBitCount() {
        return bitCount;
    }

    public void setMintermSet(String input) {
        mintermSet.add(input); // You could modify this logic based on how you want to handle the minterms.
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Error setting look and feel. Defaulting to system look and feel.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String bitsInput = JOptionPane.showInputDialog("Enter the boolean bits(3 to 5): ");
        int bitCount;
        try {
            bitCount = Integer.parseInt(bitsInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Defaulting to 3.", "Error", JOptionPane.ERROR_MESSAGE);
            bitCount = 3;
        }

        if (bitCount < 3 || bitCount > 5) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Bits should be between 3 and 5.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        GUI gui = new GUI(bitCount);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class InputValidationListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent event) {
            // No action needed for keyPressed
        }

        @Override
        public void keyTyped(KeyEvent event) {
            // No action needed for keyTyped
        }

        @Override
        public void keyReleased(KeyEvent event) {
            String inputText = mintermInputField.getText();
            validateInput(inputText);
        }

        private void validateInput(String inputText) {
            try {
                int input = Integer.parseInt(inputText);
                if (isValidRange(input)) {
                    currentInput = inputText;
                } else {
                    showErrorMessage("Number should be within 0 to " + ((int) Math.pow(2, bitCount) - 1));
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid input. Please enter a valid number.");
            }
        }

        private boolean isValidRange(int input) {
            return input >= 0 && input <= (int) Math.pow(2, bitCount) - 1;
        }
    }
}