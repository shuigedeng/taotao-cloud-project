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

package com.taotao.cloud.flink.atguigu.apitest.window;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.util.OutputTag;

/**
 * WindowTest3_EventTimeWindow
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class WindowTest3_EventTimeWindow {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //        env.setParallelism(1);
        //         //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(100);

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型，分配时间戳和watermark
        DataStream<SensorReading> dataStream =
                inputStream
                        .map(
                                line -> {
                                    String[] fields = line.split(",");
                                    return new SensorReading(
                                            fields[0], new Long(fields[1]), new Double(fields[2]));
                                })
                        // 升序数据设置事件时间和watermark
                        //                .assignTimestampsAndWatermarks(new
                        // AscendingTimestampExtractor<SensorReading>() {
                        //                    @Override
                        //                    public long extractAscendingTimestamp(SensorReading
                        // element) {
                        //                        return element.getTimestamp() * 1000L;
                        //                    }
                        //                })
                        // 乱序数据设置时间戳和watermark
                        .assignTimestampsAndWatermarks(
                                new BoundedOutOfOrdernessTimestampExtractor<SensorReading>(
                                        Duration.ofSeconds(2)) {
                                    @Override
                                    public long extractTimestamp( SensorReading element ) {
                                        return element.getTimestamp() * 1000L;
                                    }
                                });

        OutputTag<SensorReading> outputTag = new OutputTag<SensorReading>("late") {
        };

        // 基于事件时间的开窗聚合，统计15秒内温度的最小值
        SingleOutputStreamOperator<SensorReading> minTempStream =
                dataStream
                        .keyBy("id")
                        .timeWindow(Duration.ofSeconds(15))
                        .allowedLateness(Time.minutes(1))
                        .sideOutputLateData(outputTag)
                        .minBy("temperature");

        minTempStream.print("minTemp");
        minTempStream.getSideOutput(outputTag).print("late");

        env.execute();
    }
}
