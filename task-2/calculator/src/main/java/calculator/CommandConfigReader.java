package calculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads command configuration files from the file system or classpath.
 */
public class CommandConfigReader {

    /**
     * Reads all non-empty, non-comment lines from the given configuration path.
     *
     * @param configPath the file system or classpath-relative path
     * @return list of configuration lines
     * @throws IOException if the configuration cannot be loaded
     */
    public List<String> readConfigLines(String configPath) throws IOException {
        File configFile = new File(configPath);
        if (configFile.exists()) {
            if (isJarFile(configFile)) {
                throw new IOException("Configuration path points to a JAR file: " + configPath);
            }
            return readLines(new FileInputStream(configFile));
        }

        try (InputStream is = CommandConfigReader.class.getResourceAsStream("/" + configPath)) {
            if (is == null) {
                throw new FileNotFoundException("Config file not found: " + configPath + " (tried file system and classpath)");
            }
            return readLines(is);
        }
    }

    private List<String> readLines(InputStream input) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Checks whether a file is an existing JAR file.
     *
     * @param file the file to inspect
     * @return true if the file is a JAR file
     */
    public boolean isJarFile(File file) {
        return file != null && file.exists() && file.isFile() && file.getName().toLowerCase().endsWith(".jar");
    }
}
