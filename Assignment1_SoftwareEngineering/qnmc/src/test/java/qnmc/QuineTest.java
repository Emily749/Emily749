package qnmc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qnmc.model.ExceptionQuine;
import qnmc.model.MinTerm;
import qnmc.model.Quine;

import static org.junit.jupiter.api.Assertions.*;

class QuineTest {
    private Quine quine;

    @BeforeEach
    void setUp() {
        quine = new Quine();
    }

    @Test
    void testAddMinTerms() throws ExceptionQuine {
        quine.addMinTerms("010");
        quine.addMinTerms("011");

        assertEquals("010\n011\n", quine.toString());
    }

    @Test
    void testMaxLimit() {
        ExceptionQuine exception = assertThrows(ExceptionQuine.class, () -> {
            for (int i = 0; i <= Quine.MAX_TERMS; i++) {
                quine.addMinTerms(Integer.toBinaryString(i));
            }
        });

        assertEquals("Quine::addTerm() - Maximum terms reached.", exception.getMessage());
    }

    @Test
    void testIncludesTerm() throws ExceptionQuine {
        MinTerm term1 = new MinTerm("010");
        MinTerm term2 = new MinTerm("111");

        quine.addMinTerms("010");

        assertTrue(quine.hasTerm(term1));
        assertFalse(quine.hasTerm(term2));
    }

    @Test
    void testToString() throws ExceptionQuine {
        quine.addMinTerms("101");
        quine.addMinTerms("001");

        String expected = "101\n001\n";
        assertEquals(expected, quine.toString());
    }
}