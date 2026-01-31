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

package com.taotao.cloud.flink.atguigu.apitest.transform;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TransformTest2_RollingAggregation
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class TransformTest2_RollingAggregation {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);

        // 从文件读取数据
        DataStream<String> inputStream =
                env.readTextFile(
                        "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // 转换成SensorReading类型
        //        DataStream<SensorReading> dataStream = inputStream.map(new MapFunction<String,
        // SensorReading>() {
        //            @Override
        //            public SensorReading map(String value) throws Exception {
        //                String[] fields = value.split(",");
        //                return new SensorReading(fields[0], new Long(fields[1]), new
        // Double(fields[2]));
        //            }
        //        });

        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        // 分组
        KeyedStream<SensorReading, Tuple> keyedStream = dataStream.keyBy("id");
        KeyedStream<SensorReading, String> keyedStream1 = dataStream.keyBy(data -> data.getId());

        DataStream<Long> dataStream1 = env.fromElements(1L, 34L, 4L, 657L, 23L);
        KeyedStream<Long, Integer> keyedStream2 =
                dataStream1.keyBy(
                        new KeySelector<Long, Integer>() {
                            @Override
                            public Integer getKey( Long value ) throws Exception {
                                return value.intValue() % 2;
                            }
                        });

        //        KeyedStream<SensorReading, String> keyedStream1 =
        // dataStream.keyBy(SensorReading::getId);

        // 滚动聚合，取当前最大的温度值
        DataStream<SensorReading> resultStream = keyedStream.maxBy("temperature");

        resultStream.print("result");

        keyedStream1.print("key1");
        keyedStream2.sum(0).print("key2");
        env.execute();
    }
}
