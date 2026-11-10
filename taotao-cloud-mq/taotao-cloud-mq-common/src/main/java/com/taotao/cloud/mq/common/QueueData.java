package com.taotao.cloud.mq.common;

import java.io.Serializable;

/**
 * 队列数据（对标 RocketMQ QueueData）.
 * <p>
 * 描述单个 Broker 上承载的 Topic 队列数量与权限。
 */
public class QueueData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String brokerName;
    private int readQueueNums;
    private int writeQueueNums;
    private int perm;
    private int topicSysFlag;

    public QueueData() {
    }

    public QueueData(String brokerName, int readQueueNums, int writeQueueNums, int perm) {
        this.brokerName = brokerName;
        this.readQueueNums = readQueueNums;
        this.writeQueueNums = writeQueueNums;
        this.perm = perm;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
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

    public int getTopicSysFlag() {
        return topicSysFlag;
    }

    public void setTopicSysFlag(int topicSysFlag) {
        this.topicSysFlag = topicSysFlag;
    }

    @Override
    public String toString() {
        return "QueueData{" +
                "brokerName='" + brokerName + '\'' +
                ", readQueueNums=" + readQueueNums +
                ", writeQueueNums=" + writeQueueNums +
                ", perm=" + perm +
                '}';
    }
}
