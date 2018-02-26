package com.github.benhaixiao.concurrent;

import com.github.benhaixiao.concurrent.configure.ThreadPoolConfigure;
import com.github.benhaixiao.concurrent.constants.ThreadQueueType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池，由ThreadPoolManager管理。
 * 各连接池初始化参数见threadpool-config.xml中{@code <threadPool>标签}
 *
 * @author xiaobenhai
 *
 */
public class CustomThreadPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomThreadPool.class);

    private String key = "default"; // 线程池名称
    /**
     * 默认超时时间为2分钟
     */
    private long timeout = 120000;
    //队列类型，1：LinkedBlockingQueue，2：SynchronousQueue
    private String type = "1";
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    private int maxPoolSize = Integer.MAX_VALUE;
    private long keepAliveTime = 2 * 60L;
    private boolean fair = false;
    private int initQueueSize = 100;
    private int showThreadQueueSize = 10;
    private ThreadPoolExecutor taskPool = null;
    private BlockingQueue<Runnable> queue = null;

    /**
     * Scheduled Thread pool used to hold running threads
     */
    private ScheduledExecutorService scdTaskPool = null;

    public CustomThreadPool(ThreadPoolConfigure conf) {
        this(conf, null, null);
    }

    public CustomThreadPool(ThreadPoolConfigure conf, ThreadFactory factory) {
        this(conf, factory, null);
    }

    public CustomThreadPool(ThreadPoolConfigure conf, ThreadFactory factory, RejectedExecutionHandler handler) {
        this.key = conf.getKey();
        this.timeout = conf.getTimeout() == 0 ? timeout : conf.getTimeout();
        this.maxPoolSize = conf.getMaxPoolSize() == 0 ? maxPoolSize : conf.getMaxPoolSize();
        this.corePoolSize = conf.getCorePoolSize() == 0 ? corePoolSize : conf.getCorePoolSize();
        this.type = StringUtils.isBlank(conf.getType()) ? type : conf.getType();
        this.keepAliveTime = conf.getKeepAliveTime() == 0 ? keepAliveTime : conf.getKeepAliveTime();
        this.fair = conf.isFair() == null ? fair : conf.isFair();
        this.initQueueSize = conf.getInitQueueSize() == 0 ? initQueueSize : conf.getInitQueueSize();
        this.showThreadQueueSize = conf.getShowThreadQueueSize();
        init(factory, handler);
        LOGGER.info("Thread pool: {}, factory.class: {},handler.class:{}", this.toString(),
                    null == factory ? null : factory.getClass(), null == handler ? null : handler.getClass());
    }

    public CustomThreadPool() {
        init(null, null);
    }

    private void init(ThreadFactory factory, RejectedExecutionHandler handler) {
        if (this.timeout > 0) {
            scdTaskPool = Executors.newScheduledThreadPool(this.corePoolSize);
        }
        queue = getBlockQueue();
        /** 相当于fixedThreadPool, 等待队列为queue,便于观测队列长度 */
        if (null == factory && null == handler) {
            taskPool = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue, factory, handler);
        } else if (null != factory) {
            taskPool = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue, factory);
        } else {
            taskPool = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
        }

    }

    /**
     * 执行任务,同时打印当前等待队列长度。利用另一定时任务控制任务超时。
     *
     * @author xiaobenhai
     */
    public Future<?> execute(Runnable task) {
        Future<?> future = taskPool.submit(task);
        /** 当前等待队列长度 */
        int size = queue.size();
        if (this.showThreadQueueSize > -1 && size >= this.showThreadQueueSize) {
            LOGGER.info("task queue length <" + size + "> type<" + type + ">");
        }
        /** 判断超时 */
        isOvertime(future);
        return future;
    }

    /**
     * 执行任务,同时打印当前等待队列长度。利用另一定时任务控制任务超时。
     *
     * @author xiaobenhai
     */
    public <T> Future<T> submit(Callable<T> task) {
        Future<T> future = taskPool.submit(task);
        int size = queue.size();
        if (this.showThreadQueueSize > -1 && size >= this.showThreadQueueSize) {
            LOGGER.info("task queue length <" + size + "> type<" + type + ">");
        }
        /** 判断超时 */
        isOvertime(future);
        return future;
    }

    private BlockingQueue<Runnable> getBlockQueue() {
        BlockingQueue<Runnable> blockingQueue;
        if (this.type.equals(ThreadQueueType.SynchronousQueueWithFair.getValue())) {
            blockingQueue = new SynchronousQueue<Runnable>(this.fair);
        } else if (this.type.equals(ThreadQueueType.LinkedBlockingQueue.getValue())) {
            blockingQueue = new LinkedBlockingQueue<Runnable>();
        } else if (this.type.equals(ThreadQueueType.LinkedBlockingQueueWithQueueSize.getValue())) {
            blockingQueue = new LinkedBlockingQueue<Runnable>(this.initQueueSize);
        } else {
            blockingQueue = new SynchronousQueue<Runnable>();
        }
        return blockingQueue;
    }

    private void isOvertime(final Future future) {
        /** 判断超时 */
        if (this.timeout > 0) {
            scdTaskPool.schedule(new Runnable() {
                public void run() {
                    if (!future.isDone()) {
                        future.cancel(true);
                        LOGGER.info("task cancel because out of time: over <" + timeout + "ms> type<" + type + ">");
                    }
                }
            }, timeout, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 等待线程池执行完成所有任务
     */
    public boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        /** 等待子线程执行完毕 */
        return taskPool.awaitTermination(timeout, timeUnit);

    }

    public List<Runnable> shutdownNow() throws InterruptedException {
        /** 等待子线程执行完毕 */
        return taskPool.shutdownNow();
    }

    public BlockingQueue<Runnable> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    public void setPoolSize(int corePoolSize, int maxPoolSize) {
        taskPool.setCorePoolSize(corePoolSize);
        taskPool.setMaximumPoolSize(maxPoolSize);
    }

    public int getActiveCout() {
        return taskPool.getActiveCount();
    }

    public int getPoolSize() {
        return taskPool.getPoolSize();
    }

    public ThreadPoolExecutor getTaskPool() {
        return taskPool;
    }

    public void shutdown() {
        taskPool.shutdown();
        LOGGER.info("threadpool[" + this.type + "] is shutdown:" + taskPool.isShutdown());
    }

    @Override
    public String toString() {
        return "CustomThreadPool{" +
               "key='" + key + '\'' +
               ", timeout=" + timeout +
               ", type='" + type + '\'' +
               ", corePoolSize=" + corePoolSize +
               ", maxPoolSize=" + maxPoolSize +
               ", keepAliveTime=" + keepAliveTime +
               ", fair=" + fair +
               ", initQueueSize=" + initQueueSize +
               ", taskPool=" + taskPool +
               ", queue=" + queue +
               ", scdTaskPool=" + scdTaskPool +
               '}';
    }
}