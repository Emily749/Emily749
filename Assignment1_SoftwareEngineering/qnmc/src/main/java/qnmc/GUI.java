package qnmc;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import javax.swing.*;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel mainPanel;
    private  JLabel mintermLabel;
    private JTextField mintermInputField;
    private  JButton nextButton;
    private  JTextArea resultTextArea;
    private  JButton calculateButton;
    private final int bitCount;
    public static Set<String> mintermSet;
    private String currentInput;
    private static final GetMintermList mintermList = new GetMintermList();

    public GUI(int bitCount) {
        super("Quine McCluskey Prime Implicant Generator");

        this.bitCount = bitCount;
        mintermSet = GetMintermList.getMin();

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
            mintermList.setMinList(currentInput);
        });

        resultTextArea = new JTextArea();
        resultTextArea.setBounds(50, 200, 300, 200);
        resultTextArea.setEditable(false);
        mainPanel.add(resultTextArea);

        calculateButton = createButton("Calculate", 400, 250, 100, 50);
        calculateButton.addActionListener((ActionEvent event) -> calculateAction());

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

    private void calculateAction() {
        try {
            Quine quine = new Quine();
            processMinterms(quine);
            quine.simplify();
            updateResult(quine);
        } catch (ExceptionQuine e) {
            showErrorMessage("An error occurred during the Quine-McCluskey operation. Please check your input.", "Error");
        } catch (Exception e) {
            showErrorMessage("An unexpected error occurred. Please try again.", "Error");
        }
    }

    private void processMinterms(Quine quine) throws ExceptionQuine {
        for (String minterm : mintermSet) {
            quine.addMinTerms(getBinary(minterm));
        }
    }

    private void updateResult(Quine quine) {
        resultTextArea.setText(quine.toString());
    }

    private void showErrorMessage(String message, String error) {
        JOptionPane.showMessageDialog(null, message, error, JOptionPane.ERROR_MESSAGE);
    }

    private String getBinary(String input) {
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
            showErrorMessage("Invalid input. Please enter a number between 0 and " + maxIndex + ".", "Error");
            return null;
        }
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
                    showErrorMessage("Number should be within 0 to " + ((int) Math.pow(2, bitCount) - 1), "Error");
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Invalid input. Please enter a valid number.", "Error");
            }
        }

        private boolean isValidRange(int input) {
            return input >= 0 && input <= (int) Math.pow(2, bitCount) - 1;
        }
    }
}