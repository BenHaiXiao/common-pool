package com.github.benhaixiao.concurrent.demo.mainDemo;

import com.github.benhaixiao.concurrent.CustomThreadPool;
import com.github.benhaixiao.concurrent.CustomThreadPoolManager;
import com.github.benhaixiao.concurrent.DefaultRejectedExecutionHandler;
import com.github.benhaixiao.concurrent.LoggingThreadFactory;
import com.github.benhaixiao.concurrent.configure.DefaultConfigure;
import com.github.benhaixiao.concurrent.configure.ThreadPoolConfigure;
import com.github.benhaixiao.concurrent.configure.ConfigureContext;
import com.github.benhaixiao.concurrent.demo.springDemo.ThreadPoolType;

/**
 * @author xiaobenhai
 */
public class Main {

    public static void main(String[] args) {
        //xml注入Bean 泛型
        ConfigureContext configureContext = new DefaultConfigure<ConfigureContext>().init("/common-config.xml", ConfigureContext.class);
        System.out.println(configureContext.getThreadPoolConfigures());
        //使用线程池工具,使用默自定义线程池类型
        ThreadPoolConfigure conf = configureContext.getThreadPoolConfigMap().get(ThreadPoolType.THRIFT.getValue());
        CustomThreadPool threadPool = CustomThreadPoolManager.getThreadPool(conf, new LoggingThreadFactory("common-pool-worker"), new
                DefaultRejectedExecutionHandler());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!!!");
            }
        });
    }

}
