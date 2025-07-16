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

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * TODO DataStream实现Wordcount：读socket（无界流）
 *
 * @author shuigedeng
 * @version 1.0
 */
public class OperatorChainDemo {
    public static void main(String[] args) throws Exception {
        // TODO 1. 创建执行环境
        //        StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.getExecutionEnvironment();
        // IDEA运行时，也可以看到webui，一般用于本地测试
        // 需要引入一个依赖 flink-runtime-web
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        // 在idea运行，不指定并行度，默认就是 电脑的 线程数
        env.setParallelism(1);

        // 全局禁用 算子链
        //        env.disableOperatorChaining();

        // TODO 2. 读取数据： socket
        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        // TODO 3. 处理数据: 切换、转换、分组、聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum =
                socketDS
                        //                .disableChaining()
                        .flatMap(
                                (String value, Collector<String> out) -> {
                                    String[] words = value.split(" ");
                                    for (String word : words) {
                                        out.collect(word);
                                    }
                                })
                        .startNewChain()
                        //                .disableChaining()
                        .returns(Types.STRING)
                        .map(word -> Tuple2.of(word, 1))
                        .returns(Types.TUPLE(Types.STRING, Types.INT))
                        .keyBy(value -> value.f0)
                        .sum(1);

        // TODO 4. 输出
        sum.print();

        // TODO 5. 执行
        env.execute();
    }
}

/**
 * 1、算子之间的传输关系：
 * 一对一
 * 重分区
 *
 * 2、算子 串在一起的条件：
 * 1） 一对一
 * 2） 并行度相同
 *
 * 3、关于算子链的api：
 * 1）全局禁用算子链：env.disableOperatorChaining();
 * 2）某个算子不参与链化：  算子A.disableChaining(),  算子A不会与 前面 和 后面的 算子 串在一起
 * 3）从某个算子开启新链条：  算子A.startNewChain()， 算子A不与 前面串在一起，从A开始正常链化
 *
 */
