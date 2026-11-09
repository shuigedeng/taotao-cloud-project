package com.taotao.cloud.mq.consistency.ttc.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Broker 注册结果（对标 RocketMQ RegisterBrokerResult）.
 * <p>
 * Broker 向 NameServer 注册后，NameServer 把这个对象放在响应 Body 里返回。
 */
public class RegisterBrokerResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String haServerAddr;
    private String masterAddr;
    /**
     * 声明成 Collection 而不是 Set：z-util JsonUtil 的容器分支只认 List / Collection / Map，
     * Set 字段读回时会去找 Set 的构造器并抛异常（实测）。写入方给的仍是 HashSet，
     * 调用方按 Collection 用即可，线上 JSON 形状不变（仍是 JSON 数组）。
     */
    private Collection<String> changedTopicSet = new HashSet<>();

    public String getHaServerAddr() {
        return haServerAddr;
    }

    public void setHaServerAddr(String haServerAddr) {
        this.haServerAddr = haServerAddr;
    }

    public String getMasterAddr() {
        return masterAddr;
    }

    public void setMasterAddr(String masterAddr) {
        this.masterAddr = masterAddr;
    }

    public Collection<String> getChangedTopicSet() {
        return changedTopicSet;
    }

    public void setChangedTopicSet(Collection<String> changedTopicSet) {
        this.changedTopicSet = changedTopicSet;
    }

    @Override
    public String toString() {
        return "RegisterBrokerResult{" +
                "haServerAddr='" + haServerAddr + '\'' +
                ", masterAddr='" + masterAddr + '\'' +
                ", changedTopicSet=" + changedTopicSet +
                '}';
    }
}
