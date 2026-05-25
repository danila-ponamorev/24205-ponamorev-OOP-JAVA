package calculator.commands;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import calculator.ExecutionContext;

class PushCommandTest {
    @Test
    void pushCommandShouldParseNumber() throws Exception {
        ExecutionContext context = new ExecutionContext();

        new PushCommand().execute(context, Collections.singletonList("3.5"));

        assertEquals(3.5, context.pop());
    }

    @Test
    void pushCommandShouldUseDefinedValue() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.define("x", 2.5);

        new PushCommand().execute(context, Collections.singletonList("x"));

        assertEquals(2.5, context.pop());
    }

    @Test
    void pushCommandMissingArgumentShouldThrow() {
        ExecutionContext context = new ExecutionContext();

        Exception exception = assertThrows(Exception.class, () -> new PushCommand().execute(context, Collections.emptyList()));
        assertEquals("PUSH missing argument", exception.getMessage());
    }
}
