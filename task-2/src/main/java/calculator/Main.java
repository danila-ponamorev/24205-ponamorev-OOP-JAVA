package calculator;

import java.io.FileInputStream;
import java.io.IOException;
/**
 * Main entry point for the calculator application.
 * 
 * @param args If provided, the first argument is treated as a file path to read commands from. 
 * If no arguments are given, the application runs in interactive mode, reading commands from standard input.
 */
public class Main {
    public static void main(String[] args) {
        CommandFactory factory = new CommandFactory("commands.txt");
        Calculator calculator = new Calculator(factory);

        if (args.length > 0) {
            try (FileInputStream fis = new FileInputStream(args[0])) {
                calculator.executeStream(fis);
            } catch (IOException e) {
                System.err.println("Failed to read file: " + e.getMessage());
            }
        } else {
            System.out.println("Running in interactive mode. Type commands (Ctrl+D/Ctrl+C to exit):");
            calculator.executeStream(System.in);
        }
    }
}