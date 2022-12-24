package com.taotao.cloud.data.sync.kafka.qianfeng;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 生产者API操作 本案例就是学习如何使用API来完成Kafka数据的生产 首先肯定需要生产者---> 就是程序的入口Producer 数据被生产到哪里？---> Topic 什么样数据？
 * ---> 数据类型
 */
public class _04MyProducerTest {

	public static void main(String[] args) throws Exception {
		// 加载配置信息
		Properties prop = new Properties();
		prop.load(_04MyProducerTest.class
			.getClassLoader().getResourceAsStream("producer.properties"));
		/**
		 * 创建执行入口
		 * 首先我们知道Kafka中的数据都是有三个部分组成 Key，Value，timestamp
		 * Each record consists of a key, a value, and a timestamp.
		 * K就是记录中的Key的类型
		 * V就是记录中的Value的类型
		 */
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
		// 设置我们要发送的Topic
		String topic = "spark";
		for (int i = 0; i < 10; i++) {
			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,
				"111");
			// 通过send发送
        /*
          此send方法并不会立即将数据发送到Kafka集群，而且先发送到缓冲区，该方法便立即返回，
          返回给调用者producer，该方法是一个异步方法
          而缓冲区满了，或者时间到了，就会将send的数据转换为request请求，提交给Kafka集群
         */
			producer.send(record);
		}
		// 释放资源
		producer.close();
	}
}
