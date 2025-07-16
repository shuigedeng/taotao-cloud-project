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

package com.taotao.cloud.flink.atguigu.wc;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        // 创建流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //        env.setParallelism(1);
        //        env.disableOperatorChaining();

        //        // 从文件中读取数据
        //        String inputPath =
        // "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\hello.txt";
        //        DataStream<String> inputDataStream = env.readTextFile(inputPath);

        // 用parameter tool工具从程序启动参数中提取配置项

        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");

        // 从socket文本流读取数据
        DataStream<String> inputDataStream = env.socketTextStream(host, port);

        // 基于数据流进行转换计算
        DataStream<Tuple2<String, Integer>> resultStream =
                inputDataStream
                        .flatMap(new WordCount.MyFlatMapper())
                        .slotSharingGroup("green")
                        .keyBy(0)
                        .sum(1)
                        .setParallelism(2)
                        .slotSharingGroup("red");

        resultStream.print().setParallelism(1);

        // 执行任务
        env.execute();
    }
}
