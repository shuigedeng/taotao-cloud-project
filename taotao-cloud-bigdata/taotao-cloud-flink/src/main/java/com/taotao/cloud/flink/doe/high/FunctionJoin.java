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

package com.taotao.cloud.flink.doe.high;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;

/**
 * @since: 2024/1/3
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class FunctionJoin {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);
        // uid,name ,ts
        DataStreamSource<String> userStream = see.socketTextStream("doe01", 6666);
        // oid , money, uid, ts
        DataStreamSource<String> orderStream = see.socketTextStream("doe01", 7777);
        // 用户数据流
        SingleOutputStreamOperator<UserBean> userBeans =
                userStream.map(
                        new MapFunction<String, UserBean>() {
                            @Override
                            public UserBean map(String value) throws Exception {
                                String[] arr = value.split(",");
                                return new UserBean(
                                        Integer.parseInt(arr[0]), arr[1], Long.parseLong(arr[2]));
                            }
                        });
        // 订单数据流
        SingleOutputStreamOperator<OrderBean> orderBeans =
                orderStream.map(
                        new MapFunction<String, OrderBean>() {
                            @Override
                            public OrderBean map(String value) throws Exception {
                                String[] arr = value.split(",");
                                return new OrderBean(
                                        Integer.parseInt(arr[0]),
                                        Double.parseDouble(arr[1]),
                                        Integer.parseInt(arr[2]),
                                        Long.parseLong(arr[3]));
                            }
                        });
        // 用户数据流   分配水位线
        SingleOutputStreamOperator<UserBean> userBeanWm =
                userBeans.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<UserBean>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<UserBean>() {
                                            @Override
                                            public long extractTimestamp(
                                                    UserBean element, long recordTimestamp) {
                                                return element.getTs();
                                            }
                                        }));
        // 订单数据流 分配水位线
        SingleOutputStreamOperator<OrderBean> orderBeanWm =
                orderBeans.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<OrderBean>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<OrderBean>() {
                                            @Override
                                            public long extractTimestamp(
                                                    OrderBean element, long recordTimestamp) {
                                                return element.getTs();
                                            }
                                        }));

        /**
         * 两个关联的数据流分别分配水位线
         * 只有两个数据流对应的窗口 比如 流1 [10  20]  和流2  [10  20] 都触发了窗口计算 :
         *    两个流中的数据才会按照对应的关联条件进行等值关联 然后进行输出
         *
         */
        DataStream<String> res =
                userBeanWm
                        .join(orderBeanWm)
                        .where(user -> user.getUid())
                        .equalTo(orders -> orders.getUid())
                        .window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)))
                        .apply(
                                new JoinFunction<UserBean, OrderBean, String>() {
                                    /**
                                     * 同一时间窗口 , 且两个liu能关联上的key会调用一次方法
                                     * @param first The element from first input.
                                     * @param second The element from second input.
                                     * @return
                                     * @throws Exception
                                     */
                                    @Override
                                    public String join(UserBean first, OrderBean second)
                                            throws Exception {
                                        return second.getOid()
                                                + ","
                                                + second.getMoney()
                                                + ","
                                                + second.getTs()
                                                + ","
                                                + first.getName();
                                    }
                                });

        res.print();

        see.execute();
    }
}
