package edu.hit.ehealth.main.util;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class ThreadPool {

    private static final int MAX_QUEUE_SIZE = 20;
    private Queue<HandlerTask> threadQueue;

    public ThreadPool() {
        threadQueue = new SynchronousQueue<>();
    }

    public void queueTask(HandlerTask task) {
        if (threadQueue.size() < MAX_QUEUE_SIZE) {
            threadQueue.add(task);
        }
    }

    public interface HandlerTask extends Runnable {
        void run();
    }
}
