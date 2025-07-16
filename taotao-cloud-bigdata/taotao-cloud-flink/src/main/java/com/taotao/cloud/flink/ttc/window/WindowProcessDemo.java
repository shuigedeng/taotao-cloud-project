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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WindowProcessDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, TimeWindow> sensorWS =
                sensorKS.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));

        // 老写法
        //        sensorWS
        //                .apply(
        //                        new WindowFunction<WaterSensor, String, String, TimeWindow>() {
        //                            /**
        //                             *
        //                             * @param s  分组的key
        //                             * @param window 窗口对象
        //                             * @param input 存的数据
        //                             * @param out   采集器
        //                             * @throws Exception
        //                             */
        //                            @Override
        //                            public void apply(String s, TimeWindow window,
        // Iterable<WaterSensor> input, Collector<String> out) throws Exception {
        //
        //                            }
        //                        }
        //                )

        SingleOutputStreamOperator<String> process =
                sensorWS.process(
                        new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {
                            /**
                             * 全窗口函数计算逻辑：  窗口触发时才会调用一次，统一计算窗口的所有数据
                             * @param s   分组的key
                             * @param context  上下文
                             * @param elements 存的数据
                             * @param out      采集器
                             * @throws Exception
                             */
                            @Override
                            public void process(
                                    String s,
                                    Context context,
                                    Iterable<WaterSensor> elements,
                                    Collector<String> out)
                                    throws Exception {
                                // 上下文可以拿到window对象，还有其他东西：侧输出流 等等
                                long startTs = context.window().getStart();
                                long endTs = context.window().getEnd();
                                String windowStart =
                                        DateFormatUtils.format(startTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                String windowEnd =
                                        DateFormatUtils.format(endTs, "yyyy-MM-dd HH:mm:ss.SSS");

                                long count = elements.spliterator().estimateSize();

                                out.collect(
                                        "key="
                                                + s
                                                + "的窗口["
                                                + windowStart
                                                + ","
                                                + windowEnd
                                                + ")包含"
                                                + count
                                                + "条数据===>"
                                                + elements.toString());
                            }
                        });

        process.print();

        env.execute();
    }
}
