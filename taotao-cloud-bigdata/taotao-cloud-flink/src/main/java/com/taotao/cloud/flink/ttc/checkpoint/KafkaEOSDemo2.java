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

package com.taotao.cloud.flink.ttc.checkpoint;

import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;

/**
 * TODO flink去消费 被 两阶段提交的 topic，设置隔离级别
 *
 * @author shuigedeng
 * @version 1.0
 */
public class KafkaEOSDemo2 {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 消费 在前面使用两阶段提交写入的Topic
        KafkaSource<String> kafkaSource =
                KafkaSource.<String>builder()
                        .setBootstrapServers("hadoop102:9092,hadoop103:9092,hadoop104:9092")
                        .setGroupId("atguigu")
                        .setTopics("ws")
                        .setValueOnlyDeserializer(new SimpleStringSchema())
                        .setStartingOffsets(OffsetsInitializer.latest())
                        // TODO 作为 下游的消费者，要设置 事务的隔离级别 = 读已提交
                        .setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed")
                        .build();

        env.fromSource(
                        kafkaSource,
                        WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                        "kafkasource")
                .print();

        env.execute();
    }
}
