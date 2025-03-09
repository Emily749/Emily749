package qnmc;

import org.junit.jupiter.api.Test;
import qnmc.view.GUI;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GUITest {

    @Test
    void testSetMintermSet_withValidInput() {
        GUI gui = new GUI(3);
        Set<String> mintermSetBefore = GUI.mintermSet;
        gui.setMintermSet("2");
        assertTrue(GUI.mintermSet.contains("2"));
        assertEquals(mintermSetBefore.size(), GUI.mintermSet.size());
    }

    @Test
    void testSetMintermSet_withDuplicateInput() {
        GUI gui = new GUI(3);
        GUI.mintermSet.add("3");
        gui.setMintermSet("3");
        assertEquals(2, GUI.mintermSet.size());
        assertTrue(GUI.mintermSet.contains("3"));
    }

    @Test
    void testSetMintermSet_withEmptyInput() {
        GUI gui = new GUI(3);
        gui.setMintermSet("");
        assertTrue(GUI.mintermSet.contains(""));
    }
}