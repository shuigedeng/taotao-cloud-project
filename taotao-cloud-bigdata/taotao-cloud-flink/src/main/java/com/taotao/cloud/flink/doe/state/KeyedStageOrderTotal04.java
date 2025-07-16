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

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2024/1/5
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 自己模拟状态   不具备容错
 */
public class KeyedStageOrderTotal04 {
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
        /// 城市,类别,金额     --> 城市,类别,总金额
        keyed.map(
                        new RichMapFunction<
                                Tuple3<String, String, Double>, Tuple3<String, String, Double>>() {
                            MapState<String, Double> mapState; // 类别,总额

                            @Override
                            public void open(Configuration parameters) throws Exception {

                                MapStateDescriptor<String, Double> cm =
                                        new MapStateDescriptor<>(
                                                "cm",
                                                TypeInformation.of(String.class),
                                                TypeInformation.of(Double.class));
                                mapState = getRuntimeContext().getMapState(cm);
                            }

                            @Override
                            public Tuple3<String, String, Double> map(
                                    Tuple3<String, String, Double> value)
                                    throws Exception { // 北京 ,类别 , 金额

                                if (value.f1.equals("X")) {
                                    throw new RuntimeException();
                                }
                                Double aDouble = mapState.get(value.f1);
                                if (aDouble != null) {
                                    aDouble += value.f2;
                                    mapState.put(value.f1, aDouble);
                                } else {
                                    mapState.put(value.f1, value.f2);
                                }
                                Double totalMoney = mapState.get(value.f1);

                                return Tuple3.of(value.f0, value.f1, totalMoney);
                            }
                        })
                .print();

        see.execute();
    }
}
