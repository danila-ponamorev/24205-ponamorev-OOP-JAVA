package factory.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The base class for all parts used in the factory (Body, Motor, Accessory). It provides a unique ID for each part instance and implements Serializable for potential future use in saving/loading state.
 */
public abstract class Part implements Serializable {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int id;

    public Part() {
        this.id = idGenerator.getAndIncrement();
    }

    public int getId() { return id; }
}
