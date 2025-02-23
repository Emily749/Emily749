package qnmc.model;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionQuine extends Exception {
    private static final Logger LOGGER = Logger.getLogger(ExceptionQuine.class.getName());

    public ExceptionQuine(String message) {
        super(message);
        LOGGER.log(Level.SEVERE, "Quine-McCluskey Error: {0}", message);
    }
}