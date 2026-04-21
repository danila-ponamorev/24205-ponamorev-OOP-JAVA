package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import calculator.commands.AddCommand;
import calculator.commands.DefineCommand;
import calculator.commands.PrintCommand;
import calculator.commands.PushCommand;
import calculator.commands.SqrtCommand;

class CommandFactoryTest {
    @Test
    void shouldCreateAllConfiguredCommands() throws Exception {
        CommandFactory factory = new CommandFactory("commands.txt");

        assertTrue(factory.createCommand("PUSH") instanceof PushCommand);
        assertTrue(factory.createCommand("ADD") instanceof AddCommand);
        assertTrue(factory.createCommand("SQRT") instanceof SqrtCommand);
        assertTrue(factory.createCommand("PRINT") instanceof PrintCommand);
        assertTrue(factory.createCommand("DEFINE") instanceof DefineCommand);
    }

    @Test
    void unknownCommandShouldThrowException() {
        CommandFactory factory = new CommandFactory("commands.txt");

        Exception exception = assertThrows(Exception.class, () -> factory.createCommand("UNKNOWN"));
        assertEquals("UNKNOWN", exception.getMessage());
    }
}
