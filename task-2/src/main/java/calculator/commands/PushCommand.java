package calculator.commands;

import calculator.Command;
import calculator.CommandName;
import calculator.ExecutionContext;

import java.util.List;

@CommandName("PUSH")
public class PushCommand implements Command {
    @Override
    public void execute(ExecutionContext ctx, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new Exception("PUSH missing argument");
        }
        String arg = args.get(0);
        try {
            Double val = ctx.getDefine(arg);
            if (val != null) {
                ctx.push(val);
            } else {
                ctx.push(Double.parseDouble(arg));
            }
        } catch (NumberFormatException e) {
            throw new Exception("PUSH argument is neither a defined parameter nor a number", e);
        }
    }
}
