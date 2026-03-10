package game.classes;

import java.io.IOException;
import java.util.Scanner;

/**
 * Класс для взаимодействия с пользователем через консоль.
 * Обрабатывает ввод и вывод сообщений.
 */
public class GameScanner {
    private final Scanner scanner;

    public GameScanner() {
        this.scanner = new Scanner(System.in);
    }

    private static boolean isInputReady() {
        try {
            return System.in.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads User Input with time measurement.
     * @param timeLimitSeconds Time limit.
     * @return Input string or null if time expired.
     */
    public String readInput(long timeLimitSeconds) {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        while((endTime - startTime) / 1000 < timeLimitSeconds) {
            if (isInputReady()) {
                String input = scanner.nextLine();
                // endTime = System.currentTimeMillis();
                // long duration = (endTime - startTime) / 1000;

                // if (duration > timeLimitSeconds) {
                //     return null;
                // }
                return input.trim();
            }
            endTime = System.currentTimeMillis();
        }
        return null;
    }

    public void close() {
        scanner.close();
    }
}