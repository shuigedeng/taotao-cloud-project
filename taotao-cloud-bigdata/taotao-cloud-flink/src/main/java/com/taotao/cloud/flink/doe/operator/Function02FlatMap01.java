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

package com.taotao.cloud.flink.doe.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 每摄入一个元素  执行一次   扁平化
 * 返回值  0 1 多个  大部分返回多个结果数据
 */
public class Function02FlatMap01 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<String> lines = see.socketTextStream("doe01", 8899);

        SingleOutputStreamOperator<String> res =
                lines.flatMap(
                        new FlatMapFunction<String, String>() {
                            // 每摄入一个元素  执行一次
                            @Override
                            public void flatMap(String value, Collector<String> out)
                                    throws Exception {

                                try {
                                    String[] words = value.split("\\s+");
                                    for (String word : words) {
                                        String newWord = word.toUpperCase();
                                        out.collect(newWord);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        });

        res.print();
        see.execute();
    }
}
