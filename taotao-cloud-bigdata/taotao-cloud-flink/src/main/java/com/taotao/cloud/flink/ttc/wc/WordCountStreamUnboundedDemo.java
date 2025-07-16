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
public class WordCountStreamUnboundedDemo {
    public static void main(String[] args) throws Exception {
        // TODO 1. 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // IDEA运行时，也可以看到webui，一般用于本地测试
        // 需要引入一个依赖 flink-runtime-web
        // 在idea运行，不指定并行度，默认就是 电脑的 线程数
        //        StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        env.setParallelism(3);

        // TODO 2. 读取数据： socket
        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        // TODO 3. 处理数据: 切换、转换、分组、聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum =
                socketDS.flatMap(
                                (String value, Collector<Tuple2<String, Integer>> out) -> {
                                    String[] words = value.split(" ");
                                    for (String word : words) {
                                        out.collect(Tuple2.of(word, 1));
                                    }
                                })
                        .setParallelism(2)
                        .returns(Types.TUPLE(Types.STRING, Types.INT))
                        //                .returns(new TypeHint<Tuple2<String, Integer>>() {})
                        .keyBy(value -> value.f0)
                        .sum(1);

        // TODO 4. 输出
        sum.print();

        // TODO 5. 执行
        env.execute();
    }
}

/**
 *
 * 并行度的优先级：
 * 代码：算子 > 代码：env > 提交时指定 > 配置文件
 *
 */
