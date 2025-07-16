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
import org.apache.flink.api.common.functions.ReduceFunction;
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
public class WindowReduceDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, TimeWindow> sensorWS =
                sensorKS.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));

        // 2. 窗口函数： 增量聚合 Reduce
        /**
         * 窗口的reduce：
         * 1、相同key的第一条数据来的时候，不会调用reduce方法
         * 2、增量聚合： 来一条数据，就会计算一次，但是不会输出
         * 3、在窗口触发的时候，才会输出窗口的最终计算结果
         */
        SingleOutputStreamOperator<WaterSensor> reduce =
                sensorWS.reduce(
                        new ReduceFunction<WaterSensor>() {
                            @Override
                            public WaterSensor reduce(WaterSensor value1, WaterSensor value2)
                                    throws Exception {
                                System.out.println(
                                        "调用reduce方法，value1=" + value1 + ",value2=" + value2);
                                return new WaterSensor(
                                        value1.getId(),
                                        value2.getTs(),
                                        value1.getVc() + value2.getVc());
                            }
                        });

        reduce.print();

        env.execute();
    }
}
