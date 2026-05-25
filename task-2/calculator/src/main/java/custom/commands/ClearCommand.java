package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("CLEAR")
public class ClearCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        ctx.getStack().clear();
        ctx.getDefines().clear();
    }
}
