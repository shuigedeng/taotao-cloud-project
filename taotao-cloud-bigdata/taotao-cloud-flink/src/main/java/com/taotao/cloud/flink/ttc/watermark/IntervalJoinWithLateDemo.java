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

package com.taotao.cloud.flink.ttc.watermark;

import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;

import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class IntervalJoinWithLateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> ds1 =
                env.socketTextStream("hadoop102", 7777)
                        .map(
                                new MapFunction<String, Tuple2<String, Integer>>() {
                                    @Override
                                    public Tuple2<String, Integer> map(String value)
                                            throws Exception {
                                        String[] datas = value.split(",");
                                        return Tuple2.of(datas[0], Integer.valueOf(datas[1]));
                                    }
                                })
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Tuple2<String, Integer>>forBoundedOutOfOrderness(
                                                Duration.ofSeconds(3))
                                        .withTimestampAssigner((value, ts) -> value.f1 * 1000L));

        SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> ds2 =
                env.socketTextStream("hadoop102", 8888)
                        .map(
                                new MapFunction<String, Tuple3<String, Integer, Integer>>() {
                                    @Override
                                    public Tuple3<String, Integer, Integer> map(String value)
                                            throws Exception {
                                        String[] datas = value.split(",");
                                        return Tuple3.of(
                                                datas[0],
                                                Integer.valueOf(datas[1]),
                                                Integer.valueOf(datas[2]));
                                    }
                                })
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy
                                        .<Tuple3<String, Integer, Integer>>forBoundedOutOfOrderness(
                                                Duration.ofSeconds(3))
                                        .withTimestampAssigner((value, ts) -> value.f1 * 1000L));

        /**
         * TODO Interval join
         * 1、只支持事件时间
         * 2、指定上界、下界的偏移，负号代表时间往前，正号代表时间往后
         * 3、process中，只能处理 join上的数据
         * 4、两条流关联后的watermark，以两条流中最小的为准
         * 5、如果 当前数据的事件时间 < 当前的watermark，就是迟到数据， 主流的process不处理
         *  => between后，可以指定将 左流 或 右流 的迟到数据 放入侧输出流
         */

        // 1. 分别做keyby，key其实就是关联条件
        KeyedStream<Tuple2<String, Integer>, String> ks1 = ds1.keyBy(r1 -> r1.f0);
        KeyedStream<Tuple3<String, Integer, Integer>, String> ks2 = ds2.keyBy(r2 -> r2.f0);

        // 2. 调用 interval join
        OutputTag<Tuple2<String, Integer>> ks1LateTag =
                new OutputTag<>("ks1-late", Types.TUPLE(Types.STRING, Types.INT));
        OutputTag<Tuple3<String, Integer, Integer>> ks2LateTag =
                new OutputTag<>("ks2-late", Types.TUPLE(Types.STRING, Types.INT, Types.INT));
        SingleOutputStreamOperator<String> process =
                ks1.intervalJoin(ks2)
                        .between(Duration.ofSeconds(-2), Duration.ofSeconds(2))
                        .sideOutputLeftLateData(ks1LateTag) // 将 ks1的迟到数据，放入侧输出流
                        .sideOutputRightLateData(ks2LateTag) // 将 ks2的迟到数据，放入侧输出流
                        .process(
                                new ProcessJoinFunction<
                                        Tuple2<String, Integer>,
                                        Tuple3<String, Integer, Integer>,
                                        String>() {
                                    /**
                                     * 两条流的数据匹配上，才会调用这个方法
                                     * @param left  ks1的数据
                                     * @param right ks2的数据
                                     * @param ctx   上下文
                                     * @param out   采集器
                                     * @throws Exception
                                     */
                                    @Override
                                    public void processElement(
                                            Tuple2<String, Integer> left,
                                            Tuple3<String, Integer, Integer> right,
                                            Context ctx,
                                            Collector<String> out)
                                            throws Exception {
                                        // 进入这个方法，是关联上的数据
                                        out.collect(left + "<------>" + right);
                                    }
                                });

        process.print("主流");
        process.getSideOutput(ks1LateTag).printToErr("ks1迟到数据");
        process.getSideOutput(ks2LateTag).printToErr("ks2迟到数据");

        env.execute();
    }
}
