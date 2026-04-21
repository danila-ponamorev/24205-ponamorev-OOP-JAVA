package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Execution context that holds the stack and defined variables for the calculator.
 */
public class ExecutionContext {
    private final Deque<Double> stack = new ArrayDeque<>();
    private final Map<String, Double> defines = new HashMap<>();

    /**
     * Pushes a value onto the stack.
     * @param value The value to push
     */
    public void push(Double value) { stack.push(value); }

    /**
     * Pops a value from the stack.
     * @return The popped value
     * @throws Exception If the stack is empty
     */
    public Double pop() throws Exception {
        if (stack.isEmpty()) throw new Exception();
        return stack.pop();
    }

    /**
     * Peeks at the top value of the stack without removing it.
     * @return The top value
     * @throws Exception If the stack is empty
     */
    public Double peek() throws Exception {
        if (stack.isEmpty()) throw new Exception();
        return stack.peek();
    }

    /**
     * Defines a variable with a given name and value.
     * @param name The name of the variable
     * @param value The value of the variable
     */
    public void define(String name, Double value) { defines.put(name, value); }
    
    /**
     * Gets the value of a defined variable by name.
     * @param name The name of the variable
     * @return The value of the variable, or null if not defined
     */
    public Double getDefine(String name) { return defines.get(name); }
    
    /**
     * Gets the stack and defined variables for external access.
     * @return The stack
     */
    public Deque<Double> getStack() { return stack; }

    /**
     * Gets the defined variables map.
     * @return The map of defined variables.
     */
    public Map<String, Double> getDefines() { return defines; }
}