package qnmc;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel mainPanel;
    private final JLabel mintermLabel;
    private JTextField mintermInputField;
    private final JButton nextButton;
    private final JTextArea resultTextArea;
    private final JButton calculateButton;
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

        MenuBar menuBar = new MenuBar();
        setJMenuBar(menuBar);

        mintermLabel = new JLabel("Enter Minterm list: ");
        mintermLabel.setBounds(50, 100, 150, 30);
        mintermLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        mainPanel.add(mintermLabel);

        mintermInputField = new JTextField();
        mintermInputField.setBounds(50, 140, 70, 30);

        mintermInputField.addKeyListener(new KeyListener() {
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
        });
        mainPanel.add(mintermInputField);

        nextButton = new JButton("Next");
        nextButton.setBounds(140, 140, 70, 30);
        nextButton.addActionListener((ActionEvent event) -> {
            mintermInputField.setText("");
            mintermList.setMinList(currentInput);
        });
        mainPanel.add(nextButton);

        resultTextArea = new JTextArea();
        resultTextArea.setBounds(50, 200, 300, 200);
        resultTextArea.setEditable(false);
        mainPanel.add(resultTextArea);

        calculateButton = new JButton("Calculate");
        calculateButton.setBounds(400, 250, 100, 50);
        calculateButton.addActionListener((ActionEvent event) -> calculateAction());
        mainPanel.add(calculateButton);

        setVisible(true);
        add(mainPanel);
    }

    private void validateInput(String inputText) {
        try {
            int input = Integer.parseInt(inputText);
            int maxValue = (int) Math.pow(2, bitCount) - 1;
            if (input < 0 || input > maxValue) {
                showErrorMessage("Number should be within 0 to " + maxValue, "Error");
            } else {
                currentInput = inputText;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter a valid number.", "Error");
        }
    }

    private void calculateAction() {
        try {
            Quine quine = new Quine();
            for (String minterm : mintermSet) {
                quine.addMinTerms(getBinary(minterm));
            }
            quine.simplify();
            resultTextArea.setText(quine.toString());
        } catch (ExceptionQuine e) {
            showErrorMessage("An error occurred during the Quine-McCluskey operation. Please check your input.", "Error");
        } catch (Exception e) {
            showErrorMessage("An unexpected error occurred. Please try again.", "Error");
        }
    }

    private void showErrorMessage(String message, String error) {
        JOptionPane.showMessageDialog(null, message, error, JOptionPane.ERROR_MESSAGE);
    }

    private String getBinary(String input) {
        int maxIndex = (int) Math.pow(2, bitCount) - 1;
        String[] binaryValues = new String[maxIndex + 1];

        for (int i = 0; i <= maxIndex; i++) {
            binaryValues[i] = String.format("%0" + bitCount + "d", Integer.valueOf(Integer.toBinaryString(i)));
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
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
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
}