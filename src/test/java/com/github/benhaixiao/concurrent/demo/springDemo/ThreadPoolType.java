package com.github.benhaixiao.concurrent.demo.springDemo;

/**
 * @author xiaobenhai
 */
public enum ThreadPoolType {
    DEFAULT("default"), THRIFT("thrift"), THRIFTWITIQUEUESIZE("thriftWithQueueSize"), THRIFTWITHFAIR("thriftWithFair");

    private String value = "";

    private ThreadPoolType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
