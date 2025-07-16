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

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WatermarkMonoDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        // TODO 1.定义Watermark策略
        WatermarkStrategy<WaterSensor> watermarkStrategy =
                WatermarkStrategy
                        // 1.1 指定watermark生成：升序的watermark，没有等待时间
                        .<WaterSensor>forMonotonousTimestamps()
                        // 1.2 指定 时间戳分配器，从数据中提取
                        .withTimestampAssigner(
                                new SerializableTimestampAssigner<WaterSensor>() {
                                    @Override
                                    public long extractTimestamp(
                                            WaterSensor element, long recordTimestamp) {
                                        // 返回的时间戳，要 毫秒
                                        System.out.println(
                                                "数据=" + element + ",recordTs=" + recordTimestamp);
                                        return element.getTs() * 1000L;
                                    }
                                });

        // TODO 2. 指定 watermark策略
        SingleOutputStreamOperator<WaterSensor> sensorDSwithWatermark =
                sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);

        sensorDSwithWatermark
                .keyBy(sensor -> sensor.getId())
                // TODO 3.使用 事件时间语义 的窗口
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(
                        new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {

                            @Override
                            public void process(
                                    String s,
                                    Context context,
                                    Iterable<WaterSensor> elements,
                                    Collector<String> out)
                                    throws Exception {
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
                        })
                .print();

        env.execute();
    }
}
