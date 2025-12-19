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

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * TransformTest1_Base
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class TransformTest1_Base {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从文件读取数据
        DataStream<String> inputStream =
                env.readTextFile(
                        "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // 1. map，把String转换成长度输出
        DataStream<Integer> mapStream =
                inputStream.map(
                        new MapFunction<String, Integer>() {
                            @Override
                            public Integer map( String value ) throws Exception {
                                return value.length();
                            }
                        });

        // 2. flatmap，按逗号分字段
        DataStream<String> flatMapStream =
                inputStream.flatMap(
                        new FlatMapFunction<String, String>() {
                            @Override
                            public void flatMap( String value, Collector<String> out )
                                    throws Exception {
                                String[] fields = value.split(",");
                                for (String field : fields)
                                    out.collect(field);
                            }
                        });

        // 3. filter, 筛选sensor_1开头的id对应的数据
        DataStream<String> filterStream =
                inputStream.filter(
                        new FilterFunction<String>() {
                            @Override
                            public boolean filter( String value ) throws Exception {
                                return value.startsWith("sensor_1");
                            }
                        });

        // 打印输出
        mapStream.print("map");
        flatMapStream.print("flatMap");
        filterStream.print("filter");

        env.execute();
    }
}
