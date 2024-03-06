/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.legacy.example;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class RocketMQSqlExample {
    private static String topic = "tp_test";
    private static String consumerGroup = "cg_test";
    private static String nameServerAddress = "10.13.66.140:9876";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String sqlCreate =
                String.format(
                        "CREATE TABLE rocketmq_source (\n"
                                + "  `user_id` BIGINT,\n"
                                + "  `behavior` STRING\n"
                                + ") WITH (\n"
                                + "  'connector' = 'rocketmq',\n"
                                + "  'topic' = '%s',\n"
                                + "  'consumerGroup' = '%s',\n"
                                + "  'nameServerAddress' = '%s',\n"
                                + "  'fieldDelimiter' = ' ',\n"
                                + "  'scan.startup.mode' = 'specific-offsets',\n"
                                + "  'commit.offset.auto' = 'true',\n"
                                + "  'scan.startup.specific-offsets' = 'broker:broker-a,queue:0,offset:21;broker:broker-a,queue:1,offset:14'\n"
                                + ");",
                        topic, consumerGroup, nameServerAddress);
        tableEnv.executeSql(sqlCreate);
        Table table = tableEnv.sqlQuery("select * from rocketmq_source");
        table.execute().print();
        env.execute();
    }
}
