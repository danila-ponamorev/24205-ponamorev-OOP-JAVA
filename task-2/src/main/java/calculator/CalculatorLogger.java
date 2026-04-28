package calculator;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**.
 * Class for logging game events inot file. 
 */
public class CalculatorLogger {
    private static final Logger logger = Logger.getLogger(CalculatorLogger.class.getName());

    /**
     * Initializes logger with file handler.
     */
    static {
        try {
            FileHandler fileHandler = new FileHandler("game.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
            
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            logger.info("Logger is initialized.");
        } catch (IOException e) {
            System.err.println("Logger initialization error: " + e.getMessage());
        }
    }

    /**
     * Info message write.
     * @param message message.
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Severe message write.
     * @param message Message.
     */
    public static void severe(String message) {
        logger.severe(message);
    }

    /**
     * Fine message write.
     * @param message Message.
     */
    public static void fine(String message) {
        logger.fine(message);
    }

    /**
     * Error message write
     * @param message Message.
     */
    public static void warning(String message) {
        logger.warning(message);
    }
}
