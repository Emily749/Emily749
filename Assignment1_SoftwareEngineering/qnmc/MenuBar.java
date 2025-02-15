package qnmc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class MenuBar extends JMenuBar {

    private static final long serialVersionUID = 1L;

    public static int bits;

    private String developer1 = "Jane Smith";
    private String developer2 = "John Doe";
    private String developer3 = "Ashok Kumar";
    private String objectiveMessage = "The Quine McCluskey algorithm (or the method of prime implicants) "
            + "\nis a method used for minimization of boolean functions which was developed by W.V. "
            + "\nQuine and Edward J. McCluskey. It is functionally identical to Karnaugh mapping, "
            + "\nbut the tabular form makes it more efficient for use in computer algorithms, and"
            + "\nit also gives a deterministic way to check that the minimal form of a Boolean "
            + "\nfunction has been reached. It is sometimes referred to as the tabulation method.";

    public MenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New  Ctrl+N", KeyEvent.VK_N);
        fileMenu.add(newMenuItem);
        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (GUI.mintermSet != null) {
                    GUI.mintermSet.clear();
                }

                String s = JOptionPane.showInputDialog("Enter the boolean bits (3 to 5): ");
                try {
                    bits = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    bits = 2;
                }

                if (bits < 3 || bits > 5) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid input. Bits must be between 3 and 5. Please press File and then NEW.",
                            "Error", JOptionPane.ERROR_MESSAGE, null);
                }
            }
        });

        JMenuItem exit = new JMenuItem("Exit  Alt+F4", KeyEvent.VK_N);
        fileMenu.add(exit);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        add(help);

        JMenuItem objectiveItem = new JMenuItem("About Quine McCluskey Algorithm", KeyEvent.VK_O);
        help.add(objectiveItem);
        objectiveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, objectiveMessage,
                        "Quine McCluskey Prime Implicant Generator", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenu authors = new JMenu("About...");
        authors.setMnemonic(KeyEvent.VK_A);
        add(authors);

        JMenuItem jd1 = new JMenuItem("Developer 1", KeyEvent.VK_1);
        authors.add(jd1);
        jd1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, developer1,
                        "Quine McCluskey Prime Implicant Generator", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem jd2 = new JMenuItem("Developer 2", KeyEvent.VK_2);
        authors.add(jd2);
        jd2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, developer2,
                        "Quine McCluskey Prime Implicant Generator", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem jd3 = new JMenuItem("Developer 3", KeyEvent.VK_3);
        authors.add(jd3);
        jd3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, developer3,
                        "Quine McCluskey Prime Implicant Generator", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}