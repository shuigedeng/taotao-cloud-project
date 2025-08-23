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
import com.taotao.cloud.flink.ttc.functions.WaterSensorMapFunction;
import java.time.Duration;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WatermarkCustomDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 默认周期 200ms
        env.getConfig().setAutoWatermarkInterval(2000);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        WatermarkStrategy<WaterSensor> watermarkStrategy =
                WatermarkStrategy
                        // TODO 指定 自定义的 生成器
                        // 1.自定义的 周期性生成
                        //                .<WaterSensor>forGenerator(ctx -> new
                        // MyPeriodWatermarkGenerator<>(3000L))
                        // 2.自定义的 断点式生成
                        .<WaterSensor>forGenerator(
                                ctx -> new MyPuntuatedWatermarkGenerator<>(3000L))
                        .withTimestampAssigner(
                                (element, recordTimestamp) -> {
                                    return element.getTs() * 1000L;
                                });

        SingleOutputStreamOperator<WaterSensor> sensorDSwithWatermark =
                sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);

        sensorDSwithWatermark
                .keyBy(sensor -> sensor.getId())
                .window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)))
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
