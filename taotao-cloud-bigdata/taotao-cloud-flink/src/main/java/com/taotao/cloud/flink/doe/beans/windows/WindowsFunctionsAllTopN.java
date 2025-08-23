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
import com.taotao.cloud.flink.doe.custom.MyTrigger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.KeyedStateStore;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @since: 2024/1/2
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 全量聚合函数
 * keyBy后  时间窗口  以事件时间为时间语义
 * 1,zss,bj,900,10000
 * 2,lss,bj,200,18000
 * 3,lss,bj,300,19000
 * 4,lss,bj,400,19500
 * 5,zss,sh,100,18900
 * 6,zss,sh,400,12000
 * 7,lss,sh,200,21000
 * 8,lss,bj,100,22000
 */
public class WindowsFunctionsAllTopN {
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

        // 对数据进行分组  按照城市
        KeyedStream<OrdersBean, String> keyed =
                beansWithWm.keyBy(
                        new KeySelector<OrdersBean, String>() {
                            @Override
                            public String getKey(OrdersBean value) throws Exception {
                                return value.getCity();
                            }
                        });

        /**
         * 滚动窗口
         *    keyby后的数据调用时间窗口
         *    当时间窗口触发后 ,当前窗口中的所有的数据按照分组key统计
         */
        WindowedStream<OrdersBean, String, TimeWindow> window =
                keyed.window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)));
        /**
         *  统计10s内各城市的订单金额Top3
         */
        window.trigger(MyTrigger.create())
        // .evictor()
        ;
        SingleOutputStreamOperator<OrdersBean> res =
                window.process(
                        new ProcessWindowFunction<OrdersBean, OrdersBean, String, TimeWindow>() {
                            @Override
                            public void process(
                                    String key,
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
                                context.currentWatermark(); // 水位线
                                TimeWindow window1 = context.window(); // 窗口信息
                                KeyedStateStore keyedStateStore = context.windowState(); // 状态
                                context.currentProcessingTime(); // 处理时间
                                window1.getEnd(); // 窗口的范围
                                window1.getStart();

                                ArrayList<OrdersBean> list = new ArrayList<>();
                                for (OrdersBean bean : elements) {
                                    list.add(bean);
                                }
                                list.sort(
                                        new Comparator<OrdersBean>() {
                                            @Override
                                            public int compare(OrdersBean o1, OrdersBean o2) {
                                                return Double.compare(o2.getMoney(), o1.getMoney());
                                            }
                                        });

                                for (int i = 0; i < Math.min(list.size(), 3); i++) {
                                    out.collect(list.get(i));
                                }
                            }
                        });

        res.print();
        see.execute();
    }
}
