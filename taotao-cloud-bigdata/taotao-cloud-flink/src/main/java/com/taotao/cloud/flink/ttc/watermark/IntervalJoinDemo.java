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
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;

import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class IntervalJoinDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> ds1 =
                env.fromElements(
                                Tuple2.of("a", 1),
                                Tuple2.of("a", 2),
                                Tuple2.of("b", 3),
                                Tuple2.of("c", 4))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Tuple2<String, Integer>>forMonotonousTimestamps()
                                        .withTimestampAssigner((value, ts) -> value.f1 * 1000L));

        SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> ds2 =
                env.fromElements(
                                Tuple3.of("a", 1, 1),
                                Tuple3.of("a", 11, 1),
                                Tuple3.of("b", 2, 1),
                                Tuple3.of("b", 12, 1),
                                Tuple3.of("c", 14, 1),
                                Tuple3.of("d", 15, 1))
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy
                                        .<Tuple3<String, Integer, Integer>>forMonotonousTimestamps()
                                        .withTimestampAssigner((value, ts) -> value.f1 * 1000L));

        // TODO interval join
        // 1. 分别做keyby，key其实就是关联条件
        KeyedStream<Tuple2<String, Integer>, String> ks1 = ds1.keyBy(r1 -> r1.f0);
        KeyedStream<Tuple3<String, Integer, Integer>, String> ks2 = ds2.keyBy(r2 -> r2.f0);

        // 2. 调用 interval join
        ks1.intervalJoin(ks2)
                .between(Duration.ofSeconds(-2), Duration.ofSeconds(2))
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
                        })
                .print();

        env.execute();
    }
}
