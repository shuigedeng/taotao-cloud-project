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

package com.taotao.cloud.flink.doe.sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/30
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class sink03Kafka {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);
        // 获取数据源
        DataStreamSource<String> dataStreamSource = see.socketTextStream("doe01", 8899);
        // 处理数据
        SingleOutputStreamOperator<String> dataStreamSource2 =
                dataStreamSource.map(String::toUpperCase);
        // 将结果输出到kafka中

        KafkaSink<String> kafkaSink =
                KafkaSink.<String>builder()
                        .setBootstrapServers("doe01:9092,doe02:9092,doe03:9092")
                        .setRecordSerializer(
                                KafkaRecordSerializationSchema.builder()
                                        .setTopic("data")
                                        .setValueSerializationSchema(new SimpleStringSchema())
                                        .build())
                        /**
                         *保证输出数据安全性策略  精确输出一次  至少输出一次
                         * 至少输出一次 , 不用使用事务控制数据输出
                         * Exactly once 保证
                         *    使用kafka内部的事务 和 两阶段提交保证
                         *    1) 开启事务
                         *    2)  事务超时参数   < 15分钟
                         */
                        .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                        // 设置事务ID
                        .setTransactionalIdPrefix("doe-")
                        .setProperty("transaction.timeout.ms", "600000 ")
                        .build();

        dataStreamSource.sinkTo(kafkaSink);
        see.execute("sink to kafka");
    }
}
