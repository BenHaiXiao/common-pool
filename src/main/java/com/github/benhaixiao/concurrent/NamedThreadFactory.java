package com.github.benhaixiao.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义命名线程。
 *
 * @author xiaobenhai
 */
public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger threadCreationCounter = new AtomicInteger();

    private String name;

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        int threadNumber = threadCreationCounter.incrementAndGet();
        Thread workerThread = new Thread(r, name + "-" + threadNumber);
        return workerThread;
    }

}
