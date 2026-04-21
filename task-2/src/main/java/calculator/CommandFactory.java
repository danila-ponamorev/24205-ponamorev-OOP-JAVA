package calculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Command Factory that loads command classes based on a configuration file and creates instances of commands by name.
 */
public class CommandFactory {
    private final Map<String, Class<? extends Command>> commandClasses = new HashMap<>();

    /**
     * Constructor that loads command classes from a configuration file.
     * @param configFileName The name of the configuration file
     */
    public CommandFactory(String configFileName) {
        CalculatorLogger.info("Loading commands from config: " + configFileName);
        try (InputStream is = CommandFactory.class.getResourceAsStream("/" + configFileName)) {
            if (is == null) throw new FileNotFoundException("Config file not found in classpath: " + configFileName);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String className;
            while ((className = reader.readLine()) != null) {
                className = className.trim();
                if (className.isEmpty()) continue;
                
                try {
                    Class<?> clazz = Class.forName(className);
                    if (Command.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(CommandName.class)) {
                        CommandName annotation = clazz.getAnnotation(CommandName.class);
                        @SuppressWarnings("unchecked")
                        Class<? extends Command> commandClass = (Class<? extends Command>) clazz;
                        commandClasses.put(annotation.value(), commandClass);
                        CalculatorLogger.fine("Registered command: " + annotation.value() + " -> " + className);
                    } else {
                        CalculatorLogger.severe("Class " + className + " does not implement Command or missing @CommandName");
                    }
                } catch (ClassNotFoundException e) {
                    CalculatorLogger.severe("Class not found: " + className);
                }
            }
        } catch (IOException e) {
            CalculatorLogger.severe("Failed to load factory configuration");
        }
    }

    /**
     * Creates a command instance by name.
     * @param name The name of the command to create
     * @return The created command instance
     * @throws Exception If the command cannot be created
     */
    public Command createCommand(String name) throws Exception {
        Class<? extends Command> clazz = commandClasses.get(name);
        if (clazz == null) {
            throw new Exception(name);
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            CalculatorLogger.severe("Failed to instantiate command: " + name);
            throw new RuntimeException("Factory failed to instantiate command", e);
        }
    }
}