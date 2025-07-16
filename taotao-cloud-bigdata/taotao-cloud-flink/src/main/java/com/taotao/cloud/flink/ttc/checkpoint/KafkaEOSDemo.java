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
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class KafkaEOSDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 代码中用到hdfs，需要导入hadoop依赖、指定访问hdfs的用户名
        System.setProperty("HADOOP_USER_NAME", "atguigu");

        // TODO 1、启用检查点,设置为精准一次
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointStorage("hdfs://hadoop102:8020/chk");
        checkpointConfig.setExternalizedCheckpointCleanup(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // TODO 2.读取kafka
        KafkaSource<String> kafkaSource =
                KafkaSource.<String>builder()
                        .setBootstrapServers("hadoop102:9092,hadoop103:9092,hadoop104:9092")
                        .setGroupId("atguigu")
                        .setTopics("topic_1")
                        .setValueOnlyDeserializer(new SimpleStringSchema())
                        .setStartingOffsets(OffsetsInitializer.latest())
                        .build();

        DataStreamSource<String> kafkasource =
                env.fromSource(
                        kafkaSource,
                        WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                        "kafkasource");

        /**
         * TODO 3.写出到Kafka
         * 精准一次 写入Kafka，需要满足以下条件，缺一不可
         * 1、开启checkpoint
         * 2、sink设置保证级别为 精准一次
         * 3、sink设置事务前缀
         * 4、sink设置事务超时时间： checkpoint间隔 <  事务超时时间  < max的15分钟
         */
        KafkaSink<String> kafkaSink =
                KafkaSink.<String>builder()
                        // 指定 kafka 的地址和端口
                        .setBootstrapServers("hadoop102:9092,hadoop103:9092,hadoop104:9092")
                        // 指定序列化器：指定Topic名称、具体的序列化
                        .setRecordSerializer(
                                KafkaRecordSerializationSchema.<String>builder()
                                        .setTopic("ws")
                                        .setValueSerializationSchema(new SimpleStringSchema())
                                        .build())
                        // TODO 3.1 精准一次,开启 2pc
                        .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                        // TODO 3.2 精准一次，必须设置 事务的前缀
                        .setTransactionalIdPrefix("atguigu-")
                        // TODO 3.3 精准一次，必须设置 事务超时时间: 大于checkpoint间隔，小于 max 15分钟
                        .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
                        .build();

        kafkasource.sinkTo(kafkaSink);

        env.execute();
    }
}
