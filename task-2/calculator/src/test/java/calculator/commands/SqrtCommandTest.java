package calculator.commands;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import calculator.ExecutionContext;

class SqrtCommandTest {
    @Test
    void sqrtCommandShouldComputeSquareRoot() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(16.0);

        new SqrtCommand().execute(context, Collections.emptyList());

        assertEquals(4.0, context.pop());
    }

    @Test
    void sqrtCommandShouldFailOnNegative() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(-4.0);

        Exception exception = assertThrows(Exception.class, () -> new SqrtCommand().execute(context, Collections.emptyList()));
        assertEquals("SQRT from negative number", exception.getMessage());
        assertEquals(-4.0, context.pop());
    }
}
