package com.taotao.cloud.data.analysis.kafka.qianfeng;

import java.util.Map;
import java.util.Random;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

/**
 * 自定义分区之随机分区
 */
public class _05RandomPartitioner implements Partitioner {

	private Random random = new Random();

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,
		Cluster cluster) {
		// 获取总的分区数
		Integer partitionNum = cluster.partitionCountForTopic(topic);
		// 随机生产
		int i = random.nextInt(partitionNum);
		return i;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
