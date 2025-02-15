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

    private JTextArea resultTextArea;
    private final JButton calculateButton;

    static public int bitCount = 0;
    static public Set<String> mintermSet;
    public String currentInput;
    GetMintermList mintermList = new GetMintermList();

    // Helper method to generate binary representation based on input size
    static public String getBinary(String input, int bits) {
        int maxIndex = (int) Math.pow(2, bits) - 1; // Calculate the max value for the given bit size
        String[] binaryValues = new String[maxIndex + 1];
        
        for (int i = 0; i <= maxIndex; i++) {
            binaryValues[i] = String.format("%0" + bits + "d", Integer.valueOf(Integer.toBinaryString(i)));
        }

        try {
            int index = Integer.parseInt(input);  // Handling NumberFormatException
            if (index < 0 || index > maxIndex) {
                throw new ArrayIndexOutOfBoundsException();  // Throw ArrayIndexOutOfBoundsException if index is out of range
            }
            return binaryValues[index];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 0 and " + maxIndex + ".", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public GUI() {
        super("Quine McCluskey Prime Implicant Generator");

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

        // Consolidate all number validation
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
                try {
                    int bits = MenuBar.bits;
                    int input = Integer.parseInt(inputText); // Consolidate all number validation

                    if (bits == 3 && (input < 0 || input > 7)) {
                        showErrorMessage("Number should be within 0 to 7");
                    } else if (bits == 4 && (input < 0 || input > 15)) {
                        showErrorMessage("Number should be within 0 to 15");
                    } else if (bits == 5 && (input < 0 || input > 31)) {
                        showErrorMessage("Number should be within 0 to 31");
                    } else {
                        currentInput = inputText;
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Invalid input. Please enter a valid number.");
                }
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
        calculateButton.addActionListener((ActionEvent event) -> {
            try {
                Quine quine = new Quine();
                mintermSet = GetMintermList.getMin();
                for (String minterm : mintermSet) {
                    // Refactor repeated binary conversion based on bits
                    quine.addTerm(getBinary(minterm, MenuBar.bits));
                }
                
                quine.simplify();
                resultTextArea.setText(quine.toString());
            } catch (ExceptionQuine e) {
                JOptionPane.showMessageDialog(null, "An error occurred during the Quine-McCluskey operation. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(calculateButton);

        setVisible(true);
        add(mainPanel);
    }

    // Helper method to show error messages
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
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
        try {
            MenuBar.bits = Integer.parseInt(bitsInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Defaulting to 2.", "Error", JOptionPane.ERROR_MESSAGE);
            MenuBar.bits = 2;
        }

        if (MenuBar.bits < 3 || MenuBar.bits > 5) {
            JOptionPane.showMessageDialog(null,
                    "Wrong input. Press File and then NEW", "Error",
                    JOptionPane.ERROR_MESSAGE, null);
        }

        GUI gui = new GUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}