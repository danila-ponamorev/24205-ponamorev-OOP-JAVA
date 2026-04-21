package calculator.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("ADD")
public class AddCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        Double a = ctx.pop();
        try {
            Double b = ctx.pop();
            ctx.push(a + b);
        } catch (Exception e) {
            ctx.push(a);
            throw e;
        }
    }
}
