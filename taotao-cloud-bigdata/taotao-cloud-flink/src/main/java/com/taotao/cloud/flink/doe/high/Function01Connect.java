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

package com.taotao.cloud.flink.doe.high;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @since: 2024/1/3
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function01Connect {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<Integer> ds1 = see.fromElements(1, 2, 3, 4, 5, 6, 7);
        DataStreamSource<String> ds2 =
                see.fromElements("java", "sql", "scala", "python", "c++", "C");

        ConnectedStreams<Integer, String> connect = ds1.connect(ds2);

        connect.map(
                        new CoMapFunction<Integer, String, String>() {

                            //    String  str = "doe"  ;
                            int i = 0;

                            @Override
                            public String map1(Integer value) throws Exception {
                                i++;
                                return value * 10 + "-" + i + "";
                            }

                            @Override
                            public String map2(String value) throws Exception {
                                i++;
                                return value.toUpperCase() + "-" + i;
                            }
                        })
                .print();
        /*     SingleOutputStreamOperator<String> res = connectedStreams.map(new CoMapFunction<Integer, String, String>() {

                    // 流1
                    @Override
                    public String map1(Integer value) throws Exception {
                        return value * 10 + "";
                    }

                    // 流2  字符串
                    @Override
                    public String map2(String value) throws Exception {
                        return value.toUpperCase();
                    }
                });
        */

        see.execute();
    }
}
