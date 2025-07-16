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

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SqlDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // TODO 1.创建表环境
        // 1.1 写法一：
        //        EnvironmentSettings settings = EnvironmentSettings.newInstance()
        //                .inStreamingMode()
        //                .build();
        //        StreamTableEnvironment tableEnv = TableEnvironment.create(settings);

        // 1.2 写法二
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // TODO 2.创建表
        tableEnv.executeSql(
                "CREATE TABLE source ( \n"
                        + "    id INT, \n"
                        + "    ts BIGINT, \n"
                        + "    vc INT\n"
                        + ") WITH ( \n"
                        + "    'connector' = 'datagen', \n"
                        + "    'rows-per-second'='1', \n"
                        + "    'fields.id.kind'='random', \n"
                        + "    'fields.id.min'='1', \n"
                        + "    'fields.id.max'='10', \n"
                        + "    'fields.ts.kind'='sequence', \n"
                        + "    'fields.ts.start'='1', \n"
                        + "    'fields.ts.end'='1000000', \n"
                        + "    'fields.vc.kind'='random', \n"
                        + "    'fields.vc.min'='1', \n"
                        + "    'fields.vc.max'='100'\n"
                        + ");\n");

        tableEnv.executeSql(
                "CREATE TABLE sink (\n"
                        + "    id INT, \n"
                        + "    sumVC INT \n"
                        + ") WITH (\n"
                        + "'connector' = 'print'\n"
                        + ");\n");

        // TODO 3.执行查询
        // 3.1 使用sql进行查询
        //        Table table = tableEnv.sqlQuery("select id,sum(vc) as sumVC from source where id>5
        // group by id ;");
        // 把table对象，注册成表名
        //        tableEnv.createTemporaryView("tmp", table);
        //        tableEnv.sqlQuery("select * from tmp where id > 7");

        // 3.2 用table api来查询
        Table source = tableEnv.from("source");
        Table result =
                source.where($("id").isGreater(5))
                        .groupBy($("id"))
                        .aggregate($("vc").sum().as("sumVC"))
                        .select($("id"), $("sumVC"));

        // TODO 4.输出表
        // 4.1 sql用法
        //        tableEnv.executeSql("insert into sink select * from tmp");
        // 4.2 tableapi用法
        result.executeInsert("sink");
    }
}
