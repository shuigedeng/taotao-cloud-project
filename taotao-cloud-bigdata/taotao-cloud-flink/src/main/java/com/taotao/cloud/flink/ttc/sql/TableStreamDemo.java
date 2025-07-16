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

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class TableStreamDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> sensorDS =
                env.fromElements(
                        new WaterSensor("s1", 1L, 1),
                        new WaterSensor("s1", 2L, 2),
                        new WaterSensor("s2", 2L, 2),
                        new WaterSensor("s3", 3L, 3),
                        new WaterSensor("s3", 4L, 4));

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // TODO 1. 流转表
        Table sensorTable = tableEnv.fromDataStream(sensorDS);
        tableEnv.createTemporaryView("sensor", sensorTable);

        Table filterTable = tableEnv.sqlQuery("select id,ts,vc from sensor where ts>2");
        Table sumTable = tableEnv.sqlQuery("select id,sum(vc) from sensor group by id");

        // TODO 2. 表转流
        // 2.1 追加流
        tableEnv.toDataStream(filterTable, WaterSensor.class).print("filter");
        // 2.2 changelog流(结果需要更新)
        tableEnv.toChangelogStream(sumTable).print("sum");

        // 只要代码中调用了 DataStreamAPI，就需要 execute，否则不需要
        env.execute();
    }
}
