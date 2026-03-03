package classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import game.classes.Evaluation;
import game.classes.EvaluationResult;
/**
 * Unit tests for the Evaluation class.
 * Tests the core logic of counting bulls and cows.
 */
@DisplayName("Evaluation Logic Tests")
public class EvaluationTest {

    private final Evaluation evaluation = new Evaluation();

    @Test
    @DisplayName("Exact match - all bulls")
    public void testAllBulls() {
        EvaluationResult result = evaluation.evaluate("1234", "1234");
        assertEquals(4, result.bulls(), "Should have 4 bulls for exact match");
        assertEquals(0, result.cows(), "Should have 0 cows for exact match");
    }

    @Test
    @DisplayName("Correct digits, wrong positions - all cows")
    public void testAllCows() {
        EvaluationResult result = evaluation.evaluate("1234", "4321");
        assertEquals(0, result.bulls(), "Should have 0 bulls");
        assertEquals(4, result.cows(), "Should have 4 cows");
    }

    @Test
    @DisplayName("Mixed bulls and cows")
    public void testMixedBullsAndCows() {
        // Secret: 3219, Guess: 2310 -> 1 bull (1), 2 cows (2, 3)
        EvaluationResult result = evaluation.evaluate("3219", "2310");
        assertEquals(1, result.bulls(), "Should have 1 bull");
        assertEquals(2, result.cows(), "Should have 2 cows");
    }

    @Test
    @DisplayName("No matches at all")
    public void testNoMatches() {
        EvaluationResult result = evaluation.evaluate("1234", "5678");
        assertEquals(0, result.bulls(), "Should have 0 bulls");
        assertEquals(0, result.cows(), "Should have 0 cows");
    }

    @Test
    @DisplayName("Partial match with some bulls")
    public void testPartialMatch() {
        EvaluationResult result = evaluation.evaluate("1234", "1256");
        assertEquals(2, result.bulls(), "Should have 2 bulls (1 and 2)");
        assertEquals(0, result.cows(), "Should have 0 cows");
    }
}
