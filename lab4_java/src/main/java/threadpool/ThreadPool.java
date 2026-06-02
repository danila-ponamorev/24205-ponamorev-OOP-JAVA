package threadpool;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple thread pool implementation that manages a fixed number of worker threads and a task queue. It allows submitting tasks to be executed by the worker threads and provides a method to shut down the pool gracefully.
 */
public class ThreadPool {
    private final int nThreads;
    private final PoolWorker[] threads;
    private final Queue<Runnable> queue;
    private volatile boolean isRunning = true;

    public ThreadPool(int nThreads) {
        this.nThreads = nThreads;
        queue = new LinkedList<>();
        threads = new PoolWorker[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            if (isRunning) {
                queue.add(task);
                queue.notify();
            }
        }
    }

    public int getQueueSize() {
        synchronized (queue) {
            return queue.size();
        }
    }

    public void shutdown() {
        isRunning = false;
        synchronized (queue) {
            queue.notifyAll();
        }
        for (Thread t : threads) {
            t.interrupt();
        }
    }

    // Worker thread that continuously takes tasks from the queue and executes them
    private class PoolWorker extends Thread {
        @Override
        public void run() {
            Runnable task;
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                synchronized (queue) {
                    while (queue.isEmpty() && isRunning) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (!isRunning) break;
                    task = queue.poll();
                }
                try {
                    if (task != null) task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
