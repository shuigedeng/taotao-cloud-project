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

package com.taotao.cloud.flink.atguigu.apitest.source;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;

import java.util.Arrays;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * SourceTest1_Collection
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class SourceTest1_Collection {

    public static void main( String[] args ) throws Exception {
        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从集合中读取数据
        DataStream<SensorReading> dataStream =
                env.fromCollection(
                        Arrays.asList(
                                new SensorReading("sensor_1", 1547718199L, 35.8),
                                new SensorReading("sensor_6", 1547718201L, 15.4),
                                new SensorReading("sensor_7", 1547718202L, 6.7),
                                new SensorReading("sensor_10", 1547718205L, 38.1)));

        DataStream<Integer> integerDataStream = env.fromElements(1, 2, 4, 67, 189);

        // 打印输出
        dataStream.print("data");
        integerDataStream.print("int");

        // 执行
        env.execute();
    }
}
