package com.github.benhaixiao.concurrent.configure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobenhai
 * 调用方定义的配置类
 * 若属性与xml同名，则只需要配置@XmlRootElement
 */
@XmlRootElement(name = "root")
public class ConfigureContext {


    private List<ThreadPoolConfigure> threadPoolConfigures;

    public List<ThreadPoolConfigure> getThreadPoolConfigures() {
        return threadPoolConfigures;
    }

    @XmlElement(name = "threadPool")
    public void setThreadPoolConfigures(List<ThreadPoolConfigure> threadPoolConfigures) {
        this.threadPoolConfigures = threadPoolConfigures;
    }

    public Map<String, ThreadPoolConfigure> getThreadPoolConfigMap() {
        Map<String, ThreadPoolConfigure> map = new HashMap<String, ThreadPoolConfigure>();
        map.clear();
        for (ThreadPoolConfigure config : this.threadPoolConfigures) {
            map.put(config.getKey(), config);
        }
        return map;
    }
}
