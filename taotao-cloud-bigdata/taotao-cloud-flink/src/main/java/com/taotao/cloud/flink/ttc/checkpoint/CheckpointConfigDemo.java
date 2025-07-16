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

package com.taotao.cloud.flink.ttc.checkpoint;

import java.time.Duration;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class CheckpointConfigDemo {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        // TODO 最终检查点：1.15开始，默认是true
        //        configuration.set(ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH, false);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        env.setParallelism(1);

        // TODO 开启 Changelog
        // 要求checkpoint的最大并发必须为1，其他参数建议在flink-conf配置文件中去指定
        env.enableChangelogStateBackend(true);

        // 代码中用到hdfs，需要导入hadoop依赖、指定访问hdfs的用户名
        System.setProperty("HADOOP_USER_NAME", "atguigu");

        // TODO 检查点常用配置
        // 1、启用检查点: 默认是barrier对齐的，周期为5s, 精准一次
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        // 2、指定检查点的存储位置
        checkpointConfig.setCheckpointStorage("hdfs://hadoop102:8020/chk");
        // 3、checkpoint的超时时间: 默认10分钟
        checkpointConfig.setCheckpointTimeout(60000);
        // 4、同时运行中的checkpoint的最大数量
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        // 5、最小等待间隔: 上一轮checkpoint结束 到 下一轮checkpoint开始 之间的间隔
        checkpointConfig.setMinPauseBetweenCheckpoints(1000);
        // 6、取消作业时，checkpoint的数据 是否保留在外部系统
        // DELETE_ON_CANCELLATION:主动cancel时，删除存在外部系统的chk-xx目录 （如果是程序突然挂掉，不会删）
        // RETAIN_ON_CANCELLATION:主动cancel时，外部系统的chk-xx目录会保存下来
        checkpointConfig.setExternalizedCheckpointCleanup(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 7、允许 checkpoint 连续失败的次数，默认0--》表示checkpoint一失败，job就挂掉
        checkpointConfig.setTolerableCheckpointFailureNumber(10);

        // TODO 开启 非对齐检查点（barrier非对齐）
        // 开启的要求： Checkpoint模式必须是精准一次，最大并发必须设为1
        checkpointConfig.enableUnalignedCheckpoints();
        // 开启非对齐检查点才生效： 默认0，表示一开始就直接用 非对齐的检查点
        // 如果大于0， 一开始用 对齐的检查点（barrier对齐）， 对齐的时间超过这个参数，自动切换成 非对齐检查点（barrier非对齐）
        checkpointConfig.setAlignedCheckpointTimeout(Duration.ofSeconds(1));

        env.socketTextStream("hadoop102", 7777)
                .flatMap(
                        (String value, Collector<Tuple2<String, Integer>> out) -> {
                            String[] words = value.split(" ");
                            for (String word : words) {
                                out.collect(Tuple2.of(word, 1));
                            }
                        })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1)
                .print();

        env.execute();
    }
}

/**
 * TODO 检查点算法的总结
 * 1、Barrier对齐： 一个Task 收到 所有上游 同一个编号的 barrier之后，才会对自己的本地状态做 备份
 *      精准一次： 在barrier对齐过程中，barrier后面的数据 阻塞等待（不会越过barrier）
 *      至少一次： 在barrier对齐过程中，先到的barrier，其后面的数据 不阻塞 接着计算
 *
 * 2、非Barrier对齐： 一个Task 收到 第一个 barrier时，就开始 执行备份，能保证 精准一次（flink 1.11出的新算法）
 *      先到的barrier，将 本地状态 备份， 其后面的数据接着计算输出
 *      未到的barrier，其 前面的数据 接着计算输出，同时 也保存到 备份中
 *      最后一个barrier到达 该Task时，这个Task的备份结束
 */
