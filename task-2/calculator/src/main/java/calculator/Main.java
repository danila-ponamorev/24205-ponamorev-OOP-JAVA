package calculator;

import java.io.File;
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
        String configFile = "commands.txt";
        String inputFile = null;
        
        if (args.length > 0) {
            if (args[0].endsWith(".txt") || args[0].contains(File.separator) || args[0].contains("/")) {
                configFile = args[0];
                if (args.length > 1) {
                    inputFile = args[1];
                }
            } else {
                inputFile = args[0];
            }
        }
        
        CommandFactory factory = new CommandFactory(configFile);
        Calculator calculator = new Calculator(factory);

        if (inputFile != null) {
            try (FileInputStream fis = new FileInputStream(inputFile)) {
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