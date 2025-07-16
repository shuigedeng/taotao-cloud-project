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

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
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
 *      lambda表达式实现map算子中的计算逻辑
 *      如果接口中只有一个方法可以使用 lambda
 */
public class Function01Map04 {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 8888);
        StreamExecutionEnvironment see =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        // Lambda
        // 单并行逐个接收
        DataStreamSource<String> ds = see.socketTextStream("doe01", 8899);
        // 接收数据  将数据封装成自定义元组
        /**
         * 使用lambda表达式实现计算逻辑时, 返回值的类型推断不够完善
         * 返回值的时候容易出现异常问题
         *  方案1 : 使用new xxFunction    有复杂的数据封装
         *  方案2 : 人为的定义返回值类型    简单的处理可以使用lambda
         */
        SingleOutputStreamOperator<Tuple3<String, String, String>> res =
                ds.map(
                                line -> {
                                    Tuple3<String, String, String> tp3 = null;
                                    try {
                                        String[] arr = line.split(",");
                                        tp3 = Tuple3.of(arr[0], arr[1], arr[2]);
                                    } catch (Exception e) {
                                        tp3 = Tuple3.of("", "", "");
                                    }
                                    return tp3;
                                })
                        .returns(
                                TypeInformation.of(
                                        new TypeHint<Tuple3<String, String, String>>() {}));

        // .returns(HeroBean.class)

        // .returns(new TypeHint<Tuple3<String, String, String>>() {
        //  }) ;

        //  .returns(TypeInformation.of(new TypeHint<Tuple3<String, String, String>>() {
        //  })) ;

        res.print();

        see.execute();
    }
}
