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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class CountWindowDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, GlobalWindow> sensorWS =
                sensorKS
                        //                .countWindow(5); // 滚动窗口，窗口长度5条数据
                        .countWindow(
                        5, 2); // 滑动窗口，窗口长度5条数据，滑动步长2条数据（每经过一个步长，都有一个窗口触发输出，第一次输出在第2条数据来的时候）

        SingleOutputStreamOperator<String> process =
                sensorWS.process(
                        new ProcessWindowFunction<WaterSensor, String, String, GlobalWindow>() {
                            @Override
                            public void process(
                                    String s,
                                    Context context,
                                    Iterable<WaterSensor> elements,
                                    Collector<String> out)
                                    throws Exception {
                                long maxTs = context.window().maxTimestamp();
                                String maxTime =
                                        DateFormatUtils.format(maxTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                long count = elements.spliterator().estimateSize();

                                out.collect(
                                        "key="
                                                + s
                                                + "的窗口最大时间="
                                                + maxTime
                                                + ",包含"
                                                + count
                                                + "条数据===>"
                                                + elements.toString());
                            }
                        });

        process.print();

        env.execute();
    }
}
