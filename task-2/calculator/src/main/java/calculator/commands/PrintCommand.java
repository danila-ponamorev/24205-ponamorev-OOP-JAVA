package calculator.commands;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

import java.util.List;

@CommandName("PRINT")
public class PrintCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        System.out.println(ctx.peek());
    }
}
