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

import com.taotao.cloud.flink.doe.beans.HeroBean;
import com.taotao.cloud.flink.doe.beans.OrdersBean;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;

/**
 * @since: 2024/1/3
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class Function01Connect02 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        see.setParallelism(1);

        DataStreamSource<OrdersBean> ds1 =
                see.fromElements(
                        new OrdersBean(1, "zss", "bj", 100, 1000L),
                        new OrdersBean(2, "zss", "bj", 100, 1000L),
                        new OrdersBean(3, "zss", "bj", 100, 1000L),
                        new OrdersBean(4, "zss", "bj", 100, 1000L));

        DataStreamSource<HeroBean> ds2 =
                see.fromElements(
                        new HeroBean(1, "zs", 99),
                        new HeroBean(1, "zs", 99),
                        new HeroBean(1, "zs", 99),
                        new HeroBean(1, "zs", 99));

        ds1.connect(ds2)
                .keyBy("city", "name")
                .map(
                        new RichCoMapFunction<OrdersBean, HeroBean, String>() {
                            ValueState<String> state;
                            String str;

                            @Override
                            public void open(Configuration parameters) throws Exception {

                                state =
                                        getRuntimeContext()
                                                .getState(
                                                        new ValueStateDescriptor<String>(
                                                                "v", String.class, "doe"));
                            }

                            @Override
                            public String map1(OrdersBean value) throws Exception {
                                return value.toString() + state.value();
                            }

                            @Override
                            public String map2(HeroBean value) throws Exception {
                                return value.toString() + state.value();
                            }
                        })
                .print();

        see.execute();
    }
}
