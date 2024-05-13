package com.taotao.cloud.mq.broker.dto.persist;


/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqMessagePersistPull {

    /**
     * 消息体
     */
    private MqConsumerPullReq pullReq;

    /**
     * 地址信息
     */
    private RpcAddress rpcAddress;

    public MqConsumerPullReq getPullReq() {
        return pullReq;
    }

    public void setPullReq(MqConsumerPullReq pullReq) {
        this.pullReq = pullReq;
    }

    public RpcAddress getRpcAddress() {
        return rpcAddress;
    }

    public void setRpcAddress(RpcAddress rpcAddress) {
        this.rpcAddress = rpcAddress;
    }
}
