package com.taotao.cloud.data.sync.kafka.qianfeng;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Producer：读取本地文件
 */
public class _09GroupProducerTest {

	public static void main(String[] args) throws Exception {
		// 加载自定义分区
		Properties prop = new Properties();
//        prop.put("partition.class",com.day21._05RandomPartitioner.class);
//        prop.put("partition.class",com.day21._06HashPartitioner.class);
		prop.load(_09GroupProducerTest.class
			.getClassLoader().getResourceAsStream("producer.properties"));
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prop);
		String topic = "hadoop";
		// 读取本地文件
		List<String> lines = getContent();
		ProducerRecord<String, String> record = null;
		for (String line : lines) {
			record = new ProducerRecord<String, String>(topic, line);
			producer.send(record);
		}
		// 关闭 释放资源
		producer.close();
	}


	private static List<String> getContent() {
		List<String> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("data/access.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
