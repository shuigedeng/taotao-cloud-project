package com.taotao.cloud.data.analysis.kafka.qianfeng;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 加载自定义分区
 */
public class ExactlyOnceProducerTest {

	public static void main(String[] args) throws Exception {
		// 加载自定义分区
		Properties prop = new Properties();
//        prop.put("partition.class",com.day21._05RandomPartitioner.class);
//        prop.put("partition.class",com.day21._06HashPartitioner.class);
		prop.load(ExactlyOnceProducerTest.class
			.getClassLoader().getResourceAsStream("producer.properties"));
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
		String topic = "spark";

		int start = 210;
		int end = start + 9;
		ProducerRecord<String, String> record = null;
		for (int i = start; i < end; i++) {
			record = new ProducerRecord<String, String>(topic, i + "", i + "");
			producer.send(record);
		}
		// 释放资源
		producer.close();
	}
}
