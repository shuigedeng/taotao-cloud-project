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

package com.taotao.cloud.flink.doe.high;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @since: 2024/1/5
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 1  处理流中的每个元素  可实现  map   flatMap  filter
 * 2 将数据分流处理
 */
public class FunctionProcess {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 每行转大写
        SingleOutputStreamOperator<String> ds2 = ds.map(String::toUpperCase);
        /**
         * 使用方式1      每行转小写  map
         */
        SingleOutputStreamOperator<String> res1 =
                ds2.process(
                        new ProcessFunction<String, String>() {
                            /**
                             *
                             * @param value The input value.  摄入的每条数据
                             * @param ctx  测流   时间服务
                             * @param out 返回数据的收集器
                             * @throws Exception
                             */
                            @Override
                            public void processElement(
                                    String value,
                                    ProcessFunction<String, String>.Context ctx,
                                    Collector<String> out)
                                    throws Exception {
                                out.collect(value.toLowerCase());
                            }
                        });

        /**
         * 使用2  flatMap
         */
        ds2.process(
                new ProcessFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void processElement(
                            String value,
                            ProcessFunction<String, Tuple2<String, Integer>>.Context ctx,
                            Collector<Tuple2<String, Integer>> out)
                            throws Exception {
                        String[] split = value.split("\\s+");

                        for (String word : split) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                }) /*.print()*/;

        /**
         * 使用3  filter
         */
        ds2.process(
                        new ProcessFunction<String, String>() {
                            @Override
                            public void processElement(
                                    String value,
                                    ProcessFunction<String, String>.Context ctx,
                                    Collector<String> out)
                                    throws Exception {
                                if (value.startsWith("H")) {
                                    out.collect(value);
                                }
                            }
                        })
                .print();

        // res1.print() ;

        see.execute();
    }
}
