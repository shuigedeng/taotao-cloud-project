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
import org.apache.commons.collections.IteratorUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * WindowTest1_TimeWindow
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class WindowTest1_TimeWindow {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //        // 从文件读取数据
        //        DataStream<String> inputStream =
        // env.readTextFile("D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // socket文本流
        DataStream<String> inputStream = env.socketTextStream("localhost", 7777);

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        // 开窗测试

        // 1. 增量聚合函数
        DataStream<Integer> resultStream =
                dataStream
                        .keyBy("id")
                        //                .countWindow(10, 2);
                        //                .window(EventTimeSessionWindows.withGap(Time.minutes(1)));
                        //
                        // .window(TumblingProcessingTimeWindows.of(Duration.ofSeconds(15)))
                        .timeWindow(Duration.ofSeconds(15))
                        .aggregate(
                                new AggregateFunction<SensorReading, Integer, Integer>() {
                                    @Override
                                    public Integer createAccumulator() {
                                        return 0;
                                    }

                                    @Override
                                    public Integer add( SensorReading value, Integer accumulator ) {
                                        return accumulator + 1;
                                    }

                                    @Override
                                    public Integer getResult( Integer accumulator ) {
                                        return accumulator;
                                    }

                                    @Override
                                    public Integer merge( Integer a, Integer b ) {
                                        return a + b;
                                    }
                                });

        // 2. 全窗口函数
        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> resultStream2 =
                dataStream
                        .keyBy("id")
                        .timeWindow(Duration.ofSeconds(15))
                        //                .process(new ProcessWindowFunction<SensorReading, Object,
                        // Tuple, TimeWindow>() {
                        //                })
                        .apply(
                                new WindowFunction<
                                        SensorReading,
                                        Tuple3<String, Long, Integer>,
                                        Tuple,
                                        TimeWindow>() {
                                    @Override
                                    public void apply(
                                            Tuple tuple,
                                            TimeWindow window,
                                            Iterable<SensorReading> input,
                                            Collector<Tuple3<String, Long, Integer>> out )
                                            throws Exception {
                                        String id = tuple.getField(0);
                                        Long windowEnd = window.getEnd();
                                        Integer count =
                                                IteratorUtils.toList(input.iterator()).size();
                                        out.collect(new Tuple3<>(id, windowEnd, count));
                                    }
                                });

        // 3. 其它可选API
        OutputTag<SensorReading> outputTag = new OutputTag<SensorReading>("late") {
        };

        SingleOutputStreamOperator<SensorReading> sumStream =
                dataStream
                        .keyBy("id")
                        .timeWindow(Duration.ofSeconds(15))
                        //                .trigger()
                        //                .evictor()
                        .allowedLateness(Time.minutes(1))
                        .sideOutputLateData(outputTag)
                        .sum("temperature");

        sumStream.getSideOutput(outputTag).print("late");

        resultStream2.print();

        env.execute();
    }
}
