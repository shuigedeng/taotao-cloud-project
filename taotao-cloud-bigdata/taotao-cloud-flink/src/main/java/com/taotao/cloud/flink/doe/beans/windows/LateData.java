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

import com.taotao.cloud.flink.doe.beans.OrdersBean;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @since: 2024/1/2
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 全量聚合函数
 * keyBy后  时间窗口  以事件时间为时间语义
 * 1,zss,bj,100,10000
 * 2,lss,bj,100,18000
 * 3,lss,bj,100,19000
 * 5,lss,bj,100,21000     -
 * 4,lss,bj,100,19100
 * 6,lss,bj,100,22000     -
 * 7,lss,bj,100,19300
 * 8,lss,bj,100,24000     -
 * 9,lss,bj,100,19400
 */
public class LateData {
    public static void main(String[] args) throws Exception {
        // 1  环境
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 2  处理数据 ,  将数据封装成Bean
        SingleOutputStreamOperator<OrdersBean> beans =
                ds.map(
                        new MapFunction<String, OrdersBean>() {
                            @Override
                            public OrdersBean map(String value) throws Exception {
                                OrdersBean orderBean = new OrdersBean();
                                try {
                                    String[] arr = value.split(",");
                                    int oid = Integer.parseInt(arr[0]);
                                    String name = arr[1];
                                    String city = arr[2];
                                    double money = Double.parseDouble(arr[3]);
                                    long ts = Long.parseLong(arr[4]);
                                    orderBean = new OrdersBean(oid, name, city, money, ts);

                                } catch (Exception e) {

                                }
                                return orderBean;
                            }
                        });
        // 3 分配水位线 允许乱序数据   *****************  forBoundedOutOfOrderness(Duration.ofSeconds(1))
        // *****************
        SingleOutputStreamOperator<OrdersBean> beansWithWm =
                beans.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<OrdersBean>forBoundedOutOfOrderness(
                                        Duration.ofSeconds(1))
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<OrdersBean>() {
                                            @Override
                                            public long extractTimestamp(
                                                    OrdersBean element, long recordTimestamp) {
                                                return element.getTs();
                                            }
                                        }));

        // 4  对数据进行分组  按照城市
        KeyedStream<OrdersBean, String> keyed =
                beansWithWm.keyBy(
                        new KeySelector<OrdersBean, String>() {
                            @Override
                            public String getKey(OrdersBean value) throws Exception {
                                return value.getCity();
                            }
                        });

        /**
         * 5 滚动窗口
         *    keyby后的数据调用时间窗口
         *    当时间窗口触发后 ,当前窗口中的所有的数据按照分组key统计
         *    ************************.allowedLateness(Duration.ofSeconds(2)) **********
         *    ************************sideOutputLateData(late_data) **********
         */
        OutputTag<OrdersBean> late_data =
                new OutputTag<>("late_data", TypeInformation.of(new TypeHint<OrdersBean>() {}));
        WindowedStream<OrdersBean, String, TimeWindow> window =
                keyed.window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)))
                        .allowedLateness(Duration.ofSeconds(2))
                        .sideOutputLateData(late_data);

        SingleOutputStreamOperator<OrdersBean> res =
                window.process(
                        new ProcessWindowFunction<OrdersBean, OrdersBean, String, TimeWindow>() {
                            @Override
                            public void process(
                                    String s,
                                    ProcessWindowFunction<
                                                            OrdersBean,
                                                            OrdersBean,
                                                            String,
                                                            TimeWindow>
                                                    .Context
                                            context,
                                    Iterable<OrdersBean> elements,
                                    Collector<OrdersBean> out)
                                    throws Exception {
                                for (OrdersBean element : elements) {
                                    out.collect(element);
                                }
                            }
                        });
        // 获取迟到的数据
        SideOutputDataStream<OrdersBean> sideOutput = res.getSideOutput(late_data);
        sideOutput.print();
        // res.union(sideOutput)
        see.execute();
    }
}
