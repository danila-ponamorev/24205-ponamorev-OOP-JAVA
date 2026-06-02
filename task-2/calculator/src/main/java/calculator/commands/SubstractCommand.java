package calculator.commands;

import java.util.List;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

@CommandName("SUB")
public class SubstractCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        Double a = ctx.pop();
        try {
            Double b = ctx.pop();
            ctx.push(b - a);
        } catch (Exception e) {
            ctx.push(a);
            throw e;
        }
    }
    
}
