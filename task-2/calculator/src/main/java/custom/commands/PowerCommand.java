package custom.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("POWER")
public class PowerCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        Double exponent = ctx.pop();
        Double base = ctx.pop();
        Double result = Math.pow(base, exponent);
        ctx.push(result);
    }
}
