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

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.flink.doe.beans.HeroBean;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/28
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 * 接收数据
 *      将接收的数据组织成Bean
 */
public class Function01Map02 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        // Lambda
        // 单并行逐个接收
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 接收数据  将数据封装成自定义Bean
        SingleOutputStreamOperator<HeroBean> heros =
                ds.map(
                        new MapFunction<String, HeroBean>() {
                            @Override
                            public HeroBean map(String value) throws Exception {

                                HeroBean heroBean = null;
                                try { // 实际生产中 将自己的逻辑代码try起来 保证程序者正常运行
                                    heroBean = JSON.parseObject(value, HeroBean.class);
                                } catch (Exception e) {
                                    // e.printStackTrace();
                                    // heroBean = new HeroBean() ;
                                }
                                // 注意返回的结果数据 , 在实时处理的过程中会被后续的处理 , 比如进行持久化等
                                // 返回的数据对象不能为 null  否则null指针异常
                                return heroBean;
                            }
                        });
        heros.print();
        see.execute();
    }
}
