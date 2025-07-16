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

package com.taotao.cloud.flink.ttc.sink;

import java.time.Duration;
import java.time.ZoneId;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SinkFile {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // TODO 每个目录中，都有 并行度个数的 文件在写入
        env.setParallelism(2);

        // 必须开启checkpoint，否则一直都是 .inprogress
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        DataGeneratorSource<String> dataGeneratorSource =
                new DataGeneratorSource<>(
                        new GeneratorFunction<Long, String>() {
                            @Override
                            public String map(Long value) throws Exception {
                                return "Number:" + value;
                            }
                        },
                        Long.MAX_VALUE,
                        RateLimiterStrategy.perSecond(1000),
                        Types.STRING);

        DataStreamSource<String> dataGen =
                env.fromSource(
                        dataGeneratorSource, WatermarkStrategy.noWatermarks(), "data-generator");

        // TODO 输出到文件系统
        FileSink<String> fieSink =
                FileSink
                        // 输出行式存储的文件，指定路径、指定编码
                        .<String>forRowFormat(
                                new Path("f:/tmp"), new SimpleStringEncoder<>("UTF-8"))
                        // 输出文件的一些配置： 文件名的前缀、后缀
                        .withOutputFileConfig(
                                OutputFileConfig.builder()
                                        .withPartPrefix("atguigu-")
                                        .withPartSuffix(".log")
                                        .build())
                        // 按照目录分桶：如下，就是每个小时一个目录
                        .withBucketAssigner(
                                new DateTimeBucketAssigner<>(
                                        "yyyy-MM-dd HH", ZoneId.systemDefault()))
                        // 文件滚动策略:  1分钟 或 1m
                        .withRollingPolicy(
                                DefaultRollingPolicy.builder()
                                        .withRolloverInterval(Duration.ofMinutes(1))
                                        .withMaxPartSize(new MemorySize(1024 * 1024))
                                        .build())
                        .build();

        dataGen.sinkTo(fieSink);

        env.execute();
    }
}
