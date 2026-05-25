package calculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import calculator.commands.AddCommand;
import calculator.commands.DefineCommand;
import calculator.commands.PrintCommand;
import calculator.commands.PushCommand;
import calculator.commands.SqrtCommand;

class CommandFactoryTest {
    @Test
    void shouldCreateAllConfiguredCommands() throws Exception {
        CommandFactory factory = new CommandFactory("commands.txt");

        assertTrue(factory.createCommand("PUSH") instanceof PushCommand);
        assertTrue(factory.createCommand("ADD") instanceof AddCommand);
        assertTrue(factory.createCommand("SQRT") instanceof SqrtCommand);
        assertTrue(factory.createCommand("PRINT") instanceof PrintCommand);
        assertTrue(factory.createCommand("DEFINE") instanceof DefineCommand);
    }

    @Test
    void shouldScanJarForAnnotatedCommandClasses() throws Exception {
        File jarFile = Files.createTempFile("command-scan", ".jar").toFile();
        jarFile.deleteOnExit();

        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
            addClassToJar(jos, "/calculator/commands/PushCommand.class");
        }

        CommandFactory factory = new CommandFactory(jarFile.getAbsolutePath());
        assertTrue(factory.createCommand("PUSH") instanceof PushCommand);
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

    @Test
    void unknownCommandShouldThrowException() {
        CommandFactory factory = new CommandFactory("commands.txt");

        Exception exception = assertThrows(Exception.class, () -> factory.createCommand("UNKNOWN"));
        assertEquals("UNKNOWN", exception.getMessage());
    }
}
