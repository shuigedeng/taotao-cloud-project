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

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @since: 2024/1/5
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 1  处理流中的每个元素  可实现  map   flatMap  filter
 * 2 将数据分流处理
 */
public class FunctionProcessSplit {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);

        // a    A   流
        // b    B   流
        // c    C   流
        OutputTag a = new OutputTag("A", TypeInformation.of(String.class));
        OutputTag b = new OutputTag("B", TypeInformation.of(String.class));
        OutputTag c = new OutputTag("C", TypeInformation.of(String.class));

        SingleOutputStreamOperator<String> data =
                ds.process(
                        new ProcessFunction<String, String>() {
                            @Override
                            public void processElement(
                                    String value,
                                    ProcessFunction<String, String>.Context ctx,
                                    Collector<String> out)
                                    throws Exception {

                                if (value.startsWith("a")) { // 将a开头的数据存储在A流中
                                    ctx.output(a, value);
                                } else if (value.startsWith("b")) { // 将b开头的数据存储在B流中
                                    ctx.output(b, value);
                                } else if (value.startsWith("c")) { // 将c开头的数据存储在C流中
                                    ctx.output(c, value);
                                } else { // 主流中
                                    out.collect(value);
                                }
                            }
                        });
        // 从返回的结果中获取测流数据
        SideOutputDataStream streamA = data.getSideOutput(a);

        streamA.print(); // 打印主流数据

        see.execute();
    }
}
