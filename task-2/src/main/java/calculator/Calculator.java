package calculator;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main calculator class that processes commands and maintains execution context.
 */
public class Calculator {
    private final ExecutionContext context;
    private final CommandFactory factory;

    /**
     * Constructor for Calculator.
     * @param factory CommandFactory to create command instances.
     */
    public Calculator(CommandFactory factory) {
        this.factory = factory;
        this.context = new ExecutionContext();
    }

    /**
     * Gets the execution context of the calculator.
     * @return
     */
    public ExecutionContext getContext() { return context; }

    /**
     * Executes a single line of command input.
     * @param line
     */
    public void executeLine(String line) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) return;

        String[] parts = line.split("\\s+");
        String cmdName = parts[0];
        List<String> args = Arrays.asList(parts).subList(1, parts.length);

        try {
            Command command = factory.createCommand(cmdName);
            CalculatorLogger.info("Executing: " + cmdName + " with args " + args);
            command.execute(context, args);
        } catch (Exception e) {
            CalculatorLogger.severe("Execution error: " + e.getMessage());
            System.err.println("Error executing '" + cmdName + "': " + e.getMessage());
        }
    }

    /**
     * Executes commands from the given input stream.
     * @param in Input stream to read commands from.
     */
    public void executeStream(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                executeLine(scanner.nextLine());
            }
        }
    }
}