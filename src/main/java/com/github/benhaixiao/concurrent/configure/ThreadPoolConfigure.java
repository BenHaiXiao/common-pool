package com.github.benhaixiao.concurrent.configure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author xiaobenhai
 */
@XmlRootElement(name = "threadPool")
public class ThreadPoolConfigure {

    private String key;

    private String type;

    private int corePoolSize;

    private int maxPoolSize;

    private long keepAliveTime;

    private long timeout;

    private Boolean fair;

    private int initQueueSize;
    //超过此值时打印线程队列长度，-1不打印
    private int showThreadQueueSize;

    public String getKey() {
        return key;
    }

    @XmlElement(name = "key")
    public void setKey(String key) {
        this.key = key;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    @XmlElement(name = "corePoolSize")
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    @XmlElement(name = "maxPoolSize")
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    @XmlElement(name = "keepAliveTime")
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public String getType() {
        return type;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public long getTimeout() {
        return timeout;
    }

    @XmlElement(name = "timeout")
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    public Boolean isFair() {
        return fair;
    }

    @XmlElement(name = "fair")
    public void setFair(Boolean fair) {
        this.fair = fair;
    }

    public int getInitQueueSize() {
        return initQueueSize;
    }

    @XmlElement(name = "initQueueSize")
    public void setInitQueueSize(int initQueueSize) {
        this.initQueueSize = initQueueSize;
    }

    public int getShowThreadQueueSize() {
        return showThreadQueueSize;
    }

    @XmlElement(name = "showThreadQueueSize")
    public void setShowThreadQueueSize(int showThreadQueueSize) {
        this.showThreadQueueSize = showThreadQueueSize;
    }

    @Override
    public String toString() {
        return "ThreadPoolConfigure{" +
               "key='" + key + '\'' +
               ", type='" + type + '\'' +
               ", corePoolSize=" + corePoolSize +
               ", maxPoolSize=" + maxPoolSize +
               ", keepAliveTime=" + keepAliveTime +
               ", timeout=" + timeout +
               ", fair=" + fair +
               ", initQueueSize=" + initQueueSize +
               ", showThreadQueueSize=" + showThreadQueueSize +
               '}';
    }
}
