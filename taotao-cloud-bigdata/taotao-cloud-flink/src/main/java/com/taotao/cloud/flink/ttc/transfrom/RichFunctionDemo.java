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

package com.taotao.cloud.flink.ttc.transfrom;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class RichFunctionDemo {
    public static void main(String[] args) throws Exception {
        //        StreamExecutionEnvironment env =
        // StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(2);

        DataStreamSource<String> source = env.socketTextStream("hadoop102", 7777);
        SingleOutputStreamOperator<Integer> map =
                source.map(
                        new RichMapFunction<String, Integer>() {

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                System.out.println(
                                        "子任务编号="
                                                + getRuntimeContext().getIndexOfThisSubtask()
                                                + "，子任务名称="
                                                + getRuntimeContext().getTaskNameWithSubtasks()
                                                + ",调用open()");
                            }

                            @Override
                            public void close() throws Exception {
                                super.close();
                                System.out.println(
                                        "子任务编号="
                                                + getRuntimeContext().getIndexOfThisSubtask()
                                                + "，子任务名称="
                                                + getRuntimeContext().getTaskNameWithSubtasks()
                                                + ",调用close()");
                            }

                            @Override
                            public Integer map(String value) throws Exception {
                                return Integer.parseInt(value) + 1;
                            }
                        });

        /**
         * TODO RichXXXFunction: 富函数
         * 1、多了生命周期管理方法：
         *    open(): 每个子任务，在启动时，调用一次
         *    close():每个子任务，在结束时，调用一次
         *      => 如果是flink程序异常挂掉，不会调用close
         *      => 如果是正常调用 cancel命令，可以close
         * 2、多了一个 运行时上下文
         *    可以获取一些运行时的环境信息，比如 子任务编号、名称、其他的.....
         */
        //        DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 4);
        //        SingleOutputStreamOperator<Integer> map = source.map(new RichMapFunction<Integer,
        // Integer>() {
        //
        //            @Override
        //            public void open(Configuration parameters) throws Exception {
        //                super.open(parameters);
        //                System.out.println(
        //                        "子任务编号="+getRuntimeContext().getIndexOfThisSubtask()
        //                                +"，子任务名称="+getRuntimeContext().getTaskNameWithSubtasks()
        //                                +",调用open()");
        //            }
        //
        //            @Override
        //            public void close() throws Exception {
        //                super.close();
        //                System.out.println(
        //                        "子任务编号="+getRuntimeContext().getIndexOfThisSubtask()
        //                                +"，子任务名称="+getRuntimeContext().getTaskNameWithSubtasks()
        //                                +",调用close()");
        //            }
        //
        //            @Override
        //            public Integer map(Integer value) throws Exception {
        //                return value + 1;
        //            }
        //        });

        map.print();

        env.execute();
    }
}
