package game.classes;


/**
 * Game engine. Manages the game state, turn cicle and win/loss conditions.
 */
public class GameEngine {
    private final GameSettings settings;
    private final SecretCode secretCode;
    private final Evaluation evaluation;
    private final GameRender render;
    private final GameScanner scanner;
    
    private int attemptsCount;
    private boolean gameFinished;

    public GameEngine(GameSettings settings, GameRender render, GameScanner scanner, String secretCodeValue) {
        this.settings = settings;

        if (secretCodeValue != null && !secretCodeValue.isEmpty()) {
            this.secretCode = new SecretCode(secretCodeValue);
        } else {
            this.secretCode = new SecretCode(settings.getCodeLength());
        }
        this.evaluation = new Evaluation();
        this.render = render;
        this.scanner = scanner;
        this.attemptsCount = 0;
        this.gameFinished = false;

        GameLogger.info("Game started. Secret code is: " + secretCode.getCode());
    }

    /**
     * Game cicle start.
     */
    public void startGame() {
        render.printWelcome(settings);

        while (!gameFinished) {
            attemptsCount++;
            GameLogger.info("Attempt №" + attemptsCount);

            render.printMessage("Your turn: ");
            String guess = scanner.readInput(settings.getTimeLimitSeconds());

            if (guess == null) {
                render.printMessage("Defeat: Input time exceeded.");
                GameLogger.warning("Defeat: Input time exceeded.");
                endGame(false);
                continue;
            }

            if (!secretCode.isValidInput(guess)) {
                render.printMessage("Wrong format! Required " + settings.getCodeLength() + 
                        " unique numers.");
                attemptsCount--;
                GameLogger.warning("Wrong input: " + guess);
                continue;
            }

            EvaluationResult result = evaluation.evaluate(secretCode.getCode(), guess);
            render.printMessage(result.toString());
            GameLogger.info("Input: " + guess + " Result: " + result);

            if (result.bulls() == settings.getCodeLength()) {
                render.printMessage("Congrats! You guessed the number right!");
                endGame(true);
            } else if (attemptsCount >= settings.getMaxAttempts()) {
                render.printMessage("Defeat! Exceeded maximum amount of attempts.");
                render.printMessage("The hidden number was: " + secretCode.getCode());
                endGame(false);
            }
        }
    }

    private void endGame(boolean isWin) {
        gameFinished = true;
        GameLogger.info("Game over. Win: " + isWin);
    }
}