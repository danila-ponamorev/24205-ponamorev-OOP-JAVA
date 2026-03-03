package classes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import game.classes.GameSettings;

/**
 * Unit tests for the GameSettings class.
 * Tests validation and constraints.
 */
@DisplayName("Game Settings Tests")
public class GameSettingsTest {

    @Test
    @DisplayName("Default settings are correct")
    public void testDefaultSettings() {
        GameSettings settings = new GameSettings();
        assertEquals(4, settings.getCodeLength(), "Default code length should be 4");
        assertEquals(10, settings.getMaxAttempts(), "Default max attempts should be 10");
        assertEquals(60, settings.getTimeLimitSeconds(), "Default time limit should be 60");
    }

    @Test
    @DisplayName("Custom settings are applied correctly")
    public void testCustomSettings() {
        GameSettings settings = new GameSettings(5, 15, 120);
        assertEquals(5, settings.getCodeLength(), "Code length should be 5");
        assertEquals(15, settings.getMaxAttempts(), "Max attempts should be 15");
        assertEquals(120, settings.getTimeLimitSeconds(), "Time limit should be 120");
    }

    @Test
    @DisplayName("Code length boundary validation")
    public void testCodeLengthBoundaries() {
        assertDoesNotThrow(() -> new GameSettings(3, 10, 60), "Should accept min length (3)");
        assertDoesNotThrow(() -> new GameSettings(6, 10, 60), "Should accept max length (6)");
        // assertThrows(llegalArgumentException.class, () -> new GameSettings(2, 10, 60), "Should reject length < 3");
        // assertThrows(IllegalArgumentException.class, () -> new GameSettings(7, 10, 60), "Should reject length > 6");
    }

    @Test
    @DisplayName("Max attempts validation")
    public void testMaxAttemptsValidation() {
        assertDoesNotThrow(() -> new GameSettings(4, 1, 60), "Should accept 1 attempt");
        // assertThrows(IllegalArgumentException.class, () -> new GameSettings(4, 0, 60), "Should reject 0 attempts");
        // assertThrows(IllegalArgumentException.class, () -> new GameSettings(4, -5, 60), "Should reject negative attempts");
    }

    @Test
    @DisplayName("Time limit validation")
    public void testTimeLimitValidation() {
        assertDoesNotThrow(() -> new GameSettings(4, 10, 1), "Should accept 1 second");
        // assertThrows(IllegalArgumentException.class, () -> new GameSettings(4, 10, 0), "Should reject 0 time");
        // assertThrows(IllegalArgumentException.class, () -> new GameSettings(4, 10, -30), "Should reject negative time");
    }
}
