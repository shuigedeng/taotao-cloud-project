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
public class SlotSharingGroupDemo {
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

        // TODO 2. 读取数据： socket
        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        // TODO 3. 处理数据: 切换、转换、分组、聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum =
                socketDS.flatMap(
                                (String value, Collector<String> out) -> {
                                    String[] words = value.split(" ");
                                    for (String word : words) {
                                        out.collect(word);
                                    }
                                })
                        .returns(Types.STRING)
                        .map(word -> Tuple2.of(word, 1))
                        .slotSharingGroup("aaa")
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
 * 1、slot特点：
 * 1）均分隔离内存，不隔离cpu
 * 2）可以共享：
 * 同一个job中，不同算子的子任务 才可以共享 同一个slot，同时在运行的
 * 前提是，属于同一个 slot共享组，默认都是“default”
 *
 * 2、slot数量 与 并行度 的关系
 * 1）slot是一种静态的概念，表示最大的并发上限
 * 并行度是一种动态的概念，表示 实际运行 占用了 几个
 *
 * 2）要求： slot数量 >= job并行度（算子最大并行度），job才能运行
 * TODO 注意：如果是yarn模式，动态申请
 * --》 TODO 申请的TM数量 = job并行度 / 每个TM的slot数，向上取整
 * 比如session： 一开始 0个TaskManager，0个slot
 * --》 提交一个job，并行度10
 * --》 10/3,向上取整，申请4个tm，
 * --》 使用10个slot，剩余2个slot
 *
 *
 *
 */
