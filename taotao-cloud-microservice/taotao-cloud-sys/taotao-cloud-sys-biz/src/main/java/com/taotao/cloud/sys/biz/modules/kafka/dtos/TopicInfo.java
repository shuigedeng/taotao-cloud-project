package com.taotao.cloud.sys.biz.modules.kafka.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 对应 kafka 数据的 TopicDescription
 */
@Data
public class TopicInfo {
    private String name;
    private boolean internal;
    private List<TopicPartitionInfo> partitions = new ArrayList<>();

    public TopicInfo(String name, boolean internal) {
        this.name = name;
        this.internal = internal;
    }

    /**
     * 添加分区信息
     * @param partitionInfo
     */
    public void addPartitionInfo(TopicPartitionInfo partitionInfo){
        partitions.add(partitionInfo);
    }

    @Data
    public static class TopicPartitionInfo {
        private int partition;
        private BrokerInfo leader;
        private List<BrokerInfo> replicas;
        private List<BrokerInfo> isr;

        public TopicPartitionInfo(int partition, BrokerInfo leader, List<BrokerInfo> replicas, List<BrokerInfo> isr) {
            this.partition = partition;
            this.leader = leader;
            this.replicas = replicas;
            this.isr = isr;
        }
    }
}
