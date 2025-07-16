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

package com.taotao.cloud.flink.doe.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @since: 2023/12/31
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 1  ds.setParallelism(N)  设置数据流的并行度
 * 2  整个Job禁用算子合并  see.disableOperatorChaining() ;
 * 3 ds.startNewChain()  将当前的算子和前面的算子断开形成一个新的chain , 后面的算子可以合并过来
 * 4 ds.disableChaining() , 将ds算子前后独立出来 , 不会影响后面的算子合并
 */
public class OperatorChaining {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        // see.disableOperatorChaining() ;
        // 1 数据源
        DataStreamSource<String> ds1 = see.socketTextStream("doe01", 8899); //  1

        // 2 转大写
        SingleOutputStreamOperator<String> ds2 =
                ds1.map(String::toUpperCase).setParallelism(4); // 4
        // 3 切割出单词
        SingleOutputStreamOperator<String> ds3 =
                ds2.flatMap(
                                new FlatMapFunction<String, String>() {
                                    @Override
                                    public void flatMap(String value, Collector<String> out)
                                            throws Exception {
                                        String[] words = value.split("\\s+");
                                        for (String word : words) {
                                            out.collect(word);
                                        }
                                    }
                                })
                        .setParallelism(4) /*.startNewChain()*/; // 4
        // 4  组成(单词 , 1)
        SingleOutputStreamOperator<Tuple2<String, Integer>> ds4 =
                ds3.map(
                        new MapFunction<String, Tuple2<String, Integer>>() {
                            @Override
                            public Tuple2<String, Integer> map(String value) throws Exception {
                                return Tuple2.of(value.toLowerCase(), 1);
                            }
                        }); //  32

        // 新的ds   32
        SingleOutputStreamOperator<Tuple2<String, Integer>> ds5 =
                ds4.map(e -> e)
                        .returns(TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {}))
                        .disableChaining();
        SingleOutputStreamOperator<Tuple2<String, Integer>> ds6 =
                ds5.map(e -> e)
                        .returns(TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {}));

        ds6.print(); // 32

        see.execute();
    }
}
