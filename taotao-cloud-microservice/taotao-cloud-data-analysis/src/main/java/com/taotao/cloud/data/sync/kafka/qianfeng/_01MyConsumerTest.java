package com.taotao.cloud.data.sync.kafka.qianfeng;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Kafka 消费者 API
 */
public class _01MyConsumerTest {

	public static void main(String[] args) throws Exception {
		try {
			Properties prop = new Properties();
			prop.load(
				_01MyProducerTest.class.getClassLoader()
					.getResourceAsStream("consumer.properties"));
			// 构建消费者
			KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);
			// 消费对应的Topic数据
			consumer.subscribe(Arrays.asList("hadoop"));
			// 打印所有相关的数据信息
			System.out.println("topic\tpartition\toffset\tkey\tvalue");
			while (true) {
                /*
                 消费数据 timeout:从consumer的缓冲区Buffer中获取可用数据的等待超时时间
                 如果设置为0，则会理解返回该缓冲区内的所有数据，如果不设置为零，返回空，并且不能写负数
                 */
				ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
				for (ConsumerRecord<String, String> cr : consumerRecords) {
					String topic = cr.topic();
					int partition = cr.partition();
					String key = cr.key();
					String value = cr.value();
					long offset = cr.offset();
					System.out.printf(
						"topic:%s\tpartition:%d\toffset:%d\tkey:%s\tvalue:%s\r\n",
						topic, partition, offset, key, value);
				}
			}
			// consumer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
