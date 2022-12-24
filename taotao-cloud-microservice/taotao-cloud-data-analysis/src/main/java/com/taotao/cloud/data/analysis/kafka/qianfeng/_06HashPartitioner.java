package com.taotao.cloud.data.analysis.kafka.qianfeng;

import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * 自定义分区之hash分区 key的hashCode之%partitionNum
 */
public class _06HashPartitioner implements Partitioner {

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,
		Cluster cluster) {
		// 获取topic的分区
		Integer partitionNum = cluster.partitionCountForTopic(topic);
		if (keyBytes != null) {
			return Math.abs(key.hashCode()) % partitionNum;
		}
		return 0; // 如果Key不存在，那么直接返回0
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
