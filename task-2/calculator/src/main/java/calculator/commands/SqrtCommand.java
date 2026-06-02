package calculator.commands;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

import java.util.List;

@CommandName("SQRT")
public class SqrtCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        Double val = ctx.pop();
        if (val < 0) {
            ctx.push(val);
            throw new Exception("SQRT from negative number");
        }
        ctx.push(Math.sqrt(val));
    }
}
