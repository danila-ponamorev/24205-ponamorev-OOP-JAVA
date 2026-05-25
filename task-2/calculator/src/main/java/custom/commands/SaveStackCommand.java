package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("SAVE_STACK")
public class SaveStackCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.size() != 1) throw new IllegalArgumentException("SAVE_STACK requires exactly one argument: file name");
        String fileName = args.get(0);
        try (java.io.PrintWriter writer = new java.io.PrintWriter(fileName)) {
            for (Double value : ctx.getStack()) {
                writer.println(value);
            }
        } catch (java.io.IOException e) {
            throw new Exception("Failed to save stack to file: " + e.getMessage());
        }

    }
}
