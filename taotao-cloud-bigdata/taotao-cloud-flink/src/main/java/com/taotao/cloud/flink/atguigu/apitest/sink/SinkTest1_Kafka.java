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

package com.taotao.cloud.flink.atguigu.apitest.sink;

import java.util.Properties;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * SinkTest1_Kafka
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SinkTest1_Kafka {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //        // 从文件读取数据
        //        DataStream<String> inputStream =
        // env.readTextFile("D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("group.id", "consumer-group");
        properties.setProperty(
                "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(
                "value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("auto.offset.reset", "latest");

        // // 从文件读取数据
        // DataStream<String> inputStream = env.addSource( new
        // FlinkKafkaConsumer011<String>("sensor", new SimpleStringSchema(), properties));
        //
        // // 转换成SensorReading类型
        // DataStream<String> dataStream = inputStream.map(line -> {
        //     String[] fields = line.split(",");
        //     return new SensorReading(fields[0], new Long(fields[1]), new
        // Double(fields[2])).toString();
        // });
        //
        // dataStream.addSink( new FlinkKafkaProducer011<String>("127.0.0.1:9092", "sinktest", new
        // SimpleStringSchema()));
        //
        // env.execute();
    }
}
