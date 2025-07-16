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

package com.taotao.cloud.flink.ttc.partition;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO DataStream实现Wordcount：读socket（无界流）
 *
 * @author shuigedeng
 * @version 1.0
 */
public class PartitionDemo {
    public static void main(String[] args) throws Exception {
        // TODO 1. 创建执行环境
        //        StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        env.setParallelism(2);

        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);

        // shuffle随机分区: random.nextInt(下游算子并行度)
        //        socketDS.shuffle().print();

        // rebalance轮询：nextChannelToSendTo = (nextChannelToSendTo + 1) % 下游算子并行度
        // 如果是 数据源倾斜的场景， source后，调用rebalance，就可以解决 数据源的 数据倾斜
        //        socketDS.rebalance().print();

        // rescale缩放： 实现轮询， 局部组队，比rebalance更高效
        //        socketDS.rescale().print();

        // broadcast 广播：  发送给下游所有的子任务
        //        socketDS.broadcast().print();

        // global 全局： 全部发往 第一个子任务
        // return 0;
        socketDS.global().print();

        // keyby: 按指定key去发送，相同key发往同一个子任务
        // one-to-one: Forward分区器

        // 总结： Flink提供了 7种分区器+ 1种自定义

        env.execute();
    }
}

/**
 *
 *
 *
 */
