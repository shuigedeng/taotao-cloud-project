package com.taotao.cloud.demo.graphql;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
@Slf4j
public class TaotaoCloudDemoKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaotaoCloudDemoKafkaApplication.class, args);
    }

	@KafkaListener(topics = "access")
	public void onMessage(ConsumerRecord<String, String> record) {
		String value = record.value();
		log.info(value);
		if (value.length() % 2 == 0) {
			throw new RuntimeException("模拟业务出错");
		}
	}

}
