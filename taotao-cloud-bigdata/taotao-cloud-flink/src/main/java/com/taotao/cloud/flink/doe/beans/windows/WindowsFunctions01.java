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

package com.taotao.cloud.flink.doe.beans.windows;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @since: 2024/1/2
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 不keyBy  全局chk
 * 1) 时间窗口  处理时间语义
 * 2) 计数窗口
 */
public class WindowsFunctions01 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);
        // 接收网络数据  id,age
        SingleOutputStreamOperator<Tuple2<String, Integer>> tp2 =
                see.socketTextStream("doe01", 8899)
                        .map(
                                new MapFunction<String, Tuple2<String, Integer>>() {
                                    @Override
                                    public Tuple2<String, Integer> map(String value)
                                            throws Exception {
                                        String[] arr = value.split(",");
                                        return Tuple2.of(arr[0], Integer.parseInt(arr[1]));
                                    }
                                });
        // tp2.windowAll()  时间窗口
        // tp2.countWindowAll(10 ,10) ;
        // tp2.countWindowAll(10) ;
        /**
         * 1  计数窗口 , 滚动
         */
        //  AllWindowedStream<Tuple2<String, Integer>, GlobalWindow> windowed =
        // tp2.countWindowAll(3);
        /**
         * 2  计数窗口 , 滑动
         */
        //   AllWindowedStream<Tuple2<String, Integer>, GlobalWindow> windowed =
        // tp2.countWindowAll(3, 2);

        /**
         * 3 时间窗口
         *    tp2数据流没有分配水位线  是能使用处理时间窗口处理  处理时间语义
         *    滚动窗口
         */
        AllWindowedStream<Tuple2<String, Integer>, TimeWindow> windowed =
                tp2.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)));

        /**
         * 4 时间窗口
         *    tp2数据流没有分配水位线  是能使用处理时间窗口处理  处理时间语义
         *    滑动窗口
         *
         *    每5s计算一次最近10s的数据
         *    参数一  窗口大小  10s
         *    参数二  步进 5s
         */
        AllWindowedStream<Tuple2<String, Integer>, TimeWindow> windowed2 =
                tp2.windowAll(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)));
        SingleOutputStreamOperator<Tuple2<String, Integer>> res = windowed.sum("1");
        res.print();

        see.execute();
    }
}
