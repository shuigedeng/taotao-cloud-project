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

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function01Map01 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        // 单并行逐个接收
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        /**
         * 算子, 需要传入计算逻辑  , 计算逻辑就有设计好的算子执行对应的计算逻辑
         * 不同的算子  , 逻辑的执行策略不同  , 返回结果不同
         * 算子(xxxFunction   重写执行的方法[处理逻辑])
         *
         *  MapFunction<IN, OUT>  泛型指定输入和输出数据类型
         */
        SingleOutputStreamOperator<String> ds2 =
                ds.map(
                        new MapFunction<String, String>() {
                            // 摄入一条数据  调用一次 算子的map方法
                            @Override
                            public String map(String value) throws Exception {
                                return value.toUpperCase();
                            }
                        });

        ds2.print();

        see.execute();

        // 传入对象实例的方式
        //  ds.map(new  MyMap()) ;

    }
}

/*class  MyMap implements  MapFunction<String , String>{
    @Override
    public String map(String value) throws Exception {
        return  value.toUpperCase();
    }
}*/
