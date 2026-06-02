package calculator.commands;

import calculator.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClearStackCommandTest {

    @Test
    void clearStackShouldRemoveAllValues() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(1.0);
        context.push(2.0);

        new ClearStackCommand().execute(context, Collections.emptyList());

        assertTrue(context.getStack().isEmpty());
    }
}
