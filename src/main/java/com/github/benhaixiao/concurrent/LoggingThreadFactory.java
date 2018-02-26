package com.github.benhaixiao.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可自定义命名线程，并定义{@link Thread.UncaughtExceptionHandler}的实现：进行logger.error的输出。
 *
 * @author xiaobenhai
 */
public class LoggingThreadFactory implements ThreadFactory {

    private static Logger logger = LoggerFactory.getLogger(LoggingThreadFactory.class);

    private final AtomicInteger threadCreationCounter = new AtomicInteger();
    private String name;

    public LoggingThreadFactory(String name) {
        this.name = name;
    }

    public LoggingThreadFactory(String name, Logger logger) {
        this.name = name;
        this.logger = logger;
    }

    @Override
    public Thread newThread(Runnable r) {
        int threadNumber = threadCreationCounter.incrementAndGet();
        Thread workerThread = new Thread(r, name + "-" + threadNumber);

        workerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Thread {} {}", t.getName(), e);
            }
        });

        return workerThread;
    }


}
