package com.github.benhaixiao.concurrent;


import com.github.benhaixiao.concurrent.configure.ConfigureContext;
import com.github.benhaixiao.concurrent.configure.DefaultConfigure;
import com.github.benhaixiao.concurrent.configure.ThreadPoolConfigure;

import com.github.benhaixiao.concurrent.constants.ThreadPoolType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author xiaobenhai
 */
@Service
@Lazy
public class ThreadPoolFactory implements InitializingBean, DisposableBean {

    private final static String CONFIG_FILE = "/common-config.xml";
    private ConfigureContext configureContext;

    @Override
    public void destroy() throws Exception {
        try {
            ConcurrentHashMap<String, CustomThreadPool> threadPoolConcurrentHashMap = CustomThreadPoolManager.getThreadPool();
            for (String type : threadPoolConcurrentHashMap.keySet()) {
                CustomThreadPoolManager.shutdown(type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Interrupted while waiting for ThreadPoolManager ExecutorService to be shutdown.");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        URL url = ThreadPoolFactory.class.getResource(CONFIG_FILE);
        if (null != url) {
            this.configureContext = new DefaultConfigure<ConfigureContext>().init(CONFIG_FILE, ConfigureContext.class);
        }
    }


    public CustomThreadPool getDefaultThreadPool() {
        //使用线程池工具,使用默自定义线程池类型
        ThreadPoolConfigure conf = configureContext.getThreadPoolConfigMap().get(ThreadPoolType.DEFAULT.getValue());
        CustomThreadPool threadPool = CustomThreadPoolManager.getThreadPool(conf, new LoggingThreadFactory("ThreadPoolManager-worker"), new
                DefaultRejectedExecutionHandler());
        return threadPool;
    }

    public CustomThreadPool getThreadPool(ThreadPoolType threadPoolType) {
        //使用线程池工具,使用默自定义线程池类型
        ThreadPoolConfigure conf = configureContext.getThreadPoolConfigMap().get(threadPoolType.getValue());
        CustomThreadPool threadPool = CustomThreadPoolManager.getThreadPool(conf, new LoggingThreadFactory("ThreadPoolManager-worker"), new
                DefaultRejectedExecutionHandler());
        return threadPool;
    }
}
