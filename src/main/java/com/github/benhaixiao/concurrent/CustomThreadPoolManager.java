package com.github.benhaixiao.concurrent;

import com.github.benhaixiao.concurrent.configure.ThreadPoolConfigure;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 *
 * 管理各线程池,线程池初始化配置见threadpool-config.xml
 *
 * @author xiaobenhai
 */
public class CustomThreadPoolManager {

    private static ConcurrentHashMap<String, CustomThreadPool> threadPools = new ConcurrentHashMap<String, CustomThreadPool>();

    public static CustomThreadPool getThreadPool(ThreadPoolConfigure conf) {
        String key = conf.getKey();
        if (!threadPools.containsKey(key)) {
            synchronized (threadPools) {
                if (!threadPools.containsKey(key)) {
                    threadPools.put(key, new CustomThreadPool(conf));
                }
            }
        }
        return threadPools.get(key);
    }

    public static CustomThreadPool getThreadPool(ThreadPoolConfigure conf, ThreadFactory factory) {
        String key = conf.getKey();
        if (!threadPools.containsKey(key)) {
            synchronized (threadPools) {
                if (!threadPools.containsKey(key)) {
                    threadPools.put(key, new CustomThreadPool(conf, factory));
                }
            }
        }
        return threadPools.get(key);
    }

    public static CustomThreadPool getThreadPool(ThreadPoolConfigure conf, ThreadFactory factory, RejectedExecutionHandler handler) {
        String key = conf.getKey();
        if (!threadPools.containsKey(key)) {
            synchronized (threadPools) {
                if (!threadPools.containsKey(key)) {
                    threadPools.put(key, new CustomThreadPool(conf, factory, handler));
                }
            }
        }
        return threadPools.get(key);
    }

    public static ConcurrentHashMap<String, CustomThreadPool> getThreadPool() {
        return threadPools;
    }

    public static void shutdown(String type) {
        CustomThreadPool pool = threadPools.get(type);
        if (null == pool) {
            return;
        }
        pool.shutdown();
        threadPools.remove(type);
    }

    public static void shutdownAll() {
        List<CustomThreadPool> customThreadPoolList = Lists.newArrayList(threadPools.values());
        if (CollectionUtils.isNotEmpty(customThreadPoolList)) {
            for (CustomThreadPool customThreadPool : customThreadPoolList) {
                customThreadPool.shutdown();
                try {
                    if (!customThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                        customThreadPool.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted while waiting for customThreadPool to be shutdown.");
                }
            }
        }
        threadPools.clear();
    }
}
