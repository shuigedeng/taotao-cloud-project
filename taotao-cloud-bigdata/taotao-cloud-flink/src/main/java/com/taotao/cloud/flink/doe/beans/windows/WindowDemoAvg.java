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
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @since: 2024/1/2
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 每10s计算一次最近10s订单均价  [时间语义 数据时间]
 * 每个城市的订单均价
 */
public class WindowDemoAvg {
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
        WindowedStream<OrdersBean, String, TimeWindow> window =
                keyed.window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)));
        /**
         * reduce的返回数据类型和输入数据类型一致 , 中间缓存的数据类型和输入类型一致
         * aggregate :用户设定返回值类型 ,  设定中间缓存的数据类型
         * 统计每个城市的总额  , 订单总个数 , 均价
         * 泛型 :
         * 1  输入数据类型
         * 2 中间缓存的数据类型
         * 3 最终的输出结果类型
         */
        SingleOutputStreamOperator<Tuple2<String, Double>> res =
                window.aggregate(
                        new AggregateFunction<
                                OrdersBean,
                                Tuple3<String, Double, Integer>,
                                Tuple2<String, Double>>() {
                            // 中间 缓存的默认值
                            @Override
                            public Tuple3<String, Double, Integer> createAccumulator() {
                                return Tuple3.of("", 0D, 0);
                            }

                            // 组内输入一条数据
                            @Override
                            public Tuple3<String, Double, Integer> add(
                                    OrdersBean value, Tuple3<String, Double, Integer> accumulator) {
                                double money = value.getMoney();
                                return Tuple3.of(
                                        accumulator.f0, accumulator.f1 + money, accumulator.f2 + 1);
                            }

                            // 触发窗口时 封装结果数据
                            @Override
                            public Tuple2<String, Double> getResult(
                                    Tuple3<String, Double, Integer> accumulator) {
                                return Tuple2.of(accumulator.f0, accumulator.f1 / accumulator.f2);
                            }

                            @Override
                            public Tuple3<String, Double, Integer> merge(
                                    Tuple3<String, Double, Integer> a,
                                    Tuple3<String, Double, Integer> b) {
                                return null;
                            }
                        });

        /*    SingleOutputStreamOperator<OrdersBean> res = window.reduce(new ReduceFunction<OrdersBean>() {
             double  sum  = 0d ;
             int cnt = 0 ;
            @Override
            public OrdersBean reduce(OrdersBean value1, OrdersBean value2) throws Exception {
                double sum = 0;
                double cnt = 1;
                cnt++;
                sum = value1.getMoney() + value2.getMoney();
                return value1;
            }
        });*/

        // res.print() ;
        see.execute();
    }
}
