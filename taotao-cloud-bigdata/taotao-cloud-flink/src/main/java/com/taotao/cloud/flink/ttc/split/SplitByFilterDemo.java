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

package com.taotao.cloud.flink.ttc.split;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO 分流： 奇数、偶数拆分成不同流
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SplitByFilterDemo {
    public static void main(String[] args) throws Exception {
        //        StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        env.setParallelism(1);

        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        /**
         * TODO 使用filter来实现分流效果
         * 缺点： 同一个数据，要被处理两遍（调用两次filter）
         */
        SingleOutputStreamOperator<String> even =
                socketDS.filter(value -> Integer.parseInt(value) % 2 == 0);
        SingleOutputStreamOperator<String> odd =
                socketDS.filter(value -> Integer.parseInt(value) % 2 == 1);

        even.print("偶数流");
        odd.print("奇数流");

        env.execute();
    }
}

/**
 *
 *
 *
 */
