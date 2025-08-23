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

package com.taotao.cloud.flink.doe.sink;

import java.time.Duration;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

/**
 * @since: 2023/12/30
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Sink02File {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.enableCheckpointing(1000);

        see.setParallelism(2);
        // 数据源  处理数据  结果
        SingleOutputStreamOperator<String> ds =
                see.socketTextStream("doe01", 8899).map(String::toUpperCase);

        OutputFileConfig outputFileConfig = new OutputFileConfig("doe44-", ".csv");

        // 将结果输出到文件中
        FileSink fileSink =
                FileSink.<String>forRowFormat(new Path("data/res"), new SimpleStringEncoder())
                        // 设置输出文件滚动策略  定期   大小   无数据时间间隔
                        .withRollingPolicy(
                                DefaultRollingPolicy.builder()
                                        .withRolloverInterval(Duration.ofMinutes(1))
                                        .build())
                        // 检查生成桶文件夹的时间间隔
                        .withBucketCheckInterval(30000)
                        // 输出文件配置对象
                        .withOutputFileConfig(outputFileConfig)
                        // 存储文件的桶文件夹的命名格式  时间文件夹
                        .withBucketAssigner(new DateTimeBucketAssigner())
                        .build();

        ds.sinkTo(fileSink);

        see.execute();
    }
}
