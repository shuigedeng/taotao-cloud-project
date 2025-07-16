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

package com.taotao.cloud.flink.ttc.sql;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class MyTableAggregateFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //  姓名，分数，权重
        DataStreamSource<Integer> numDS = env.fromElements(3, 6, 12, 5, 8, 9, 4);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        Table numTable = tableEnv.fromDataStream(numDS, $("num"));

        // TODO 2.注册函数
        tableEnv.createTemporaryFunction("Top2", Top2.class);

        // TODO 3.调用 自定义函数: 只能用 Table API
        numTable.flatAggregate(call("Top2", $("num")).as("value", "rank"))
                .select($("value"), $("rank"))
                .execute()
                .print();
    }

    // TODO 1.继承 TableAggregateFunction< 返回类型，累加器类型<加权总和，权重总和> >
    // 返回类型 (数值，排名) =》 (12,1) (9,2)
    // 累加器类型 (第一大的数，第二大的数) ===》 （12,9）
    public static class Top2
            extends TableAggregateFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> {

        @Override
        public Tuple2<Integer, Integer> createAccumulator() {
            return Tuple2.of(0, 0);
        }

        /**
         * 每来一个数据调用一次，比较大小，更新 最大的前两个数到 acc中
         *
         * @param acc 累加器
         * @param num 过来的数据
         */
        public void accumulate(Tuple2<Integer, Integer> acc, Integer num) {
            if (num > acc.f0) {
                // 新来的变第一，原来的第一变第二
                acc.f1 = acc.f0;
                acc.f0 = num;
            } else if (num > acc.f1) {
                // 新来的变第二，原来的第二不要了
                acc.f1 = num;
            }
        }

        /**
         * 输出结果： （数值，排名）两条最大的
         *
         * @param acc 累加器
         * @param out 采集器<返回类型>
         */
        public void emitValue(
                Tuple2<Integer, Integer> acc, Collector<Tuple2<Integer, Integer>> out) {
            if (acc.f0 != 0) {
                out.collect(Tuple2.of(acc.f0, 1));
            }
            if (acc.f1 != 0) {
                out.collect(Tuple2.of(acc.f1, 2));
            }
        }
    }
}
