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

import com.taotao.cloud.flink.doe.beans.OrdersBean;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: 统计每个城市的订单总额
 */
public class Function07Sum {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(4);

        DataStreamSource<String> dataStreamSource = see.socketTextStream("doe01", 8899);
        // 将摄入的每条订单数据 封装成Bean
        SingleOutputStreamOperator<OrdersBean> ordersBean =
                dataStreamSource.map(
                        new MapFunction<String, OrdersBean>() {
                            @Override
                            public OrdersBean map(String value) throws Exception {
                                try {
                                    String[] arr = value.split(",");
                                    OrdersBean ordersBean =
                                            new OrdersBean(
                                                    Integer.parseInt(arr[0]),
                                                    arr[1],
                                                    arr[2],
                                                    Double.parseDouble(arr[3]),
                                                    Long.parseLong(arr[4]));
                                    return ordersBean;
                                } catch (Exception e) {
                                    return new OrdersBean();
                                }
                            }
                        });

        KeyedStream<OrdersBean, String> keyed = ordersBean.keyBy(bean -> bean.getCity());

        SingleOutputStreamOperator<OrdersBean> sumed = keyed.sum("money");

        sumed.print();

        see.execute();
    }
}
