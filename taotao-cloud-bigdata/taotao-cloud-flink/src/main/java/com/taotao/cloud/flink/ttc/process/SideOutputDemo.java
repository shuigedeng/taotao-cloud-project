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
import java.util.*;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SideOutputDemo {
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

        OutputTag<String> warnTag = new OutputTag<>("warn", Types.STRING);
        SingleOutputStreamOperator<WaterSensor> process =
                sensorDS.keyBy(sensor -> sensor.getId())
                        .process(
                                new KeyedProcessFunction<String, WaterSensor, WaterSensor>() {
                                    @Override
                                    public void processElement(
                                            WaterSensor value,
                                            Context ctx,
                                            Collector<WaterSensor> out)
                                            throws Exception {
                                        // 使用侧输出流告警
                                        if (value.getVc() > 10) {
                                            ctx.output(
                                                    warnTag,
                                                    "当前水位=" + value.getVc() + ",大于阈值10！！！");
                                        }
                                        // 主流正常 发送数据
                                        out.collect(value);
                                    }
                                });

        process.print("主流");
        process.getSideOutput(warnTag).printToErr("warn");

        env.execute();
    }
}
