package game.classes;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Class responsible for secretCode generation and User Input validation.
 */
public class SecretCode {
    private final String code;
    private final int length;

    /**

     * Generates new secretCode with given length.
     * @param length Code length.
     */
    public SecretCode(int length) {
        this.length = length;
        this.code = generateUniqueCode(length);
    }

    /**
     * Constructor for tests with the known code
     * @param code Code line.
     */
    public SecretCode(String code) {
        this.code = code;
        this.length = code.length();
    }

    public String getCode() {
        return code;
    }

    public int getLength() {
        return length;
    }

    /**
     * String of unique numbers generation.
     */
    private String generateUniqueCode(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        Set<Character> used = new HashSet<>();

        while (sb.length() < len) {
            char digit = (char) ('0' + random.nextInt(10));
            if (used.add(digit)) {
                sb.append(digit);
            }
        }
        return sb.toString();
    }

    /**
     * User input validation.
     * @param input Input string.
     * @return true if the input is correct.
     */
    public boolean isValidInput(String input) {
        if (input == null || input.length() != length) {
            return false;
        }
        Set<Character> chars = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c) || !chars.add(c)) {
                return false;
            }
        }
        return true;
    }
}