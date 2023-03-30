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

package com.taotao.cloud.message.biz.austin.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink启动类
 *
 * @author 3y
 */
@Slf4j
public class AustinBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /** 1.获取KafkaConsumer */
        KafkaSource<String> kafkaConsumer =
                MessageQueueUtils.getKafkaConsumer(
                        AustinFlinkConstant.TOPIC_NAME,
                        AustinFlinkConstant.GROUP_ID,
                        AustinFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource =
                env.fromSource(
                        kafkaConsumer,
                        WatermarkStrategy.noWatermarks(),
                        AustinFlinkConstant.SOURCE_NAME);

        /** 2. 数据转换处理 */
        SingleOutputStreamOperator<AnchorInfo> dataStream =
                kafkaSource
                        .flatMap(new AustinFlatMapFunction())
                        .name(AustinFlinkConstant.FUNCTION_NAME);

        /** 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现) */
        dataStream.addSink(new AustinSink()).name(AustinFlinkConstant.SINK_NAME);
        env.execute(AustinFlinkConstant.JOB_NAME);
    }
}
