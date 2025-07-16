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

package com.taotao.cloud.flink.doe.waters;

import com.taotao.cloud.flink.doe.beans.OrdersBean;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @since: 2023/12/31
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 水位线  类  Watermark(ts) ;
 * 水位线的生成逻辑
 * 使用事件时间语义处理数据时 , 在数据流上分配水位线(在数据流中生成水位线)
 * 1,zss,bj,100,10000
 * 2,lss,bj,100,12000
 * 3,ww,bj,100,13000
 */
public class WatermarkDemoObservation {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
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
        //  beans.assignTimestampsAndWatermarks(WatermarkStrategy.noWatermarks());
        // 不允许延迟  [单调递增的数据时使用]
        beans.assignTimestampsAndWatermarks(
                WatermarkStrategy.<OrdersBean>forMonotonousTimestamps());
        SingleOutputStreamOperator<OrdersBean> beansWithWM =
                beans.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<OrdersBean>forBoundedOutOfOrderness(
                                        Duration.ofSeconds(2)) // 2000ms
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<OrdersBean>() {
                                            @Override
                                            public long extractTimestamp(
                                                    OrdersBean element, long recordTimestamp) {
                                                return element.getTs();
                                            }
                                        }));

        // 观察水位线  1) 监控页面上有水位线指标    2) 在代码中获取当前数据的水位线
        // 处理每条数据的方法  类似于map算子
        SingleOutputStreamOperator<String> res =
                beansWithWM.process(
                        new ProcessFunction<OrdersBean, String>() {
                            // processElement处理每个元素
                            @Override
                            public void processElement(
                                    OrdersBean value,
                                    ProcessFunction<OrdersBean, String>.Context ctx,
                                    Collector<String> out)
                                    throws Exception {
                                TimerService timerService = ctx.timerService();
                                // 当前数据的水位线
                                long wm = timerService.currentWatermark();
                                // 当前数据的事件时间
                                Long ts = value.getTs();
                                out.collect("数据时间: " + ts + " ; 水位线: " + wm);
                            }
                        });

        res.print();
        see.execute();
    }
}
