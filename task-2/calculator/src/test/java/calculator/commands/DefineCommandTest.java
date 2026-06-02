package calculator.commands;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import calculator.ExecutionContext;

class DefineCommandTest {
    @Test
    void defineCommandShouldStoreValue() throws Exception {
        ExecutionContext context = new ExecutionContext();

        new DefineCommand().execute(context, java.util.List.of("pi", "3.14"));

        assertEquals(3.14, context.getDefine("pi"));
    }

    @Test
    void defineCommandRequiresTwoArguments() {
        ExecutionContext context = new ExecutionContext();

        Exception exception = assertThrows(Exception.class, () -> new DefineCommand().execute(context, Collections.singletonList("x")));
        assertEquals("DEFINE requires 2 arguments", exception.getMessage());
    }

    @Test
    void defineCommandRequiresNumericValue() {
        ExecutionContext context = new ExecutionContext();

        Exception exception = assertThrows(Exception.class, () -> new DefineCommand().execute(context, java.util.List.of("x", "not-a-number")));
        assertEquals("DEFINE second argument must be a number", exception.getMessage());
    }
}
