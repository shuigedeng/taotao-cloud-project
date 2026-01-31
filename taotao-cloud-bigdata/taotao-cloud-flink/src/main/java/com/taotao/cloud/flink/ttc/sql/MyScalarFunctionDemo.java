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

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.InputGroup;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class MyScalarFunctionDemo {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> sensorDS =
                env.fromElements(
                        new WaterSensor("s1", 1L, 1),
                        new WaterSensor("s1", 2L, 2),
                        new WaterSensor("s2", 2L, 2),
                        new WaterSensor("s3", 3L, 3),
                        new WaterSensor("s3", 4L, 4));

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        Table sensorTable = tableEnv.fromDataStream(sensorDS);
        tableEnv.createTemporaryView("sensor", sensorTable);

        // TODO 2.注册函数
        tableEnv.createTemporaryFunction("HashFunction", HashFunction.class);

        // TODO 3.调用 自定义函数
        // 3.1 sql用法
        //        tableEnv.sqlQuery("select HashFunction(id) from sensor")
        //                .execute()  // 调用了 sql的execute，就不需要 env.execute()
        //                .print();

        // 3.2 table api用法
        sensorTable.select(call("HashFunction", $("id"))).execute().print();
    }

    // TODO 1.定义 自定义函数的实现类
    /**
     * HashFunction
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    public static class HashFunction extends ScalarFunction {

        // 接受任意类型的输入，返回 INT型输出
        public int eval( @DataTypeHint(inputGroup = InputGroup.ANY) Object o ) {
            return o.hashCode();
        }
    }
}
