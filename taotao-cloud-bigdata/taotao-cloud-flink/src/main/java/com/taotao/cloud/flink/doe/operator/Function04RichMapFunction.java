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

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function04RichMapFunction {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);

        ds.map(
                        new RichMapFunction<String, String>() {

                            // 在作业中只执行一次
                            // 生命周期方法
                            @Override
                            public void open(Configuration parameters) throws Exception {
                                // 获取状态对象
                            }

                            /**
                             * 每个元素执行一次
                             * @param value The input value.
                             * @return
                             * @throws Exception
                             */
                            @Override
                            public String map(String value) throws Exception {
                                // 将计算的中间数据 使用状态维护
                                return value.toUpperCase();
                            }

                            // 在作业中只执行一次
                            // 生命周期方法
                            @Override
                            public void close() throws Exception {
                                super.close();
                            }
                        })
                .print();
        see.execute();
    }
}
