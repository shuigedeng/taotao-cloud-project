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
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
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
public class KeyedStageTTL {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(2);

        // 重启策略
        see.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Duration.ofSeconds(10)));
        // checkpoint开启
        see.enableCheckpointing(5000);
        see.setStateBackend(new EmbeddedRocksDBStateBackend());

        /**
         * 设置状态的过期时间
         */
        StateTtlConfig config =
                StateTtlConfig.newBuilder(Duration.ofSeconds(5))
                        // 状态数据有读写操作  就会更新过期时间
                        .updateTtlOnReadAndWrite() // 读写
                        // .updateTtlOnCreateAndWrite() //  写
                        // .setTtl()
                        .cleanupFullSnapshot()
                        // .cleanupIncrementally(3 , true)
                        // .neverReturnExpired()
                        .returnExpiredIfNotCleanedUp()
                        // .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                        .useProcessingTime() // 仅使用处理实现
                        .build();

        DataStreamSource<String> data = see.socketTextStream("doe01", 8899);
        // city,money
        SingleOutputStreamOperator<Tuple2<String, Double>> cityMoney =
                data.map(
                        new MapFunction<String, Tuple2<String, Double>>() {
                            @Override
                            public Tuple2<String, Double> map(String value) throws Exception {
                                String[] split = value.split(",");
                                return Tuple2.of(split[0], Double.parseDouble(split[1]));
                            }
                        });
        // 分组
        KeyedStream<Tuple2<String, Double>, String> keyed = cityMoney.keyBy(tp -> tp.f0);
        // 调用算子的时候   使用状态进行计算

        keyed.map(
                        new RichMapFunction<Tuple2<String, Double>, Tuple2<String, Double>>() {
                            ValueState<Double> state;

                            @Override
                            public void open(OpenContext openContext) throws Exception {
                                ValueStateDescriptor<Double> sumMoney =
                                        new ValueStateDescriptor<>(
                                                "sumMoney", TypeInformation.of(Double.class));
                                sumMoney.enableTimeToLive(config);

                                // 创建  加载状态
                                state = getRuntimeContext().getState(sumMoney);
                            }

                            //  处理数据
                            @Override
                            public Tuple2<String, Double> map(Tuple2<String, Double> value)
                                    throws Exception {
                                if (value.f0.equals("dg")) {
                                    throw new RuntimeException();
                                }

                                Double value1 = state.value();
                                // 每来一条数据
                                // 更新当前key的状态值 ,.总金额
                                if (value1 != null) {
                                    state.update(value1 + value.f1);
                                } else {
                                    state.update(value.f1);
                                }

                                return Tuple2.of(value.f0, state.value());
                            }
                        })
                .print();

        see.execute();
    }
}
