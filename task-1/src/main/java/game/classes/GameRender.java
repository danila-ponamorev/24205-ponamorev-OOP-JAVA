package game.classes;

/**
 * Класс для взаимодействия с пользователем через консоль.
 * Обрабатывает ввод и вывод сообщений.
 */
public class GameRender {
    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printWelcome(GameSettings settings) {
        printMessage("=== Game 'Bulls and Cows' ===");
        printMessage(String.format("Code length: %d", settings.getCodeLength()));
        printMessage(String.format("Maximum number of attempts: %d", settings.getMaxAttempts()));
        printMessage(String.format("Attempt time limit: %d seconds", settings.getTimeLimitSeconds()));
        printMessage("Enter a number with unique digits.");
        printMessage("-------------------------------");
    }
}