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
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
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
public class StateBackendDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        /**
         * TODO 状态后端
         * 1、负责管理 本地状态
         * 2、 hashmap
         *          存在 TM的 JVM的堆内存，  读写快，缺点是存不了太多（受限与TaskManager的内存）
         *     rocksdb
         *          存在 TM所在节点的rocksdb数据库，存到磁盘中，  写--序列化，读--反序列化
         *          读写相对慢一些，可以存很大的状态
         *
         * 3、配置方式
         *    1）配置文件 默认值  flink-conf.yaml
         *    2）代码中指定
         *    3）提交参数指定
         *    flink run-application -t yarn-application
         *    -p 3
         *    -Dstate.backend.type=rocksdb
         *    -c 全类名
         *    jar包
         */

        // 1. 使用 hashmap状态后端
        HashMapStateBackend hashMapStateBackend = new HashMapStateBackend();
        env.setStateBackend(hashMapStateBackend);
        // 2. 使用 rocksdb状态后端
        EmbeddedRocksDBStateBackend embeddedRocksDBStateBackend = new EmbeddedRocksDBStateBackend();
        env.setStateBackend(embeddedRocksDBStateBackend);

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
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                lastVcState =
                                        getRuntimeContext()
                                                .getState(
                                                        new ValueStateDescriptor<Integer>(
                                                                "lastVcState", Types.INT));
                            }

                            @Override
                            public void processElement(
                                    WaterSensor value, Context ctx, Collector<String> out)
                                    throws Exception {

                                int lastVc = lastVcState.value() == null ? 0 : lastVcState.value();
                                Integer vc = value.getVc();
                                if (Math.abs(vc - lastVc) > 10) {
                                    out.collect(
                                            "传感器="
                                                    + value.getId()
                                                    + "==>当前水位值="
                                                    + vc
                                                    + ",与上一条水位值="
                                                    + lastVc
                                                    + ",相差超过10！！！！");
                                }
                                lastVcState.update(vc);
                            }
                        })
                .print();

        env.execute();
    }
}
