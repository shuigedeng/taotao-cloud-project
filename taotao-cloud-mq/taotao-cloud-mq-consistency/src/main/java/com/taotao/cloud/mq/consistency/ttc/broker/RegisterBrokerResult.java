package com.taotao.cloud.mq.consistency.ttc.broker;

/**
 * Broker 注册结果（对标 RocketMQ RegisterBrokerResult）.
 * <p>
 * 由 NameServer 在注册响应中返回, 携带 HA 服务地址与旧主地址以便主备切换.
 */
public class RegisterBrokerResult {

    private String haServerAddr;
    private String oldAddr;

    public String getHaServerAddr() {
        return haServerAddr;
    }

    public void setHaServerAddr(String haServerAddr) {
        this.haServerAddr = haServerAddr;
    }

    public String getOldAddr() {
        return oldAddr;
    }

    public void setOldAddr(String oldAddr) {
        this.oldAddr = oldAddr;
    }
}
