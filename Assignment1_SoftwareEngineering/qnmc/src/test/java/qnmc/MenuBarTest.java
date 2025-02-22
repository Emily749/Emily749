package qnmc;

import java.awt.event.ActionListener;
import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuBarTest {
    private MenuBar menuBar;

    @BeforeEach
    void setUp() {
        menuBar = new MenuBar();
    }

    @Test
    void testGetMenu() {
        assertEquals(3, menuBar.getMenuCount(), "MenuBar needs correct number of menus");

        JMenu fileMenu = menuBar.getMenu(0);
        JMenu helpMenu = menuBar.getMenu(1);
        JMenu aboutMenu = menuBar.getMenu(2);

        assertEquals("File", fileMenu.getText(), "First should be File.");
        assertEquals("Help", helpMenu.getText(), "Second should be Help.");
        assertEquals("About...", aboutMenu.getText(), "Third should be About...");
    }

    @Test
    void testFileMenu() {
        JMenu fileMenu = menuBar.getMenu(0);
        assertNotNull(fileMenu, "No file menu.");
        assertEquals(2, fileMenu.getItemCount(), "File menu needs to contain 2 items.");

        JMenuItem newMenuItem = fileMenu.getItem(0);
        JMenuItem exitMenuItem = fileMenu.getItem(1);

        assertEquals("New  Ctrl+N", newMenuItem.getText(), "First should be 'New  Ctrl+N'.");
        assertEquals("Exit  Alt+F4", exitMenuItem.getText(), "Second should be 'Exit  Alt+F4'.");
    }

    @Test
    void testHelpMenu() {
        JMenu helpMenu = menuBar.getMenu(1);
        assertNotNull(helpMenu, "No Help menu.");
        assertEquals(1, helpMenu.getItemCount(), "Help menu needs to contain 1 item.");

        JMenuItem objectiveItem = helpMenu.getItem(0);
        assertEquals("About Quine McCluskey Algorithm", objectiveItem.getText(),
                "Does not contain 'About Quine McCluskey Algorithm'.");
    }

    @Test
    void testAboutMenu() {
        JMenu aboutMenu = menuBar.getMenu(2);
        assertNotNull(aboutMenu, "No About menu");
        assertEquals(3, aboutMenu.getItemCount(), "About menu needs to contain 3 items.");

        assertEquals("Developer 1", aboutMenu.getItem(0).getText(), "First should be Developer 1.");
        assertEquals("Developer 2", aboutMenu.getItem(1).getText(), "Second should be Developer 2.");
        assertEquals("Developer 3", aboutMenu.getItem(2).getText(), "Third should be Developer 3.");
    }

    @Test
    void testNewMenuItemAction() {
        JMenu fileMenu = menuBar.getMenu(0);
        JMenuItem newMenuItem = fileMenu.getItem(0);

        assertNotNull(newMenuItem.getActionListeners(), "New menu item needs an action listener.");

        ActionListener[] listeners = newMenuItem.getActionListeners();
        assertEquals(1, listeners.length, "New menu item needs only one action listener.");
    }

    @Test
    void testObjectiveMenuItemAction() {
        JMenu helpMenu = menuBar.getMenu(1);
        JMenuItem objectiveItem = helpMenu.getItem(0);

        assertNotNull(objectiveItem.getActionListeners(), "Objective menu item needs an action listener.");

        ActionListener[] listeners = objectiveItem.getActionListeners();
        assertEquals(1, listeners.length, "Objective menu item needs only one action listener.");
    }

    @Test
    void testDeveloperMenuItemActions() {
        JMenu aboutMenu = menuBar.getMenu(2);
        JMenuItem dev1 = aboutMenu.getItem(0);
        JMenuItem dev2 = aboutMenu.getItem(1);
        JMenuItem dev3 = aboutMenu.getItem(2);

        assertNotNull(dev1.getActionListeners(), "Developer 1 menu item needs an action listener.");
        assertNotNull(dev2.getActionListeners(), "Developer 2 menu item needs an action listener.");
        assertNotNull(dev3.getActionListeners(), "Developer 3 menu item needs an action listener.");

        assertEquals(1, dev1.getActionListeners().length, "Developer 1 needs one action listener.");
        assertEquals(1, dev2.getActionListeners().length, "Developer 2 needs one action listener.");
        assertEquals(1, dev3.getActionListeners().length, "Developer 3 needs one action listener.");
    }
}