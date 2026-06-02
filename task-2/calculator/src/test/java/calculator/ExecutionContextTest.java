package calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionContextTest {
    @Test
    void pushAndPopShouldFollowLifoOrder() throws Exception {
        ExecutionContext context = new ExecutionContext();

        context.push(1.0);
        context.push(2.0);

        assertEquals(2.0, context.pop());
        assertEquals(1.0, context.pop());
    }

    @Test
    void peekShouldReturnTopValueWithoutRemoving() throws Exception {
        ExecutionContext context = new ExecutionContext();
        context.push(3.0);

        assertEquals(3.0, context.peek());
        assertEquals(3.0, context.peek());
    }

    @Test
    void popEmptyStackShouldThrow() {
        ExecutionContext context = new ExecutionContext();

        assertThrows(Exception.class, context::pop);
    }

    @Test
    void peekEmptyStackShouldThrow() {
        ExecutionContext context = new ExecutionContext();

        assertThrows(Exception.class, context::peek);
    }

    @Test
    void defineAndGetDefineShouldStoreValue() {
        ExecutionContext context = new ExecutionContext();
        context.define("x", 123.45);

        assertEquals(123.45, context.getDefine("x"));
        assertNull(context.getDefine("missing"));
    }
}
