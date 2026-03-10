package game.classes;

/**
 * Record for storing result of the attempt.
 * 
 * @param bulls Number of bulls (Match by value and position)
 * @param cows Number of cows (Match by value, but not position)
 */
public record EvaluationResult(int bulls, int cows) {}
