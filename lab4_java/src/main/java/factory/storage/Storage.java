package factory.storage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
* A thread-safe storage class that holds parts (bodies, motors, accessories) and assembled autos. It has a fixed capacity and provides methods for putting items into storage and taking items out. It also keeps track of the total number of items produced and allows for an optional callback to be executed whenever an item is taken from storage.
 */
public class Storage<T> implements Serializable {
    private final int capacity;
    private final Queue<T> items = new LinkedList<>();
    private int totalProduced = 0;
    private transient Runnable onTakeCallback = null;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public void setOnTakeCallback(Runnable callback) {
        this.onTakeCallback = callback;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() >= capacity) {
            wait();
        }
        items.add(item);
        totalProduced++;
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (items.isEmpty()) {
            wait(); 
        }
        T item = items.poll();
        notifyAll();
        
        if (onTakeCallback != null) {
            onTakeCallback.run();
        }
        
        return item;
    }

    public synchronized int getSize() { return items.size(); }
    public synchronized int getTotalProduced() { return totalProduced; }
    public int getCapacity() { return capacity; }
}
