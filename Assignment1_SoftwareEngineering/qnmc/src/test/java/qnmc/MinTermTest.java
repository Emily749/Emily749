package qnmc;

import org.junit.jupiter.api.Test;
import qnmc.model.ExceptionQuine;
import qnmc.model.MinTerm;

import static org.junit.jupiter.api.Assertions.*;

class MinTermTest {

    @Test
    void testConstructor() {
        MinTerm minTerm = new MinTerm("01_");

        assertEquals("01_", minTerm.toString(), "Input string not parsed correctly");
    }

    @Test
    void testToString() {
        MinTerm minTerm = new MinTerm("01_");
        assertEquals("01_", minTerm.toString(), "Binary representation incorrect");
    }

    @Test
    void testIsSame() throws ExceptionQuine {
        MinTerm term1 = new MinTerm("101");
        MinTerm term2 = new MinTerm("101");
        MinTerm term3 = new MinTerm("110");

        assertTrue(term1.isSame(term2), "Should return true");
        assertFalse(term1.isSame(term3), "Should return false");
    }

    @Test
    void testIsSameException() {
        MinTerm term1 = new MinTerm("01_");
        MinTerm term2 = new MinTerm("10");

        ExceptionQuine exception = assertThrows(ExceptionQuine.class, () -> term1.isSame(term2));
        assertEquals("MinTerm::isSame()", exception.getMessage(), "No exception thrown for different lengths");
    }

    @Test
    void testResolution() throws ExceptionQuine {
        MinTerm term1 = new MinTerm("101");
        MinTerm term2 = new MinTerm("111");

        assertEquals(1, term1.resolutionCount(term2), "1 bit difference should return 1");

        MinTerm term3 = new MinTerm("000");
        assertEquals(2, term1.resolutionCount(term3), "2 bit difference should return 2");
    }

    @Test
    void testResolutionException() {
        MinTerm term1 = new MinTerm("101");
        MinTerm term2 = new MinTerm("10");

        ExceptionQuine exception = assertThrows(ExceptionQuine.class, () -> term1.resolutionCount(term2));
        assertEquals("MinTerm::resolutionCount()", exception.getMessage(), "No exception thrown for different lengths");
    }

    @Test
    void testCombine() throws ExceptionQuine {
        MinTerm term1 = new MinTerm("100");
        MinTerm term2 = new MinTerm("110");

        MinTerm result = MinTerm.combine(term1, term2);
        assertEquals("1_0", result.toString(), "Combine should replace different bits with '_'");
    }

    @Test
    void testCombineException() {
        MinTerm term1 = new MinTerm("101");
        MinTerm term2 = new MinTerm("10");

        ExceptionQuine exception = assertThrows(ExceptionQuine.class, () -> MinTerm.combine(term1, term2));
        assertEquals("MinTerm::combine()", exception.getMessage(), "No exception thrown for different lengths");
    }
}