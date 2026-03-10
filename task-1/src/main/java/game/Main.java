package game;
import game.classes.GameController;
import game.classes.GameLogger;


/**
 * Main application class (Entry Point).
 * Initializing GameLogger, GameController and starts the game.
 */
public class Main {
    public static void main(String[] args) {
        GameLogger.info("Application started");

        GameController controller = new GameController();
        controller.run(args);

        GameLogger.info("Application stopped");
    }
}
