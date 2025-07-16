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

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WindowJoinDemo {
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

        // TODO window join
        // 1. 落在同一个时间窗口范围内才能匹配
        // 2. 根据keyby的key，来进行匹配关联
        // 3. 只能拿到匹配上的数据，类似有固定时间范围的inner join
        DataStream<String> join =
                ds1.join(ds2)
                        .where(r1 -> r1.f0) // ds1的keyby
                        .equalTo(r2 -> r2.f0) // ds2的keyby
                        .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                        .apply(
                                new JoinFunction<
                                        Tuple2<String, Integer>,
                                        Tuple3<String, Integer, Integer>,
                                        String>() {
                                    /**
                                     * 关联上的数据，调用join方法
                                     * @param first  ds1的数据
                                     * @param second ds2的数据
                                     * @return
                                     * @throws Exception
                                     */
                                    @Override
                                    public String join(
                                            Tuple2<String, Integer> first,
                                            Tuple3<String, Integer, Integer> second)
                                            throws Exception {
                                        return first + "<----->" + second;
                                    }
                                });

        join.print();

        env.execute();
    }
}
