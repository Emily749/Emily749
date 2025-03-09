package qnmc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import qnmc.view.Observer;

class ObserverTest {

    static class MockObserver implements Observer {
        private String lastInput;

        @Override
        public void update(String currentInput) {
            this.lastInput = currentInput;
        }

        public String getLastInput() {
            return lastInput;
        }
    }

    @Test
    void testUpdateWithValidInput() {
        MockObserver observer = new MockObserver();
        String input = "Test Input";
        observer.update(input);
        assertEquals("Test Input", observer.getLastInput());
    }

    @Test
    void testUpdateWithNullInput() {
        MockObserver observer = new MockObserver();
        String input = null;
        observer.update(input);
        assertNull(observer.getLastInput());
    }

    @Test
    void testUpdateWithEmptyInput() {
        MockObserver observer = new MockObserver();
        String input = "";
        observer.update(input);
        assertEquals("", observer.getLastInput());
    }
}