package com.taotao.cloud.mq.consistency.ttc.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Topic 路由数据（对标 RocketMQ TopicRouteData）.
 * <p>
 * 包含 Topic 下的所有 QueueData + BrokerData，用于 Producer/Consumer 路由。
 */
public class TopicRouteData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 系统 flag：0 正常 / 1 顺序 */
    private int orderTopicConf;
    /** 是否是顺序 Topic. */
    private boolean order;
    private List<QueueData> queueDatas = new ArrayList<>();
    private List<BrokerData> brokerDatas = new ArrayList<>();
    private Map<String, String> filterServerOuterMap = new HashMap<>();

    public TopicRouteData() {
    }

    public int getOrderTopicConf() {
        return orderTopicConf;
    }

    public void setOrderTopicConf(int orderTopicConf) {
        this.orderTopicConf = orderTopicConf;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public List<QueueData> getQueueDatas() {
        return queueDatas;
    }

    public void setQueueDatas(List<QueueData> queueDatas) {
        this.queueDatas = queueDatas;
    }

    public List<BrokerData> getBrokerDatas() {
        return brokerDatas;
    }

    public void setBrokerDatas(List<BrokerData> brokerDatas) {
        this.brokerDatas = brokerDatas;
    }

    public Map<String, String> getFilterServerOuterMap() {
        return filterServerOuterMap;
    }

    public void setFilterServerOuterMap(Map<String, String> filterServerOuterMap) {
        this.filterServerOuterMap = filterServerOuterMap;
    }

    @Override
    public String toString() {
        return "TopicRouteData{" +
                "order=" + order +
                ", queueDatas=" + queueDatas +
                ", brokerDatas=" + brokerDatas +
                '}';
    }
}
