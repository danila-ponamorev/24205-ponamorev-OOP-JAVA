package threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ThreadPoolTest {

    @Test
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    void testMultipleTasksAreExecutedConcurrently() throws InterruptedException {
        ThreadPool pool = new ThreadPool(3); // 3 worker threads
        int totalTasks = 10;
        
        CountDownLatch latch = new CountDownLatch(totalTasks);
        AtomicInteger completedTasksCount = new AtomicInteger(0);

        for (int i = 0; i < totalTasks; i++) {
            pool.execute(() -> {
                completedTasksCount.incrementAndGet();
                latch.countDown();
            });
        }

        // Wait up to 2 seconds for all tasks to be processed by the pool
        boolean finishedOnTime = latch.await(2, TimeUnit.SECONDS);

        assertTrue(finishedOnTime, "All tasks should have been completed by the pool");
        assertEquals(totalTasks, completedTasksCount.get());
        
        pool.shutdown();
    }

    @Test
    void testShutdownStopsWorkerThreads() throws InterruptedException {
        ThreadPool pool = new ThreadPool(2);
        pool.shutdown();
        
        Thread.sleep(100); // Wait for threads to receive interrupt signal

        // Verify that executing a task after shutdown is ignored or rejected
        pool.execute(() -> fail("Tasks should not run after pool shutdown"));
        
        assertEquals(0, pool.getQueueSize(), "Queue must be empty or inactive after shutdown");
    }
}