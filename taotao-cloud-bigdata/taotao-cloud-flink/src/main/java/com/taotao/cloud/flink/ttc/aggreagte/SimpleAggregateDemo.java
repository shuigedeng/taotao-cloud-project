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

package com.taotao.cloud.flink.ttc.aggreagte;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SimpleAggregateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDS =
                env.fromElements(
                        new WaterSensor("s1", 1L, 1),
                        new WaterSensor("s1", 11L, 11),
                        new WaterSensor("s2", 2L, 2),
                        new WaterSensor("s3", 3L, 3));

        KeyedStream<WaterSensor, String> sensorKS =
                sensorDS.keyBy(
                        new KeySelector<WaterSensor, String>() {
                            @Override
                            public String getKey(WaterSensor value) throws Exception {
                                return value.getId();
                            }
                        });

        /**
         * TODO 简单聚合算子
         *  1、 keyby之后才能调用
         *  2、 分组内的聚合：对同一个key的数据进行聚合
         */
        // 传位置索引的，适用于 Tuple类型，POJO不行
        //        SingleOutputStreamOperator<WaterSensor> result = sensorKS.sum(2);
        //        SingleOutputStreamOperator<WaterSensor> result = sensorKS.sum("vc");

        /**
         *   max\maxby的区别： 同min
         *       max：只会取比较字段的最大值，非比较字段保留第一次的值
         *       maxby：取比较字段的最大值，同时非比较字段 取 最大值这条数据的值
         */
        //        SingleOutputStreamOperator<WaterSensor> result = sensorKS.max("vc");
        //        SingleOutputStreamOperator<WaterSensor> result = sensorKS.min("vc");
        SingleOutputStreamOperator<WaterSensor> result = sensorKS.maxBy("vc");
        //        SingleOutputStreamOperator<WaterSensor> result = sensorKS.minby("vc");

        result.print();

        env.execute();
    }
}
