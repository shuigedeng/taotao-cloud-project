package com.taotao.cloud.mq.consistency.ttc.common;

import java.io.Serializable;

/**
 * Topic 配置（对标 RocketMQ TopicConfig）.
 * <p>
 * 描述 Topic 的创建者、队列数、读写权限、是否顺序消息等。
 */
public class TopicConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_READ_QUEUE_NUMS = 4;
    public static final int DEFAULT_WRITE_QUEUE_NUMS = 4;
    public static final int PERM_READ = 1;
    public static final int PERM_WRITE = 2;
    public static final int PERM_READ_WRITE = PERM_READ | PERM_WRITE;
    public static final int PERM_INHERIT = 0;

    private String topicName;
    private int readQueueNums = DEFAULT_READ_QUEUE_NUMS;
    private int writeQueueNums = DEFAULT_WRITE_QUEUE_NUMS;
    private int perm = PERM_READ_WRITE;
    private boolean order = false;
    private int topicSysFlag = 0;
    private boolean unit = false;

    public TopicConfig() {
    }

    public TopicConfig(String topicName) {
        this.topicName = topicName;
    }

    public TopicConfig(String topicName, int readQueueNums, int writeQueueNums, int perm) {
        this.topicName = topicName;
        this.readQueueNums = readQueueNums;
        this.writeQueueNums = writeQueueNums;
        this.perm = perm;
    }

    public static String buildClusterName(String brokerName) {
        return "DefaultCluster";
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getReadQueueNums() {
        return readQueueNums;
    }

    public void setReadQueueNums(int readQueueNums) {
        this.readQueueNums = readQueueNums;
    }

    public int getWriteQueueNums() {
        return writeQueueNums;
    }

    public void setWriteQueueNums(int writeQueueNums) {
        this.writeQueueNums = writeQueueNums;
    }

    public int getPerm() {
        return perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public int getTopicSysFlag() {
        return topicSysFlag;
    }

    public void setTopicSysFlag(int topicSysFlag) {
        this.topicSysFlag = topicSysFlag;
    }

    public boolean isUnit() {
        return unit;
    }

    public void setUnit(boolean unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "TopicConfig{" +
                "topicName='" + topicName + '\'' +
                ", readQueueNums=" + readQueueNums +
                ", writeQueueNums=" + writeQueueNums +
                ", perm=" + perm +
                ", order=" + order +
                '}';
    }
}
