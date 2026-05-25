package calculator.commands;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import calculator.ExecutionContext;

class AddCommandTest {
    @Test
    void addCommandShouldSumTopTwoValues() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(5.0);
        context.push(7.0);

        new AddCommand().execute(context, Collections.emptyList());

        assertEquals(12.0, context.pop());
    }

    @Test
    void addCommandShouldRestoreValueOnFailure() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(5.0);

        Exception exception = assertThrows(Exception.class, () -> new AddCommand().execute(context, Collections.emptyList()));
        assertNotNull(exception);
        assertEquals(5.0, context.pop());
    }
}
