package factory.storage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class StorageTest {

    @Test
    void testBasicPutAndGet() throws InterruptedException {
        Storage<String> storage = new Storage<>(5);
        
        storage.put("Part A");
        storage.put("Part B");

        assertEquals(2, storage.getSize());
        assertEquals("Part A", storage.get());
        assertEquals(1, storage.getSize());
        assertEquals(2, storage.getTotalProduced());
    }

    @Test
    @Timeout(value = 2, unit = TimeUnit.SECONDS)
    void testGetBlocksWhenEmptyUntilPutOccurs() throws InterruptedException {
        Storage<String> storage = new Storage<>(2);
        final String[] retrievedItem = {null};

        // This thread acts as a Consumer waiting on an empty storage
        Thread consumerThread = new Thread(() -> {
            try {
                retrievedItem[0] = storage.get(); // Blocks here
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumerThread.start();
        Thread.sleep(100); // Give the thread time to start and block

        assertNull(retrievedItem[0], "Consumer must be blocked and have nothing yet");

        // Producer adds an item, which should wake up the consumer
        storage.put("Released Item");
        consumerThread.join(); // Wait for consumer to finish processing

        assertEquals("Released Item", retrievedItem[0]);
    }
}