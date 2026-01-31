/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.realtime.warehouse.datageneration.userlog_code;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.taotao.cloud.realtime.warehouse.datageneration.userlog_code.generator.UserLogGenerator;
import com.taotao.cloud.realtime.warehouse.datageneration.userlog_code.model.UserLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * UserLogCommandRunner
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Configuration
@Order(2)
public class UserLogCommandRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserLogCommandRunner.class);
    private static final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic}")
    private String topic;

    @Value("${generator.interval}")
    private long interval;

    @Autowired
    @Qualifier(value = "asyncThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run( String... args ) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("max.block.ms", "3000"); // 设置连接超时时间为3秒

        boolean useKafka = true;
        KafkaProducer<String, String> producer = null;

        try {
            producer = new KafkaProducer<>(props);
            // 测试Kafka连接
            producer.partitionsFor(topic);
            logger.info("Successfully connected to Kafka at {}", bootstrapServers);
        } catch (Exception e) {
            useKafka = false;
            logger.error(
                    "Failed to connect to Kafka: {}. Will print logs locally.", e.getMessage());
        }

        KafkaProducer<String, String> finalProducer = producer;
        boolean finalUseKafka = useKafka;
        threadPoolTaskExecutor.submit(() -> {
            try {

                while (true) {
                    UserLog log = UserLogGenerator.generateLog();
                    String jsonLog = processNestedJson(log);

                    if (finalUseKafka && finalProducer != null) {
                        try {
                            finalProducer.send(
                                            new ProducerRecord<>(topic, jsonLog),
                                            ( metadata, exception ) -> {
                                                if (exception != null) {
                                                    logger.error(
                                                            "Error sending message to Kafka",
                                                            exception);
                                                    System.out.println(
                                                            "Generated log (failed to send to Kafka): "
                                                                    + jsonLog);
                                                } else {
                                                    logger.info(
                                                            "Message sent to partition {} with offset {}",
                                                            metadata.partition(),
                                                            metadata.offset());
                                                }
                                            })
                                    .get(); // 使用get()来确保消息发送成功
                        } catch (InterruptedException | ExecutionException e) {
                            logger.error("Failed to send message to Kafka", e);
                            System.out.println("Generated log (failed to send to Kafka): " + jsonLog);
                        }
                    } else {
                        // 本地打印日志
                        System.out.println("Generated log (local print mode): " + jsonLog);
                    }

                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                logger.error("Error in log generation", e);
            } finally {
                if (finalProducer != null) {
                    finalProducer.close();
                }
            }
        });

    }

    /**
     * 处理嵌套的JSON字符串，避免转义
     */
    private String processNestedJson( UserLog log ) throws Exception {
        // 先将对象转换为JSON节点
        JsonNode rootNode = jsonMapper.valueToTree(log);

        // 处理actions字段
        if (rootNode.has("actions") && !rootNode.get("actions").isNull()) {
            String actionsStr = rootNode.get("actions").asString();
            if (actionsStr != null && !actionsStr.isEmpty() && !actionsStr.equals("[]")) {
                // 解析actions字符串为JSON数组
                JsonNode actionsNode = jsonMapper.readTree(actionsStr);
                ( (ObjectNode) rootNode ).set("actions", actionsNode);
            }
        }

        // 处理displays字段
        if (rootNode.has("displays") && !rootNode.get("displays").isNull()) {
            String displaysStr = rootNode.get("displays").asString();
            if (displaysStr != null && !displaysStr.isEmpty() && !displaysStr.equals("[]")) {
                // 解析displays字符串为JSON数组
                JsonNode displaysNode = jsonMapper.readTree(displaysStr);
                ( (ObjectNode) rootNode ).set("displays", displaysNode);
            }
        }

        // 将处理后的JSON节点转换回字符串
        return jsonMapper.writeValueAsString(rootNode);
    }
}
