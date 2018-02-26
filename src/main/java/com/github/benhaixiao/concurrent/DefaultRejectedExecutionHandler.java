package com.github.benhaixiao.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaobenhai
 */
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRejectedExecutionHandler.class);

    private AtomicInteger rejected = new AtomicInteger(0);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        LOGGER.info("reject: {}, count: {}", r, rejected.incrementAndGet());
    }
}
