package game.classes;

/**
 * Game Controller. Manages components initialization and GameEngine start.
 */
public class GameController {
    
    /**
     * Game launching method.
     * @param args Command line arguments.
     */
    public void run(String[] args) {
        GameSettings settings = new GameSettings();
        String secretCodeValue = null;
        
        for (int i = 0; i < args.length && i < args.length; ++i) {
            switch (args[i]) {
                case "-length" -> {
                    try {
                        int len = Integer.parseInt(args[i + 1]);
                        settings.setCodeLength(len);
                        GameLogger.info("CodeLength set from the arguments: " + len + " digits");
                    } catch (NumberFormatException e) {
                        GameLogger.warning("Incorrect CodeLength. Value was set by default.");
                    }
                    ++i;
                }
                case "-time" -> {
                    try {
                    int len = Integer.parseInt(args[i + 1]);
                    settings.setTimeLimitSeconds(len);
                    GameLogger.info("TimeLimit set from the arguments: " + len + " seconds");
                    } catch (NumberFormatException e) {
                        GameLogger.warning("Incorrect Time Limit. Value was set by default.");
                    }
                    ++i;
                }
                case "-attempts" -> {
                    try {
                    int len = Integer.parseInt(args[i + 1]);
                    settings.setMaxAttempts(len);
                    GameLogger.info("MaxAttempts set from the arguments: " + len + "attempts");
                    } catch (NumberFormatException e) {
                        GameLogger.warning("Incorrect Number of attempts. Value was set by default.");
                    }
                    ++i;
                }
                case "-code" -> {
                    try {
                    secretCodeValue = args[i + 1];
                    GameLogger.info("MaxAttempts set from the arguments: " + args[i + 1] + "attempts");
                    } catch (NumberFormatException e) {
                        GameLogger.warning("Incorrect Number of attempts. Value was set by default.");
                    }
                    ++i;
                }
            }
        }

        GameRender render = new GameRender();
        GameScanner scanner = new GameScanner();
        GameEngine engine = new GameEngine(settings, render, scanner, secretCodeValue);

        try {
            engine.startGame();
        } catch (Exception e) {
            GameLogger.warning("Critical error: " + e.getMessage());
            System.err.println("Error occured during the game.");
        } finally {
            scanner.close();
        }
    }
}