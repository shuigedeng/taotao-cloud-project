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

package com.taotao.cloud.flink.doe.env;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @since: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 一个算子  可以生成一个独立的并行Task
 * 多个算子也可以合并成一个并行的Task
 *
 */
public class Demo03WordCount {
    public static void main(String[] args) throws Exception {
        // 1 获取环境
        Configuration configuration = new Configuration();
        configuration.setString("rest.port", "8888");
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        // 默认并行度是所有的可用核数 [32]
        // 设置程序的默认并行度
        see.setParallelism(4);
        // -------------------------------------------------------- [1]
        // 2 创建数据源
        DataStreamSource<String> lines = see.socketTextStream("doe01", 8899);

        // --------------------------------------------------------  [32]
        // 3 将每行数据  切割  压平  组装成(单词,1)
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordOneDs =
                lines.flatMap(
                        new FlatMapFunction<String, Tuple2<String, Integer>>() {
                            @Override
                            public void flatMap(String line, Collector<Tuple2<String, Integer>> out)
                                    throws Exception {
                                // 切割当前行数据
                                String[] arr = line.split("\\s+");
                                // 处理每个单词
                                for (String word : arr) {
                                    Tuple2<String, Integer> wordAndOne = Tuple2.of(word, 1);
                                    // 收集结果返回
                                    out.collect(wordAndOne);
                                }
                            }
                        });
        // 4 分组  按照单词分组  (二元组的第一个位置  f0) 聚合
        // 泛型1   输入数据类型
        // 泛型2   key的数据类型
        // --------------------------------------------------------[32]

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordOneDs2 =
                wordOneDs.setParallelism(6);
        KeyedStream<Tuple2<String, Integer>, String> keyed =
                wordOneDs2.keyBy(
                        new KeySelector<Tuple2<String, Integer>, String>() {
                            @Override
                            public String getKey(Tuple2<String, Integer> value) throws Exception {
                                return value.f0;
                            }
                        });
        // 聚合   组内聚合
        //  SingleOutputStreamOperator<Tuple2<String, Integer>> res = keyed.sum("f1");
        // --------------------------------------------------------[32]
        SingleOutputStreamOperator<Tuple2<String, Integer>> res = keyed.sum(1); // 0 开始计数

        // --------------------------------------------------------[32]

        System.out.println(lines.getParallelism()); // 1
        System.out.println(wordOneDs.getParallelism()); // 32
        System.out.println(keyed.getParallelism()); // 32  *
        System.out.println(res.getParallelism()); // 32     *

        res.print();

        see.execute();
    }
}
