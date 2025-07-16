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

package com.taotao.cloud.flink.ttc.sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SinkKafka {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 如果是精准一次，必须开启checkpoint（后续章节介绍）
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        SingleOutputStreamOperator<String> sensorDS = env.socketTextStream("hadoop102", 7777);

        /**
         * Kafka Sink:
         * TODO 注意：如果要使用 精准一次 写入Kafka，需要满足以下条件，缺一不可
         * 1、开启checkpoint（后续介绍）
         * 2、设置事务前缀
         * 3、设置事务超时时间：   checkpoint间隔 <  事务超时时间  < max的15分钟
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
                        // 写到kafka的一致性级别： 精准一次、至少一次
                        .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                        // 如果是精准一次，必须设置 事务的前缀
                        .setTransactionalIdPrefix("atguigu-")
                        // 如果是精准一次，必须设置 事务超时时间: 大于checkpoint间隔，小于 max 15分钟
                        .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
                        .build();

        sensorDS.sinkTo(kafkaSink);

        env.execute();
    }
}
