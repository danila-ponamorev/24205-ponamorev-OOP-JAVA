package calculator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Calculator.
 */
class CalculatorTest {
    private PrintStream originalErr;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() {
        originalErr = System.err;
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    void executeLineShouldInvokeConfiguredCommand() throws Exception {
        CommandFactory factory = new CommandFactory("commands.txt");
        Calculator calculator = new Calculator(factory);

        calculator.executeLine("PUSH 4");
        calculator.executeLine("PUSH 5");
        calculator.executeLine("ADD");

        assertEquals(9.0, calculator.getContext().peek());
    }

    @Test
    void executeStreamShouldProcessMultipleCommands() throws Exception {
        CommandFactory factory = new CommandFactory("commands.txt");
        Calculator calculator = new Calculator(factory);
        String commands = "DEFINE x 9\nPUSH x\nSQRT\n";

        calculator.executeStream(new ByteArrayInputStream(commands.getBytes()));

        assertEquals(3.0, calculator.getContext().peek());
    }

    @Test
    void executeLineUnknownCommandShouldWriteError() {
        CommandFactory factory = new CommandFactory("commands.txt");
        Calculator calculator = new Calculator(factory);

        calculator.executeLine("UNKNOWN_CMD");
        String errorText = errContent.toString();

        assertTrue(errorText.contains("Error executing 'UNKNOWN_CMD'"));
    }
}
