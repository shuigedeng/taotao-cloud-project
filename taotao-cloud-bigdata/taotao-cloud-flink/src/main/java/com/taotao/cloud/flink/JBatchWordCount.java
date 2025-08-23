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

package com.taotao.cloud.flink;




import static org.apache.flink.runtime.state.StateBackendLoader.FORST_STATE_BACKEND_NAME;
import static org.apache.flink.runtime.state.StateBackendLoader.ROCKSDB_STATE_BACKEND_NAME;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.configuration.StateBackendOptions;
import org.apache.flink.configuration.WebOptions;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SessionWindowTimeGapExtractor;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * JBatchWordCount
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/3 09:05
 */
public class JBatchWordCount {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

//        conf.set(ConfigConstants.LOCAL_START_WEBSERVER, true);
        conf.set(RestOptions.PORT, 8050);
		conf.set(StateBackendOptions.STATE_BACKEND, ROCKSDB_STATE_BACKEND_NAME);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

		FileSource.FileSourceBuilder<String> builder =
			FileSource.forRecordStreamFormat(
				new TextLineInputFormat(),  Path.fromLocalFile(new File("/Users/shuigedeng/spark/hello.txt")));

        DataStream<String> dss = env.fromSource(builder.build(), WatermarkStrategy.noWatermarks(), "file-input");;

        DataStream<String> dso =
                dss.flatMap(
                        new FlatMapFunction<String, String>() {
                            @Override
                            public void flatMap(String value, Collector<String> out)
                                    throws Exception {
                                String[] s = value.split(" ");
                                for (String s1 : s) {
                                    out.collect(s1);
                                }
                            }
                        });

        KeyedStream<String, String> keyedBy =
                dso.keyBy(
                        new KeySelector<String, String>() {
                            @Override
                            public String getKey(String value) throws Exception {
                                return value;
                            }
                        });
        WindowedStream<String, String, TimeWindow> window =
                keyedBy.window(
                        EventTimeSessionWindows.withDynamicGap(
                                new SessionWindowTimeGapExtractor<String>() {
                                    @Override
                                    public long extract(String s) {
                                        return 0;
                                    }
                                }));

        DataStream<Tuple2<String, Integer>> dst =
                dso.map(
                        new MapFunction<String, Tuple2<String, Integer>>() {
                            @Override
                            public Tuple2<String, Integer> map(String value) throws Exception {
                                return Tuple2.of(value, 1);
                            }
                        });

        KeyedStream<Tuple2<String, Integer>, String> kst =
                dst.keyBy(
                        new KeySelector<Tuple2<String, Integer>, String>() {
                            @Override
                            public String getKey(Tuple2<String, Integer> value) throws Exception {
                                return value.f0;
                            }
                        });

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = kst.sum(1);

        sum.print();

        env.execute("JBatchWordCount");
    }
}
