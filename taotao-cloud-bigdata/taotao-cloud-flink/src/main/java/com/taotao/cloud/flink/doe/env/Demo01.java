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

package com.taotao.cloud.flink.doe.env;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:入门程序
 */
public class Demo01 {
    public static void main(String[] args) throws Exception {
        // 获取flink编程环境
        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * nc  -lk   端口号
         * 在本地开启一个socket服务端  使用端口 8899
         * 源源不断的创建数据  一行一行  hello  tom
         * yum  -y  install  nc  安装
         * nc  -lk  port
         */
        // 加载数据流  将数据抽象成  DataStream  以及子类
        DataStreamSource<String> dataStreamSource = see.socketTextStream("doe01", 8899);
        System.out.println(dataStreamSource.getParallelism());

        // 处理数据流
        SingleOutputStreamOperator<String> res =
                dataStreamSource.map(
                        new MapFunction<String, String>() {
                            // 处理摄入的每条数据
                            @Override
                            public String map(String value) throws Exception {
                                return value.toUpperCase();
                            }
                        });

        // 输出结果
        res.print(); //  流式输出结果
        // 本地运行
        see.execute();
    }
}
