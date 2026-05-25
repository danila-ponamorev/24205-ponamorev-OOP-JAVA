package calculator.commands;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

import java.util.List;

@CommandName("CLEARSTACK")
public class ClearStackCommand implements Command {
    @Override
    public void execute(ExecutionContext context, List<String> args) throws Exception {
        context.clearStack();
    }
}
