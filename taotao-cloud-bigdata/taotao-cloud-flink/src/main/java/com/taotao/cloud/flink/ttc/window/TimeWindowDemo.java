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
public class TimeWindowDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // 1. 窗口分配器
        WindowedStream<WaterSensor, String, TimeWindow> sensorWS =
                sensorKS.window(TumblingProcessingTimeWindows.of(Time.seconds(10))); // 滚动窗口，窗口长度10秒
        //                .window(SlidingProcessingTimeWindows.of(Time.seconds(10),
        // Time.seconds(5)));//滑动窗口，长度10s，步长5s
        //                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5)));//会话窗口，间隔5s
        //                .window(ProcessingTimeSessionWindows.withDynamicGap(
        //                        new SessionWindowTimeGapExtractor<WaterSensor>() {
        //                            @Override
        //                            public long extract(WaterSensor element) {
        //                                // 从数据中提取ts，作为间隔,单位ms
        //                                return element.getTs() * 1000L;
        //                            }
        //                        }
        //                ));// 会话窗口，动态间隔，每条来的数据都会更新 间隔时间

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

/**
 *  触发器、移除器： 现成的几个窗口，都有默认的实现，一般不需要自定义
 *
 *  以 时间类型的 滚动窗口 为例，分析原理：
 * TODO 1、窗口什么时候触发 输出？
 * 时间进展 >= 窗口的最大时间戳（end - 1ms）
 *
 * TODO 2、窗口是怎么划分的？
 * start= 向下取整，取窗口长度的整数倍
 * end = start + 窗口长度
 *
 * 窗口左闭右开 ==》 属于本窗口的 最大时间戳 = end - 1ms
 *
 * TODO 3、窗口的生命周期？
 * 创建： 属于本窗口的第一条数据来的时候，现new的，放入一个singleton单例的集合中
 * 销毁（关窗）： 时间进展 >=  窗口的最大时间戳（end - 1ms） + 允许迟到的时间（默认0）
 *
 *
 *
 * remainder = (timestamp - offset) % windowSize;
 * ·  （13s - 0 ）% 10 = 3
 * （27s - 0 ）% 10 = 7
 * if (remainder < 0) {
 * return timestamp - (remainder + windowSize);
 * } else {
 * return timestamp - remainder;
 * 13 -3 = 10
 * 27 - 7 = 20
 * }
 *
 */
