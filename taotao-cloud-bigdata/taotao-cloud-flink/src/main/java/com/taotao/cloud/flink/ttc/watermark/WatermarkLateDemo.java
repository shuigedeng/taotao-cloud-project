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
import java.time.Duration;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WatermarkLateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        WatermarkStrategy<WaterSensor> watermarkStrategy =
                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner(
                                (element, recordTimestamp) -> element.getTs() * 1000L);

        SingleOutputStreamOperator<WaterSensor> sensorDSwithWatermark =
                sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);

        OutputTag<WaterSensor> lateTag =
                new OutputTag<>("late-data", Types.POJO(WaterSensor.class));

        SingleOutputStreamOperator<String> process =
                sensorDSwithWatermark
                        .keyBy(sensor -> sensor.getId())
                        .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                        .allowedLateness(Time.seconds(2)) // 推迟2s关窗
                        .sideOutputLateData(lateTag) // 关窗后的迟到数据，放入侧输出流
                        .process(
                                new ProcessWindowFunction<
                                        WaterSensor, String, String, TimeWindow>() {

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
                                                DateFormatUtils.format(
                                                        startTs, "yyyy-MM-dd HH:mm:ss.SSS");
                                        String windowEnd =
                                                DateFormatUtils.format(
                                                        endTs, "yyyy-MM-dd HH:mm:ss.SSS");

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
        // 从主流获取侧输出流，打印
        process.getSideOutput(lateTag).printToErr("关窗后的迟到数据");

        env.execute();
    }
}
/**
 * 1、乱序与迟到的区别
 *      乱序： 数据的顺序乱了， 时间小的 比 时间大的 晚来
 *      迟到： 数据的时间戳 < 当前的watermark
 * 2、乱序、迟到数据的处理
 * 1） watermark中指定 乱序等待时间
 * 2） 如果开窗，设置窗口允许迟到
 *      =》 推迟关窗时间，在关窗之前，迟到数据来了，还能被窗口计算，来一条迟到数据触发一次计算
 *      =》 关窗后，迟到数据不会被计算
 * 3） 关窗后的迟到数据，放入侧输出流
 *
 *
 * 如果 watermark等待3s，窗口允许迟到2s， 为什么不直接 watermark等待5s 或者 窗口允许迟到5s？
 *  =》 watermark等待时间不会设太大 ===》 影响的计算延迟
 *          如果3s ==》 窗口第一次触发计算和输出，  13s的数据来 。  13-3=10s
 *          如果5s ==》 窗口第一次触发计算和输出，  15s的数据来 。  15-5=10s
 *  =》 窗口允许迟到，是对 大部分迟到数据的 处理， 尽量让结果准确
 *          如果只设置 允许迟到5s， 那么 就会导致 频繁 重新输出
 *
 *  TODO 设置经验
 *  1、watermark等待时间，设置一个不算特别大的，一般是秒级，在 乱序和 延迟 取舍
 *  2、设置一定的窗口允许迟到，只考虑大部分的迟到数据，极端小部分迟到很久的数据，不管
 *  3、极端小部分迟到很久的数据， 放到侧输出流。 获取到之后可以做各种处理
 *
 */
