package calculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
     * Tries to load from file system first, then falls back to classpath resources.
     * @param configPath The path to the configuration file (can be relative or absolute)
     */
    public CommandFactory(String configPath) {
        CalculatorLogger.info("Loading commands from config: " + configPath);
        BufferedReader reader = null;
        
        try {
            File file = new File(configPath);
            if (file.exists()) {
                CalculatorLogger.fine("Loading from file: " + file.getAbsolutePath());
                reader = new BufferedReader(new FileReader(file));
            } else {
                InputStream is = CommandFactory.class.getResourceAsStream("/" + configPath);
                if (is != null) {
                    CalculatorLogger.fine("Loading from classpath: " + configPath);
                    reader = new BufferedReader(new InputStreamReader(is));
                } else {
                    throw new FileNotFoundException("Config file not found: " + configPath + " (tried file system and classpath)");
                }
            }
            
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
            CalculatorLogger.severe("Failed to load factory configuration: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    CalculatorLogger.severe("Failed to close config file reader");
                }
            }
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