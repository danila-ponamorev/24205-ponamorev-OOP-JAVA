package classes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import game.classes.SecretCode;

/**
 * Unit tests for the SecretCode class.
 * Tests code generation and input validation.
 */
@DisplayName("Secret Code Tests")
public class SecretCodeTest {

    @Test
    @DisplayName("Generated code has correct length and unique digits")
    public void testGeneratedCodeValid() {
        SecretCode code = new SecretCode(4);
        assertEquals(4, code.getCode().length(), "Code length should be 4");
        assertTrue(code.isValidInput(code.getCode()), "Generated code should be valid");
    }

    @Test
    @DisplayName("Valid input acceptance")
    public void testValidInputAccepted() {
        SecretCode code = new SecretCode(4);
        assertTrue(code.isValidInput("1234"), "Should accept valid 4-digit code");
        assertTrue(code.isValidInput("9876"), "Should accept any valid 4-digit code");
    }

    @Test
    @DisplayName("Invalid input - wrong length")
    public void testInvalidInputWrongLength() {
        SecretCode code = new SecretCode(4);
        assertFalse(code.isValidInput("123"), "Should reject code with 3 digits");
        assertFalse(code.isValidInput("12345"), "Should reject code with 5 digits");
    }

    @Test
    @DisplayName("Invalid input - duplicate digits")
    public void testInvalidInputDuplicateDigits() {
        SecretCode code = new SecretCode(4);
        assertFalse(code.isValidInput("1123"), "Should reject duplicate digits");
        assertFalse(code.isValidInput("1222"), "Should reject duplicate digits");
    }

    @Test
    @DisplayName("Invalid input - non-digit characters")
    public void testInvalidInputNonDigits() {
        SecretCode code = new SecretCode(4);
        assertFalse(code.isValidInput("123a"), "Should reject letters");
        assertFalse(code.isValidInput("12 3"), "Should reject spaces");
        assertFalse(code.isValidInput(null), "Should reject null input");
    }
}
