package game.classes;

/**
 * Class for storing game settings.
 */
public class GameSettings {
    private int codeLength;
    private int maxAttempts;
    private long timeLimitSeconds;

    /**
     * Default constructor.
     */
    public GameSettings() {
        this.codeLength = 4;
        this.maxAttempts = 10;
        this.timeLimitSeconds = 5;
    }

    /**
     * Constructor with parameters.
     * @param codeLength secretCode length (3-6).
     * @param maxAttempts maximum number of attempts.
     * @param timeLimitSeconds time limit per attempt in seconds.
     */
    public GameSettings(int codeLength, int maxAttempts, long timeLimitSeconds) {
        if (codeLength < 3 || codeLength > 6) {
            throw new IllegalArgumentException("Code length must be in range between 3 and 6.");
        }
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Number of attempts should be a positive number");
        }
        if (timeLimitSeconds <= 0) {
            throw new IllegalArgumentException("Time should be a positive number");
        }

        this.codeLength = codeLength;
        this.maxAttempts = maxAttempts;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        if (codeLength < 3 || codeLength > 6) {
            throw new IllegalArgumentException("Code length must be in range between 3 and 6.");
        }
        this.codeLength = codeLength;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("Number of attempts should be a positive number");
        }
        this.maxAttempts = maxAttempts;
    }

    public long getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(long timeLimitSeconds) {
        if (timeLimitSeconds <= 0) {
            throw new IllegalArgumentException("Time should be a positive number");
        }
        this.timeLimitSeconds = timeLimitSeconds;
    }
}