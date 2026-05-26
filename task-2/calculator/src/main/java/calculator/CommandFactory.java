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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command Factory that loads command classes based on a configuration file and creates instances of commands by name.
 */
public class CommandFactory {
    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
    private final Map<String, Class<? extends Command>> commandClasses = new HashMap<>();
    private final CommandConfigReader configReader = new CommandConfigReader();
    private final JarCommandScanner jarScanner = new JarCommandScanner();

    /**
     * Constructor that loads command classes from a configuration file.
     * Tries to load from file system first, then falls back to classpath resources.
     * @param configPath The path to the configuration file (can be relative or absolute)
     */
    public CommandFactory(String configPath) {
        logger.info("Loading commands from config: {}", configPath);
        BufferedReader reader = null;

        try {
            File configFile = new File(configPath);
            if (configReader.isJarFile(configFile)) {
                jarScanner.scanJar(configFile, this::registerCommandClass);
                return;
            }

            if (configFile.exists()) {
                logger.debug("Loading from file: {}", configFile.getAbsolutePath());
                reader = new BufferedReader(new FileReader(configFile));
            } else {
                InputStream is = CommandFactory.class.getResourceAsStream("/" + configPath);
                if (is != null) {
                    logger.debug("Loading from classpath: {}", configPath);
                    reader = new BufferedReader(new InputStreamReader(is));
                } else {
                    throw new FileNotFoundException("Config file not found: " + configPath + " (tried file system and classpath)");
                }
            }

            loadCommandsFromConfig(reader, configFile.getParentFile());
        } catch (IOException e) {
            logger.error("Failed to load factory configuration: {}", e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("Failed to close config file reader", e);
                }
            }
        }
    }

    /**
     * Loads command classes from the configuration reader. Each line can be a class name or a path to a JAR file.
     * @param reader
     * @param baseDirectory
     * @throws IOException
     */
    private void loadCommandsFromConfig(BufferedReader reader, File baseDirectory) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            File jarFile = resolveJarFile(line, baseDirectory);
            if (jarFile.exists() && configReader.isJarFile(jarFile)) {
                jarScanner.scanJar(jarFile, this::registerCommandClass);
            } else {
                loadCommandClass(line);
            }
        }
    }

    /**
     * Resolves a JAR file path, checking various locations if the path is not absolute.
     * @param path The path to the JAR file
     * @param baseDirectory The base directory to check for relative paths
     * @return The resolved File object for the JAR file
     */
    private File resolveJarFile(String path, File baseDirectory) {
        File jarFile = new File(path);
        if (!jarFile.isAbsolute()) {
            if (baseDirectory != null) {
                File candidate = new File(baseDirectory, path);
                if (candidate.exists()) {
                    jarFile = candidate;
                }
            }
            if (!jarFile.exists()) {
                File cwdCandidate = new File(System.getProperty("user.dir"), path);
                if (cwdCandidate.exists()) {
                    jarFile = cwdCandidate;
                }
            }
            if (!jarFile.exists()) {
                File targetCandidate = new File(System.getProperty("user.dir"), "target" + File.separator + path);
                if (targetCandidate.exists()) {
                    jarFile = targetCandidate;
                }
            }
            if (!jarFile.exists()) {
                File moduleCandidate = new File(System.getProperty("user.dir"), "clearstack-command" + File.separator + "target" + File.separator + path);
                if (moduleCandidate.exists()) {
                    jarFile = moduleCandidate;
                }
            }
        }
        return jarFile;
    }

    /**
     * Loads a command class by its fully qualified name and registers it if it implements Command and has @CommandName annotation.
     * @param className
     */
    private void loadCommandClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            registerCommandClass(clazz);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found: {}", className, e);
        }
    }

    /**
     * Registers a command class if it implements Command and has @CommandName annotation.
     * @param clazz The class to register
     */
    private void registerCommandClass(Class<?> clazz) {
        if (clazz == null) {
            return;
        }

        if (Command.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(CommandName.class)) {
            CommandName annotation = clazz.getAnnotation(CommandName.class);
            @SuppressWarnings("unchecked")
            Class<? extends Command> commandClass = (Class<? extends Command>) clazz;
            commandClasses.put(annotation.value(), commandClass);
            logger.debug("Registered command: {} -> {}", annotation.value(), clazz.getName());
        } else {
            logger.error("Class {} does not implement Command or missing @CommandName", clazz.getName());
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
            logger.error("Failed to instantiate command: {}", name, e);
            throw new RuntimeException("Factory failed to instantiate command", e);
        }
    }
}
