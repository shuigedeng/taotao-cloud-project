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

import java.util.Collections;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * TransformTest4_MultipleStreams
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class TransformTest4_MultipleStreams {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从文件读取数据
        DataStream<String> inputStream =
                env.readTextFile(
                        "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // 转换成SensorReading
        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        // 1. 分流，按照温度值30度为界分为两条流
        SplitStream<SensorReading> splitStream =
                dataStream.split(
                        new OutputSelector<SensorReading>() {
                            @Override
                            public Iterable<String> select( SensorReading value ) {
                                return ( value.getTemperature() > 30 )
                                        ? Collections.singletonList("high")
                                        : Collections.singletonList("low");
                            }
                        });

        DataStream<SensorReading> highTempStream = splitStream.select("high");
        DataStream<SensorReading> lowTempStream = splitStream.select("low");
        DataStream<SensorReading> allTempStream = splitStream.select("high", "low");

        highTempStream.print("high");
        lowTempStream.print("low");
        allTempStream.print("all");

        // 2. 合流 connect，将高温流转换成二元组类型，与低温流连接合并之后，输出状态信息
        DataStream<Tuple2<String, Double>> warningStream =
                highTempStream.map(
                        new MapFunction<SensorReading, Tuple2<String, Double>>() {
                            @Override
                            public Tuple2<String, Double> map( SensorReading value )
                                    throws Exception {
                                return new Tuple2<>(value.getId(), value.getTemperature());
                            }
                        });

        ConnectedStreams<Tuple2<String, Double>, SensorReading> connectedStreams =
                warningStream.connect(lowTempStream);

        DataStream<Object> resultStream =
                connectedStreams.map(
                        new CoMapFunction<Tuple2<String, Double>, SensorReading, Object>() {
                            @Override
                            public Object map1( Tuple2<String, Double> value ) throws Exception {
                                return new Tuple3<>(value.f0, value.f1, "high temp warning");
                            }

                            @Override
                            public Object map2( SensorReading value ) throws Exception {
                                return new Tuple2<>(value.getId(), "normal");
                            }
                        });

        resultStream.print();

        // 3. union联合多条流
        //        warningStream.union(lowTempStream);
        highTempStream.union(lowTempStream, allTempStream);

        env.execute();
    }
}
