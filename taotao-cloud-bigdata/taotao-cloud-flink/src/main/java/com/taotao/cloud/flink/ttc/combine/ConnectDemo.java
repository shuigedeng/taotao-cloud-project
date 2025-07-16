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

package com.taotao.cloud.flink.ttc.combine;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class ConnectDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //        DataStreamSource<Integer> source1 = env.fromElements(1, 2, 3);
        //        DataStreamSource<String> source2 = env.fromElements("a", "b", "c");

        SingleOutputStreamOperator<Integer> source1 =
                env.socketTextStream("hadoop102", 7777).map(i -> Integer.parseInt(i));

        DataStreamSource<String> source2 = env.socketTextStream("hadoop102", 8888);

        /**
         * TODO 使用 connect 合流
         * 1、一次只能连接 2条流
         * 2、流的数据类型可以不一样
         * 3、 连接后可以调用 map、flatmap、process来处理，但是各处理各的
         */
        ConnectedStreams<Integer, String> connect = source1.connect(source2);

        SingleOutputStreamOperator<String> result =
                connect.map(
                        new CoMapFunction<Integer, String, String>() {
                            @Override
                            public String map1(Integer value) throws Exception {
                                return "来源于数字流:" + value.toString();
                            }

                            @Override
                            public String map2(String value) throws Exception {
                                return "来源于字母流:" + value;
                            }
                        });

        result.print();

        env.execute();
    }
}
