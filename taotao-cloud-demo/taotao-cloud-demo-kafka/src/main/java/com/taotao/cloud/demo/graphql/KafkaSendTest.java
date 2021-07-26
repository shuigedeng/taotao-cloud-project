/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.demo.graphql;


import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * KafkaSendTest
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/07/26 11:48
 */
public class KafkaSendTest {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "172.16.3.240:9092");
		props.put("acks", "0");
		props.put("retries", 1);
//		props.put("batch.size", 16384);
//		props.put("linger.ms", 10);
//		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 100; i++) {
			System.out.println(i);
			producer.send(new ProducerRecord<String, String>("kafka1",
				Integer.toString(i), Integer.toString(i)), new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata,
					Exception exception) {
					System.out.println("------");
				}
			});
		}

		producer.close();
	}
}
