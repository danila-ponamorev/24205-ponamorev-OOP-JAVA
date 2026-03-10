package game.classes;
/**
 * Class for player's attempt evaluation.
 * Calculates the number of bulls and cows.
 */
public class Evaluation {

    /**
     * Evaluates the attempt with the secret code.
     * @param secretCode Secret code.
     * @param userGuess User's attempt.
     * @return Object EvaluationResult with numbers of cows and bulls.
     */
    public EvaluationResult evaluate(String secretCode, String userGuess) {
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < secretCode.length(); i++) {
            char secretChar = secretCode.charAt(i);
            char guessChar = userGuess.charAt(i);

            if (secretChar == guessChar) {
                bulls++;
            } else if (secretCode.indexOf(guessChar) != -1) {
                cows++;
            }
        }
        return new EvaluationResult(bulls, cows);
    }
}