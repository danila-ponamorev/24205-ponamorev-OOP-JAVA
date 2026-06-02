package calculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
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

    private void loadCommandsFromConfig(BufferedReader reader, File baseDirectory) throws IOException {
        String line;
        boolean selfJarLoaded = false;
        File currentJarFile = getCurrentJarFile();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            File jarFile = resolveJarFile(line, baseDirectory);
            if (jarFile.exists() && configReader.isJarFile(jarFile)) {
                // Проверяем, не указал ли пользователь наш же JAR вручную в конфиге
                if (currentJarFile != null && jarFile.getCanonicalPath().equals(currentJarFile.getCanonicalPath())) {
                    selfJarLoaded = true;
                }
                jarScanner.scanJar(jarFile, this::registerCommandClass);
            } else {
                loadCommandClass(line);
            }
        }

        if (!selfJarLoaded && currentJarFile != null && currentJarFile.exists()) {
            logger.info("Automatically scanning self JAR for internal commands: {}", currentJarFile.getName());
            jarScanner.scanJar(currentJarFile, this::registerCommandClass);
        }
    }

        private File getCurrentJarFile() {
        try {
            URI jarUri = CommandFactory.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI();
            
            if (jarUri.getPath().endsWith(".jar")) {
                return new File(jarUri);
            }
        } catch (Exception e) {
            logger.error("Failed to determine current JAR path", e);
        }
        return null;
    }

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

    private void loadCommandClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            registerCommandClass(clazz);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found: {}", className, e);
        }
    }

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
