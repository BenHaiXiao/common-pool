package com.github.benhaixiao.concurrent.demo.spring;

/**
 * @author xiaobenhai
 */
public class HealthInfo {
    private long poolSize;
    private long corePoolSize;
    private long largestPoolSize;
    private long activeCount;  //正在执行任务的线程数
    private long taskCount;  //接收到的任务数
    private long completedTaskCount;  //已完成任务数
    private long workQueueSize;
    private long workQueueRemain;

    public long getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(long poolSize) {
        this.poolSize = poolSize;
    }

    public long getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(long corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public long getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(long largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(long workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    public long getWorkQueueRemain() {
        return workQueueRemain;
    }

    public void setWorkQueueRemain(long workQueueRemain) {
        this.workQueueRemain = workQueueRemain;
    }

    @Override
    public String toString() {
        return "HealthInfo{" +
                "poolSize=" + poolSize +
                ", corePoolSize=" + corePoolSize +
                ", largestPoolSize=" + largestPoolSize +
                ", activeCount=" + activeCount +
                ", taskCount=" + taskCount +
                ", completedTaskCount=" + completedTaskCount +
                ", workQueueSize=" + workQueueSize +
                ", workQueueRemain=" + workQueueRemain +
                '}';
    }
}
