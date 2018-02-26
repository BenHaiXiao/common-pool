package com.github.benhaixiao.concurrent.constants;

/**
 * @author xiaobenhai
 * 线程池队列模型
 */
public enum ThreadQueueType {

    LinkedBlockingQueue("1"), SynchronousQueue("2"), LinkedBlockingQueueWithQueueSize("3"), SynchronousQueueWithFair("4");;

    private String value = "";

    private ThreadQueueType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
