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

package com.taotao.cloud.flink.ttc.process;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class KeyedProcessTimerDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777)
                        .map(new WaterSensorMapFunction())
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(
                                                Duration.ofSeconds(3))
                                        .withTimestampAssigner(
                                                (element, ts) -> element.getTs() * 1000L));

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(sensor -> sensor.getId());

        // TODO Process:keyed
        SingleOutputStreamOperator<String> process =
                sensorKS.process(
                        new KeyedProcessFunction<String, WaterSensor, String>() {
                            /**
                             * 来一条数据调用一次
                             * @param value
                             * @param ctx
                             * @param out
                             * @throws Exception
                             */
                            @Override
                            public void processElement(
                                    WaterSensor value, Context ctx, Collector<String> out)
                                    throws Exception {
                                // 获取当前数据的key
                                String currentKey = ctx.getCurrentKey();

                                // TODO 1.定时器注册
                                TimerService timerService = ctx.timerService();

                                // 1、事件时间的案例
                                Long currentEventTime = ctx.timestamp(); // 数据中提取出来的事件时间
                                timerService.registerEventTimeTimer(5000L);
                                System.out.println(
                                        "当前key="
                                                + currentKey
                                                + ",当前时间="
                                                + currentEventTime
                                                + ",注册了一个5s的定时器");

                                // 2、处理时间的案例
                                //                        long currentTs =
                                // timerService.currentProcessingTime();
                                //
                                // timerService.registerProcessingTimeTimer(currentTs + 5000L);
                                //                        System.out.println("当前key=" + currentKey +
                                // ",当前时间=" + currentTs + ",注册了一个5s后的定时器");

                                // 3、获取 process的 当前watermark
                                //                        long currentWatermark =
                                // timerService.currentWatermark();
                                //                        System.out.println("当前数据=" + value +
                                // ",当前watermark=" + currentWatermark);

                                // 注册定时器： 处理时间、事件时间
                                //
                                // timerService.registerProcessingTimeTimer();
                                //                        timerService.registerEventTimeTimer();
                                // 删除定时器： 处理时间、事件时间
                                //                        timerService.deleteEventTimeTimer();
                                //                        timerService.deleteProcessingTimeTimer();

                                // 获取当前时间进展： 处理时间-当前系统时间，  事件时间-当前watermark
                                //                        long currentTs =
                                // timerService.currentProcessingTime();
                                //                        long wm = timerService.currentWatermark();
                            }

                            /**
                             * TODO 2.时间进展到定时器注册的时间，调用该方法
                             * @param timestamp 当前时间进展，就是定时器被触发时的时间
                             * @param ctx       上下文
                             * @param out       采集器
                             * @throws Exception
                             */
                            @Override
                            public void onTimer(
                                    long timestamp, OnTimerContext ctx, Collector<String> out)
                                    throws Exception {
                                super.onTimer(timestamp, ctx, out);
                                String currentKey = ctx.getCurrentKey();

                                System.out.println(
                                        "key=" + currentKey + "现在时间是" + timestamp + "定时器触发");
                            }
                        });

        process.print();

        env.execute();
    }
}
/**
 * TODO 定时器
 * 1、keyed才有
 * 2、事件时间定时器，通过watermark来触发的
 *    watermark >= 注册的时间
 *    注意： watermark = 当前最大事件时间 - 等待时间 -1ms， 因为 -1ms，所以会推迟一条数据
 *        比如， 5s的定时器，
 *        如果 等待=3s， watermark = 8s - 3s -1ms = 4999ms,不会触发5s的定时器
 *        需要 watermark = 9s -3s -1ms = 5999ms ，才能去触发 5s的定时器
 * 3、在process中获取当前watermark，显示的是上一次的watermark
 *    =》因为process还没接收到这条数据对应生成的新watermark
 */
