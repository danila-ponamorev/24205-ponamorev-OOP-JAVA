package calculator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Scans JAR files and delivers found command candidate classes to a consumer.
 */
public class JarCommandScanner {
    private static final Logger logger = LoggerFactory.getLogger(JarCommandScanner.class);
    /**
     * Scans the provided JAR file and applies the consumer to each class that can be loaded.
     * Only classes that are command candidates are forwarded to the consumer.
     *
     * @param jarFile the JAR file to scan
     * @param classConsumer consumer that receives loaded classes
     * @throws IOException if the JAR cannot be opened
     */
    public void scanJar(File jarFile, Consumer<Class<?>> classConsumer) throws IOException {
        try (JarFile jar = new JarFile(jarFile);
             URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, getClass().getClassLoader())) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                String className = entry.getName().replace('/', '.').replaceAll("\\.class$", "");
                try {
                    Class<?> clazz = Class.forName(className, false, classLoader);
                    if (isCommandCandidate(clazz)) {
                        classConsumer.accept(clazz);
                    }
                } catch (ClassNotFoundException | NoClassDefFoundError e) {

                    // Log and skip classes that cannot be loaded
                    logger.error("Failed to load class: " + className + " - " + e.getMessage());
                }
            }
        }
    }

    private boolean isCommandCandidate(Class<?> clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return false;
        }
        return clazz.isAnnotationPresent(CommandName.class) || Command.class.isAssignableFrom(clazz);
    }
}
