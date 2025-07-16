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

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * JStreamWordCount
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/3 09:07
 */
public class JStreamLambdaWordCount {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
        conf.setInteger(RestOptions.PORT, 8050);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        DataStream<String> dss = env.socketTextStream("127.0.0.1", 8888);

        DataStream<Tuple2<String, Integer>> dso =
                dss.flatMap(
                        (FlatMapFunction<String, Tuple2<String, Integer>>)
                                (value, out) -> {
                                    String[] s = value.split(" ");
                                    for (String s1 : s) {
                                        out.collect(Tuple2.of(s1, 1));
                                    }
                                });

        KeyedStream<Tuple2<String, Integer>, String> kst =
                dso.keyBy(
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
