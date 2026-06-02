package factory.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import factory.model.Auto;
import factory.storage.Storage;

/**
 * Represents a dealer that sells autos from the auto storage. The dealer runs in its own thread, periodically taking autos from storage and logging sales if enabled.
 */
public class Dealer implements Runnable {
    private final int id;
    private final Storage<Auto> autoStorage;
    private final boolean logEnabled;
    private volatile int delayMs = 2000;
    private int totalSold = 0;
    private static PrintWriter logWriter;

    static {
        try {
            logWriter = new PrintWriter(new FileWriter("sales.log", true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dealer(int id, Storage<Auto> autoStorage, boolean logEnabled) {
        this.id = id;
        this.autoStorage = autoStorage;
        this.logEnabled = logEnabled;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(delayMs);
                Auto auto = autoStorage.get();
                totalSold++;
                if (logEnabled) logSale(auto);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logSale(Auto auto) {
        String time = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
        String msg = String.format("%s: Dealer %d: Auto %d (Body: %d, Motor: %d, Accessory: %d)",
                time, id, auto.getId(), auto.getBody().getId(), auto.getMotor().getId(), auto.getAccessory().getId());
        synchronized (Dealer.class) {
            if (logWriter != null) logWriter.println(msg);
        }
    }

    public void setDelayMs(int delayMs) { this.delayMs = delayMs; }
    public int getDelayMs() { return delayMs; }
    public int getTotalSold() { return totalSold; }
}