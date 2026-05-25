package calculator;

import java.util.List;

/**
 * Interface for calculator commands.
 */
public interface Command {
    void execute(ExecutionContext context, List<String> args) throws Exception;
}