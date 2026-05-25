package calculator.commands;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

import java.util.List;

@CommandName("DEFINE")
public class DefineCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.size() < 2) {
            throw new Exception("DEFINE requires 2 arguments");
        }
        try {
            ctx.define(args.get(0), Double.parseDouble(args.get(1)));
        } catch (NumberFormatException e) {
            throw new Exception("DEFINE second argument must be a number", e);
        }
    }
}
