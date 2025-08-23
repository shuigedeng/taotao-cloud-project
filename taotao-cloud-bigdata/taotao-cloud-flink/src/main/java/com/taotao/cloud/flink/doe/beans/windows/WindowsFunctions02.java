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
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @since: 2024/1/2
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 不keyBy  全局chk
 * 1) 时间窗口  事件时间语义
 *    数据中有时间 ,  分配水位线
 *
 *1,zss,bj,100,10000
 *2,zss,bj,100,12000
 *3,zss,bj,100,16000
 *4,zss,bj,100,19000
 *5,zss,bj,100,20000
 *6,zss,bj,100,19100
 *7,zss,bj,100,21100
 * 8,lss,bj,100,26000
 * --> 结果
 * OrdersBean(oid=1, name=zss, city=bj, money=200.0, ts=10000)  [5 15]
 * OrdersBean(oid=1, name=zss, city=bj, money=500.0, ts=10000)  [10 20]
 * OrdersBean(oid=3, name=zss, city=bj, money=500.0, ts=16000)  [15 25]
 *                                     [20  (8,lss,bj,100,26000) 30]
 */
public class WindowsFunctions02 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 处理数据 ,  将数据封装成Bean
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

        // 分配水位线
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
        // 开窗  滑动
        AllWindowedStream<OrdersBean, TimeWindow> windowed =
                beansWithWm.windowAll(
                        SlidingEventTimeWindows.of(Duration.ofSeconds(10), Duration.ofSeconds(5)));
        // 聚合
        SingleOutputStreamOperator<OrdersBean> res = windowed.sum("money");
        res.print();

        see.execute();
    }
}
