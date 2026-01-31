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

package com.taotao.cloud.flink.atguigu.apitest.transform;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TransformTest3_Reduce
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class TransformTest3_Reduce {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从文件读取数据
        DataStream<String> inputStream =
                env.readTextFile(
                        "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        // 分组
        KeyedStream<SensorReading, Tuple> keyedStream = dataStream.keyBy("id");

        // reduce聚合，取最大的温度值，以及当前最新的时间戳
        SingleOutputStreamOperator<SensorReading> resultStream =
                keyedStream.reduce(
                        new ReduceFunction<SensorReading>() {
                            @Override
                            public SensorReading reduce( SensorReading value1, SensorReading value2 )
                                    throws Exception {
                                return new SensorReading(
                                        value1.getId(),
                                        value2.getTimestamp(),
                                        Math.max(value1.getTemperature(), value2.getTemperature()));
                            }
                        });

        keyedStream.reduce(
                ( curState, newData ) -> {
                    return new SensorReading(
                            curState.getId(),
                            newData.getTimestamp(),
                            Math.max(curState.getTemperature(), newData.getTemperature()));
                });

        resultStream.print();
        env.execute();
    }
}
