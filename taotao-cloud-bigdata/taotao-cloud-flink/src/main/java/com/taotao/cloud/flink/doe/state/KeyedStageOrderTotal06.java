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

package com.taotao.cloud.flink.doe.state;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @since: 2024/1/5
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 自己模拟状态   不具备容错
 */
public class KeyedStageOrderTotal06 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(2);

        // 重启策略
        see.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(10)));
        // checkpoint开启
        see.enableCheckpointing(5000);

        DataStreamSource<String> data = see.socketTextStream("doe01", 8899);
        // city,category,money
        SingleOutputStreamOperator<Tuple3<String, String, Double>> cityCategoryMoney =
                data.map(
                        new MapFunction<String, Tuple3<String, String, Double>>() {
                            @Override
                            public Tuple3<String, String, Double> map(String value)
                                    throws Exception {
                                String[] split = value.split(",");
                                return Tuple3.of(split[0], split[1], Double.parseDouble(split[2]));
                            }
                        });
        // 分组
        KeyedStream<Tuple3<String, String, Double>, String> keyed =
                cityCategoryMoney.keyBy(
                        new KeySelector<Tuple3<String, String, Double>, String>() {
                            @Override
                            public String getKey(Tuple3<String, String, Double> value)
                                    throws Exception {
                                return value.f0;
                            }
                        });
        // 每个城市的订单总额
        keyed.process(
                        new ProcessFunction<
                                Tuple3<String, String, Double>,
                                Tuple4<String, String, Double, Integer>>() {

                            AggregatingState<
                                            Tuple2<String, Double>, Tuple3<String, Double, Integer>>
                                    aggregatingState;

                            @Override
                            public void processElement(
                                    Tuple3<String, String, Double> value,
                                    ProcessFunction<
                                                            Tuple3<String, String, Double>,
                                                            Tuple4<String, String, Double, Integer>>
                                                    .Context
                                            ctx,
                                    Collector<Tuple4<String, String, Double, Integer>> out)
                                    throws Exception {
                                aggregatingState.add(Tuple2.of(value.f1, value.f2));
                                // 返回类别 总额  总个数
                                Tuple3<String, Double, Integer> stringDoubleIntegerTuple3 =
                                        aggregatingState.get();
                                out.collect(
                                        Tuple4.of(
                                                value.f0,
                                                stringDoubleIntegerTuple3.f0,
                                                stringDoubleIntegerTuple3.f1,
                                                stringDoubleIntegerTuple3.f2));
                            }

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                // 输入:类别,金额   缓存  类别,总额 ,个数   输出:  类别,总额 ,个数
                                /**
                                 * 参数1  名字
                                 * 参数2 agg函数
                                 * 参数3  缓存数据类型
                                 * 处理逻辑的核心是 :  输入类别,金额   缓存当前类别,当前城市的总额,当前城市的订单总个数
                                 *
                                 */
                                AggregatingStateDescriptor<
                                                Tuple2<String, Double>,
                                                Tuple3<String, Double, Integer>,
                                                Tuple3<String, Double, Integer>>
                                        agg =
                                                new AggregatingStateDescriptor<
                                                        Tuple2<String, Double>,
                                                        Tuple3<String, Double, Integer>,
                                                        Tuple3<String, Double, Integer>>(
                                                        "agg",
                                                        new AggregateFunction<
                                                                Tuple2<String, Double>,
                                                                Tuple3<String, Double, Integer>,
                                                                Tuple3<String, Double, Integer>>() {
                                                            @Override
                                                            public Tuple3<String, Double, Integer>
                                                                    createAccumulator() {
                                                                return Tuple3.of("", 0D, 0);
                                                            }

                                                            @Override
                                                            public Tuple3<String, Double, Integer>
                                                                    add(
                                                                            Tuple2<String, Double>
                                                                                    value,
                                                                            Tuple3<
                                                                                            String,
                                                                                            Double,
                                                                                            Integer>
                                                                                    accumulator) {
                                                                accumulator.f0 = value.f0;
                                                                accumulator.f1 += value.f1;
                                                                accumulator.f2 += 1;

                                                                return Tuple3.of(
                                                                        accumulator.f0,
                                                                        accumulator.f1,
                                                                        accumulator.f2);
                                                            }

                                                            @Override
                                                            public Tuple3<String, Double, Integer>
                                                                    getResult(
                                                                            Tuple3<
                                                                                            String,
                                                                                            Double,
                                                                                            Integer>
                                                                                    accumulator) {
                                                                return accumulator;
                                                            }

                                                            @Override
                                                            public Tuple3<String, Double, Integer>
                                                                    merge(
                                                                            Tuple3<
                                                                                            String,
                                                                                            Double,
                                                                                            Integer>
                                                                                    a,
                                                                            Tuple3<
                                                                                            String,
                                                                                            Double,
                                                                                            Integer>
                                                                                    b) {
                                                                return null;
                                                            }
                                                        },
                                                        TypeInformation.of(
                                                                new TypeHint<
                                                                        Tuple3<
                                                                                String,
                                                                                Double,
                                                                                Integer>>() {}));

                                aggregatingState = getRuntimeContext().getAggregatingState(agg);
                            }

                            @Override
                            public void close() throws Exception {
                                super.close();
                            }
                        })
                .print();

        see.execute();
    }
}
