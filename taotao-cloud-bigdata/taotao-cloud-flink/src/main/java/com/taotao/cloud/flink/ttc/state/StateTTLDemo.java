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

package com.taotao.cloud.flink.ttc.state;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
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
public class StateTTLDemo {
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

        sensorDS.keyBy(r -> r.getId())
                .process(
                        new KeyedProcessFunction<String, WaterSensor, String>() {

                            ValueState<Integer> lastVcState;

                            @Override
                            public void open(OpenContext openContext) throws Exception {
                                super.open(openContext);

                                // TODO 1.创建 StateTtlConfig
                                StateTtlConfig stateTtlConfig =
                                        StateTtlConfig.newBuilder(Duration.ofSeconds(5)) // 过期时间5s
                                                //
                                                // .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite) // 状态 创建和写入（更新） 更新 过期时间
                                                .setUpdateType(
                                                        StateTtlConfig.UpdateType
                                                                .OnReadAndWrite) // 状态 读取、创建和写入（更新）
                                                // 更新 过期时间
                                                .setStateVisibility(
                                                        StateTtlConfig.StateVisibility
                                                                .NeverReturnExpired) // 不返回过期的状态值
                                                .build();

                                // TODO 2.状态描述器 启用 TTL
                                ValueStateDescriptor<Integer> stateDescriptor =
                                        new ValueStateDescriptor<>("lastVcState", Types.INT);
                                stateDescriptor.enableTimeToLive(stateTtlConfig);

                                this.lastVcState = getRuntimeContext().getState(stateDescriptor);
                            }

                            @Override
                            public void processElement(
                                    WaterSensor value, Context ctx, Collector<String> out)
                                    throws Exception {
                                // 先获取状态值，打印 ==》 读取状态
                                Integer lastVc = lastVcState.value();
                                out.collect("key=" + value.getId() + ",状态值=" + lastVc);

                                // 如果水位大于10，更新状态值 ===》 写入状态
                                if (value.getVc() > 10) {
                                    lastVcState.update(value.getVc());
                                }
                            }
                        })
                .print();

        env.execute();
    }
}
