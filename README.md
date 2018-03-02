# common-pool

Java开发过程中对线程池java.util.concurrent包下面的使用相当频繁，对线程池的配置和管理工作相当繁琐，可能出现如下情况：
1. 代码中到处都是线程池，发现性能瓶颈查找困难；
2. 不便于代码的整洁和规范。
该项目包装java.util.concurrent包，方便Java并发编程；
核心功能如下：
1. 对外提供一套统一规范的线程池使用规范；
2. 支持线程可配置化管理，灵活性高；
3. 支持超时控制。
该模型在大公司生产环境得到实践验证可行。

##如何使用common-pool

1. 自定义enum ThreadPoolType线程池。
一个App有多少个线程池就定义多少个枚举类型

2. 配置common-config.xml。

      ```
      <root>
            <!-- 线程池配置 start 可配置多个 -->
            <!-- 公共线程池（初始化系统信息等公共操作使用） -->
            <threadPool>
                <key>default</key>
                <type>1</type><!-- 线程池队列模型 ：LinkedBlockingQueue("1"), SynchronousQueue("2"), LinkedBlockingQueueWithQueueSize("3"), SynchronousQueueWithFair("4") -->
                <corePoolSize>10</corePoolSize><!-- 最小线程数-->
                <maxPoolSize>100</maxPoolSize><!-- 最大线程数-->
                <timeout>0</timeout><!-- 超时时间，0为不进行超时控制-->
                <showThreadQueueSize>10</showThreadQueueSize><!-- 日志监控，队列中超过该值时候打印告警日志-->
            </threadPool>
        </root>
        ```
      ```
      //xml注入Bean 泛型
        ConfigureContext configureContext = new DefaultConfigure<ConfigureContext>().init("/common-config.xml", ConfigureContext.class);
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
        ```
        
   
## 相关Demo 

非Spring 环境：

```com.github.benhaixiao.concurrent.demo.mainDemo.Main```
Spring环境：

``` com.github.benhaixiao.concurrent.demo.springDemo.SpringThreadPoolMain```






