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

import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SinkKafkaWithKey {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);
        env.setRestartStrategy(RestartStrategies.noRestart());

        SingleOutputStreamOperator<String> sensorDS = env.socketTextStream("hadoop102", 7777);

        /**
         * 如果要指定写入kafka的key
         * 可以自定义序列器：
         * 1、实现 一个接口，重写 序列化 方法
         * 2、指定key，转成 字节数组
         * 3、指定value，转成 字节数组
         * 4、返回一个 ProducerRecord对象，把key、value放进去
         *
         */
        KafkaSink<String> kafkaSink =
                KafkaSink.<String>builder()
                        .setBootstrapServers("hadoop102:9092,hadoop103:9092,hadoop104:9092")
                        .setRecordSerializer(
                                new KafkaRecordSerializationSchema<String>() {

                                    @Nullable
                                    @Override
                                    public ProducerRecord<byte[], byte[]> serialize(
                                            String element,
                                            KafkaSinkContext context,
                                            Long timestamp) {
                                        String[] datas = element.split(",");
                                        byte[] key = datas[0].getBytes(StandardCharsets.UTF_8);
                                        byte[] value = element.getBytes(StandardCharsets.UTF_8);
                                        return new ProducerRecord<>("ws", key, value);
                                    }
                                })
                        .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                        .setTransactionalIdPrefix("atguigu-")
                        .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10 * 60 * 1000 + "")
                        .build();

        sensorDS.sinkTo(kafkaSink);

        env.execute();
    }
}
