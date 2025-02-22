package qnmc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GetMintermListTest {
    private GetMintermList mintermList;

    @BeforeEach
    void setUp() {
        mintermList = new GetMintermList();
    }

    @Test
    void testAddMinterm() {
        mintermList.setMinList("9");
        Set<String> minterms = GetMintermList.getMin();
        assertTrue(minterms.contains("9"), "Minterm list needs to include '9'");
    }

    @Test
    void testGetMintermList() {
        mintermList.setMinList("9");
        mintermList.setMinList("11");
        Set<String> minterms = GetMintermList.getMin();
        assertTrue(minterms.contains("9") && minterms.contains("11"), "Needs to include 9 and 11");
    }
}