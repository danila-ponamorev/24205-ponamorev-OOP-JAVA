package game.classes;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**.
 * Class for logging game events inot file. 
 */
public class GameLogger {
    private static final Logger logger = Logger.getLogger(GameLogger.class.getName());

    /**
     * Initializes logger with file handler.
     */
    static {
        try {
            FileHandler fileHandler = new FileHandler("game.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
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
     * Error message write
     * @param message Message.
     */
    public static void warning(String message) {
        logger.warning(message);
    }
}