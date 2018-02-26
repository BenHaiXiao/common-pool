package com.github.benhaixiao.concurrent.demo.spring;

import com.github.benhaixiao.concurrent.CustomThreadPool;
import com.github.benhaixiao.concurrent.ThreadPoolFactory;
import com.github.benhaixiao.concurrent.constants.ThreadPoolType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.*;

/**
 * Created by xiaobenhai
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:common-pool.xml")
public class ThreadPoolDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolDemo.class);
    private CustomThreadPool threadPool;
    @Autowired
    private ThreadPoolFactory threadPoolFactory;
    @Before
    public void before(){
        this.threadPool = threadPoolFactory.getThreadPool(ThreadPoolType.THRIFT);
    }

   @Test
    public void doAction()throws Exception{
        monitoring(threadPool);
        while (true){
            Future<Integer> future = threadPool.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    LOGGER.info("[doAction] is start");
                    Thread.sleep(1000);
                    LOGGER.info("[doAction] is end");
                    return 0;
                }
            });
            Integer count = future.get(2000L, TimeUnit.MILLISECONDS);
        }
    }



    private static void monitoring(final CustomThreadPool threadPool) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000L);
                        ThreadPoolExecutor taskPool = threadPool.getTaskPool();
                        HealthInfo healthInfo = getHealthInfo(taskPool);
                        LOGGER.info("[monitoring],healthInfo:{}",healthInfo);

                    }catch (Exception e){

                    }

                }

            }
        }).start();

    }
    public static HealthInfo getHealthInfo(ThreadPoolExecutor thread ) {
        HealthInfo info = new HealthInfo();
        info.setPoolSize(thread.getPoolSize());
        info.setCorePoolSize(thread.getCorePoolSize());
        info.setLargestPoolSize(thread.getLargestPoolSize());
        info.setActiveCount(thread.getActiveCount());
        info.setTaskCount(thread.getTaskCount());
        info.setCompletedTaskCount(thread.getCompletedTaskCount());
        BlockingQueue<Runnable> queue = thread.getQueue();
        if (queue != null) {
            info.setWorkQueueSize(queue.size());
            info.setWorkQueueRemain(queue.remainingCapacity());
        }
        return info;
    }



}
