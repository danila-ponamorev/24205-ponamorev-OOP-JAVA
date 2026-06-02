package factory.core;

import factory.model.Accessory;
import factory.model.Auto;
import factory.model.Body;
import factory.model.Motor;
import factory.storage.Storage;
import threadpool.ThreadPool;

/**
* Controls the production of autos by monitoring the auto storage and ensuring that new autos are produced when needed.
 */
public class StorageController implements Runnable {
    private final Storage<Auto> autoStorage;
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final ThreadPool threadPool;
    private final Object monitor = new Object();

    public StorageController(Storage<Auto> autoStorage, Storage<Body> bodyStorage,
                             Storage<Motor> motorStorage, Storage<Accessory> accessoryStorage,
                             ThreadPool threadPool) {
        this.autoStorage = autoStorage;
        this.bodyStorage = bodyStorage;
        this.motorStorage = motorStorage;
        this.accessoryStorage = accessoryStorage;
        this.threadPool = threadPool;

        autoStorage.setOnTakeCallback(() -> {
            synchronized (monitor) {
                monitor.notify();
            }
        });

        produceNeededAutos();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    monitor.wait();
                }
                produceNeededAutos();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void produceNeededAutos() {
        int needed = autoStorage.getCapacity() - autoStorage.getSize() - threadPool.getQueueSize();
        for (int i = 0; i < needed; i++) {
            threadPool.execute(this::assembleAuto);
        }
    }

    private void assembleAuto() {
        try {
            Body body = bodyStorage.get();
            Motor motor = motorStorage.get();
            Accessory accessory = accessoryStorage.get();
            Auto auto = new Auto(body, motor, accessory);
            autoStorage.put(auto);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}