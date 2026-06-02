package factory.core;

import factory.model.Part;
import factory.storage.Storage;

/**
 * A generic supplier of parts (bodies, motors, accessories) that runs in its own thread, periodically producing parts and putting them into the corresponding storage.
 * @param <T> The type of part being supplied (Body, Motor, Accessory).
 */
public class PartSupplier<T extends Part> implements Runnable {
    private final Storage<T> storage;
    private final java.util.function.Supplier<T> factoryMethod;
    private volatile int delayMs = 1000;

    public PartSupplier(Storage<T> storage, java.util.function.Supplier<T> factoryMethod) {
        this.storage = storage;
        this.factoryMethod = factoryMethod;
    }

    public void setDelayMs(int delayMs) { this.delayMs = delayMs; }
    public int getDelayMs() { return delayMs; }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(delayMs);
                T part = factoryMethod.get();
                storage.put(part);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
