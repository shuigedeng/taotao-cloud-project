package com.taotao.cloud.bigdata.kafka.qianfeng;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区之hash分区
 * key的hashCode之%partitionNum
 */
public class _06HashPartitioner implements Partitioner {

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取topic的分区
        Integer partitionNum = cluster.partitionCountForTopic(topic);
        if(keyBytes != null){
            return Math.abs(key.hashCode()) % partitionNum;
        }
        return 0; // 如果Key不存在，那么直接返回0
    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}
