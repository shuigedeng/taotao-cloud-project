package com.taotao.cloud.data.analysis.kafka.qianfeng;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * 自定义分区之轮询分区
 */
public class _07RoundRobinPartitioner implements Partitioner {

	/*
	 轮询操作 ：我们需要创建轮询对象
	 counter.getAndIncrement() = i++
	 */
	private AtomicInteger counter = new AtomicInteger();

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,
		Cluster cluster) {
		Integer partitionNum = cluster.partitionCountForTopic(topic);
		return counter.getAndIncrement() % partitionNum;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
