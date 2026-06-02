package calculator.commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import calculator.ExecutionContext;

class PrintCommandTest {
    @Test
    void printCommandShouldWriteTopValue() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(8.0);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            new PrintCommand().execute(context, java.util.Collections.emptyList());
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("8.0" + System.lineSeparator(), outContent.toString());
    }
}
