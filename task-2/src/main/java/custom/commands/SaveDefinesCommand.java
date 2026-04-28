package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("SAVE_DEFINES")
public class SaveDefinesCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.size() != 1) throw new IllegalArgumentException("SAVE_DEFINES requires exactly one argument: file name");
        String fileName = args.get(0);
        try (java.io.PrintWriter writer = new java.io.PrintWriter(fileName)) {
            for (String name : ctx.getDefines().keySet()) {
                Double value = ctx.getDefines().get(name);
                writer.println(name + " " + value);
            }
        } catch (java.io.IOException e) {
            throw new Exception("Failed to save defines to file: " + e.getMessage());
        }
    }
}
