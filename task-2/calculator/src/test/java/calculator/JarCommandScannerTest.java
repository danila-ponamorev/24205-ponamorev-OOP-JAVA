package calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class JarCommandScannerTest {

    @Test
    void shouldScanJarAndLoadClasses() throws Exception {
        File jarFile = Files.createTempFile("command-scan", ".jar").toFile();
        jarFile.deleteOnExit();

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
            addClassToJar(jos, "/calculator/commands/PushCommand.class");
        }

        Set<String> loadedClassNames = new HashSet<>();
        new JarCommandScanner().scanJar(jarFile, clazz -> loadedClassNames.add(clazz.getName()));

        assertTrue(loadedClassNames.contains("calculator.commands.PushCommand"));
    }

    private void addClassToJar(JarOutputStream jos, String resourcePath) throws IOException {
        InputStream classStream = getClass().getResourceAsStream(resourcePath);
        if (classStream == null) {
            throw new FileNotFoundException("Class resource not found: " + resourcePath);
        }

        JarEntry entry = new JarEntry(resourcePath.substring(1));
        jos.putNextEntry(entry);

        byte[] buffer = new byte[8192];
        int read;
        while ((read = classStream.read(buffer)) != -1) {
            jos.write(buffer, 0, read);
        }

        classStream.close();
        jos.closeEntry();
    }
}
