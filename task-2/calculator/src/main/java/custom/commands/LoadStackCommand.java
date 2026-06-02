package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("LOAD_STACK")
public class LoadStackCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.size() != 1) throw new IllegalArgumentException("LOAD_STACK requires exactly one argument: file name");
        String fileName = args.get(0);
        try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File(fileName))) {
            ctx.clearStack();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    try {
                        Double value = Double.parseDouble(line);
                        ctx.push(value);
                    } catch (NumberFormatException e) {
                        throw new Exception("Invalid number in stack file: " + line);
                    }
                }
            }
        } catch (java.io.IOException e) {
            throw new Exception("Failed to load stack from file: " + e.getMessage());
        }
    }
}
