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

package com.taotao.cloud.flink.ttc.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * TODO DataStream实现Wordcount：读文件（有界流）
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WordCountStreamDemo {
    public static void main(String[] args) throws Exception {
//        // TODO 1.创建执行环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // TODO 2.读取数据:从文件读
//        DataStreamSource<String> lineDS = env.readTextFile("input/word.txt");
//
//        // TODO 3.处理数据: 切分、转换、分组、聚合
//        // TODO 3.1 切分、转换
//        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOneDS =
//                lineDS.flatMap(
//                        new FlatMapFunction<String, Tuple2<String, Integer>>() {
//                            @Override
//                            public void flatMap(
//                                    String value, Collector<Tuple2<String, Integer>> out)
//                                    throws Exception {
//                                // 按照 空格 切分
//                                String[] words = value.split(" ");
//                                for (String word : words) {
//                                    // 转换成 二元组 （word，1）
//                                    Tuple2<String, Integer> wordsAndOne = Tuple2.of(word, 1);
//                                    // 通过 采集器 向下游发送数据
//                                    out.collect(wordsAndOne);
//                                }
//                            }
//                        });
//        // TODO 3.2 分组
//        KeyedStream<Tuple2<String, Integer>, String> wordAndOneKS =
//                wordAndOneDS.keyBy(
//                        new KeySelector<Tuple2<String, Integer>, String>() {
//                            @Override
//                            public String getKey(Tuple2<String, Integer> value) throws Exception {
//                                return value.f0;
//                            }
//                        });
//        // TODO 3.3 聚合
//        SingleOutputStreamOperator<Tuple2<String, Integer>> sumDS = wordAndOneKS.sum(1);
//
//        // TODO 4.输出数据
//        sumDS.print();
//
//        // TODO 5.执行：类似 sparkstreaming最后 ssc.start()
//        env.execute();
    }
}

/**
 * 接口 A，里面有一个方法a()
 * 1、正常实现接口步骤：
 * <p>
 * 1.1 定义一个class B  实现 接口A、方法a()
 * 1.2 创建B的对象：   B b = new B()
 * <p>
 * <p>
 * 2、接口的匿名实现类：
 * new A(){
 * a(){
 * <p>
 * }
 * }
 */
