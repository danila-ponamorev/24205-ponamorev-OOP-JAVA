package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("LOAD_DEFINES")
public class LoadDefinesCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.size() != 1) throw new IllegalArgumentException("LOAD_DEFINES requires exactly one argument: file name");
        String fileName = args.get(0);
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) throw new IllegalArgumentException("Invalid format in file: " + fileName);
                String name = parts[0];
                Double value = Double.parseDouble(parts[1]);
                ctx.getDefines().put(name, value);
            }
        } catch (java.io.IOException e) {
            throw new Exception("Failed to load defines from file: " + e.getMessage());
        }
    }
}
