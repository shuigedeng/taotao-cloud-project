package com.taotao.cloud.mq.consistency.ttc.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Broker 数据（对标 RocketMQ BrokerData）.
 * <p>
 * 描述 Broker 的集群归属、名称与多副本地址（brokerId 0 = Master, >0 = Slave）。
 */
public class BrokerData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Master brokerId. */
    public static final long MASTER_ID = 0L;

    private String cluster;
    private String brokerName;
    /**
     * key: brokerId (0=master, 大于 0 为 slave), value: broker addr (host:port).
     * <p>
     * 声明类型必须是 {@code Map} 而不是 {@code HashMap}：z-util 的 fromJson 只对接口型
     * 声明的 map 字段做绑定，声明成具体实现类时会把整个字段丢成空 map 且**不抛异常**，
     * 于是 brokerAddrs 恒为空、selectBrokerAddr() 恒为 null，路由表拿到了也用不了。
     */
    private Map<Long, String> brokerAddrs = new HashMap<>();

    public BrokerData() {
    }

    public BrokerData(String cluster, String brokerName, HashMap<Long, String> brokerAddrs) {
        this.cluster = cluster;
        this.brokerName = brokerName;
        this.brokerAddrs = brokerAddrs;
    }

    public String selectBrokerAddr() {
        return brokerAddrs.get(MASTER_ID);
    }

    public String getBrokerAddr(long brokerId) {
        return brokerAddrs.get(brokerId);
    }

    public void putBrokerAddr(long brokerId, String addr) {
        this.brokerAddrs.put(brokerId, addr);
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public Map<Long, String> getBrokerAddrs() {
        return brokerAddrs;
    }

    public void setBrokerAddrs(Map<Long, String> brokerAddrs) {
        this.brokerAddrs = brokerAddrs;
    }

    @Override
    public String toString() {
        return "BrokerData{" +
                "cluster='" + cluster + '\'' +
                ", brokerName='" + brokerName + '\'' +
                ", brokerAddrs=" + brokerAddrs +
                '}';
    }
}
