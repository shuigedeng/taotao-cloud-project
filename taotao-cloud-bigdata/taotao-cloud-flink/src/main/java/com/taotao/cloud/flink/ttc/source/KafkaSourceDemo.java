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

package com.taotao.cloud.flink.ttc.source;

import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class KafkaSourceDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // TODO 从Kafka读： 新Source架构
        KafkaSource<String> kafkaSource =
                KafkaSource.<String>builder()
                        .setBootstrapServers(
                                "hadoop102:9092,hadoop103:9092,hadoop104:9092") // 指定kafka节点的地址和端口
                        .setGroupId("atguigu") // 指定消费者组的id
                        .setTopics("topic_1") // 指定消费的 Topic
                        .setValueOnlyDeserializer(new SimpleStringSchema()) // 指定 反序列化器，这个是反序列化value
                        .setStartingOffsets(OffsetsInitializer.latest()) // flink消费kafka的策略
                        .build();

        env
                //                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(),
                // "kafkasource")
                .fromSource(
                        kafkaSource,
                        WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                        "kafkasource")
                .print();

        env.execute();
    }
}
/**
 *   kafka消费者的参数：
 *      auto.reset.offsets
 *          earliest: 如果有offset，从offset继续消费; 如果没有offset，从 最早 消费
 *          latest  : 如果有offset，从offset继续消费; 如果没有offset，从 最新 消费
 *
 *   flink的kafkasource，offset消费策略：OffsetsInitializer，默认是 earliest
 *          earliest: 一定从 最早 消费
 *          latest  : 一定从 最新 消费
 *
 *
 *
 */
