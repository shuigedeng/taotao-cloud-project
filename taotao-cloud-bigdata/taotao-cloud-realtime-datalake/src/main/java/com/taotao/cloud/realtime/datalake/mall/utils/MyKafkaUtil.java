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

package com.taotao.cloud.realtime.datalake.mall.utils;

import java.util.Properties;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 *
 * Date: 2021/1/30
 * Desc: 操作Kafka的工具类
 */
public class MyKafkaUtil {
    private static String KAFKA_SERVER = "hadoop202:9092,hadoop203:9092,hadoop204:9092";
    private static String DEFAULT_TOPIC = "DEFAULT_DATA";

    // 获取FlinkKafkaConsumer
    public static FlinkKafkaConsumer<String> getKafkaSource(String topic, String groupId) {
        // Kafka连接的一些属性配置
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        return new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);
    }

    // 封装FlinkKafkaProducer
    public static FlinkKafkaProducer<String> getKafkaSink(String topic) {
        return new FlinkKafkaProducer<String>(KAFKA_SERVER, topic, new SimpleStringSchema());
    }

    public static <T> FlinkKafkaProducer<T> getKafkaSinkBySchema(
            KafkaSerializationSchema<T> kafkaSerializationSchema) {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        // 设置生产数据的超时时间
        props.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 15 * 60 * 1000 + "");
        return new FlinkKafkaProducer<T>(
                DEFAULT_TOPIC,
                kafkaSerializationSchema,
                props,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
    }

    // 拼接Kafka相关属性到DDL
    public static String getKafkaDDL(String topic, String groupId) {
        String ddl =
                "'connector' = 'kafka', "
                        + " 'topic' = '"
                        + topic
                        + "',"
                        + " 'properties.bootstrap.servers' = '"
                        + KAFKA_SERVER
                        + "', "
                        + " 'properties.group.id' = '"
                        + groupId
                        + "', "
                        + "  'format' = 'json', "
                        + "  'scan.startup.mode' = 'latest-offset'  ";
        return ddl;
    }
}
