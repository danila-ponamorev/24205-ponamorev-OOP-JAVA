package calculator;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CommandConfigReaderTest {

    @Test
    void shouldReadConfigFromClasspathResource() throws Exception {
        CommandConfigReader reader = new CommandConfigReader();

        List<String> lines = reader.readConfigLines("commands.txt");

        assertTrue(lines.contains("calculator.commands.PushCommand"));
        assertTrue(lines.contains("calculator.commands.AddCommand"));
    }

    @Test
    void shouldReadConfigFromFilesystemFile() throws Exception {
        File tempFile = Files.createTempFile("commands-test", ".txt").toFile();
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("calculator.commands.PushCommand\n");
            writer.write("calculator.commands.AddCommand\n");
        }

        CommandConfigReader reader = new CommandConfigReader();
        List<String> lines = reader.readConfigLines(tempFile.getAbsolutePath());

        assertEquals(2, lines.size());
        assertEquals("calculator.commands.PushCommand", lines.get(0));
        assertEquals("calculator.commands.AddCommand", lines.get(1));
    }
}
