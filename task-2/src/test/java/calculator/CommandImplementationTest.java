package calculator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import calculator.commands.AddCommand;
import calculator.commands.DefineCommand;
import calculator.commands.PrintCommand;
import calculator.commands.PushCommand;
import calculator.commands.SqrtCommand;

class CommandImplementationTest {
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

    @Test
    void printCommandShouldWriteTopValue() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(8.0);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            new PrintCommand().execute(context, Collections.emptyList());
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("8.0" + System.lineSeparator(), outContent.toString());
    }
}
