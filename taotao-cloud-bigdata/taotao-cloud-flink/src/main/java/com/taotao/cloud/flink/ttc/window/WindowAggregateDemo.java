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

package com.taotao.cloud.flink.ttc.window;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import com.taotao.cloud.flink.ttc.functions.WaterSensorMapFunction;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WindowAggregateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, TimeWindow> sensorWS =
                sensorKS.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));

        // 2. 窗口函数： 增量聚合 Aggregate
        /**
         * 1、属于本窗口的第一条数据来，创建窗口，创建累加器
         * 2、增量聚合： 来一条计算一条， 调用一次add方法
         * 3、窗口输出时调用一次getresult方法
         * 4、输入、中间累加器、输出 类型可以不一样，非常灵活
         */
        SingleOutputStreamOperator<String> aggregate =
                sensorWS.aggregate(
                        /**
                         * 第一个类型： 输入数据的类型
                         * 第二个类型： 累加器的类型，存储的中间计算结果的类型
                         * 第三个类型： 输出的类型
                         */
                        new AggregateFunction<WaterSensor, Integer, String>() {
                            /**
                             * 创建累加器，初始化累加器
                             * @return
                             */
                            @Override
                            public Integer createAccumulator() {
                                System.out.println("创建累加器");
                                return 0;
                            }

                            /**
                             * 聚合逻辑
                             * @param value
                             * @param accumulator
                             * @return
                             */
                            @Override
                            public Integer add(WaterSensor value, Integer accumulator) {
                                System.out.println("调用add方法,value=" + value);
                                return accumulator + value.getVc();
                            }

                            /**
                             * 获取最终结果，窗口触发时输出
                             * @param accumulator
                             * @return
                             */
                            @Override
                            public String getResult(Integer accumulator) {
                                System.out.println("调用getResult方法");
                                return accumulator.toString();
                            }

                            @Override
                            public Integer merge(Integer a, Integer b) {
                                // 只有会话窗口才会用到
                                System.out.println("调用merge方法");
                                return null;
                            }
                        });

        aggregate.print();

        env.execute();
    }
}
