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

package com.taotao.cloud.flink.flink.part11_flink_join;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * RegularInnerJoinDemo
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class RegularInnerJoinDemo {

    public static void main( String[] args ) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(settings);
        tableEnv.executeSql(
                "CREATE TABLE show_log_table ("
                        + "`log_id` BIGINT,"
                        + "`show_params` STRING"
                        + ") WITH ("
                        + "'connector' = 'datagen',"
                        + "'rows-per-second' = '2',"
                        + "'fields.show_params.length' = '1',"
                        + "'fields.log_id.min' = '1',"
                        + "'fields.log_id.max' = '100'"
                        + ")");

        tableEnv.executeSql(
                "CREATE TABLE click_log_table ("
                        + "`log_id` BIGINT,"
                        + "`click_params` STRING"
                        + ")"
                        + "WITH ("
                        + "'connector' = 'datagen',"
                        + "'rows-per-second' = '2',"
                        + "'fields.click_params.length' = '1',"
                        + "'fields.log_id.min' = '1',"
                        + "'fields.log_id.max' = '10'"
                        + ");");

        tableEnv.executeSql(
                "CREATE TABLE sink_table ("
                        + "`s_id` BIGINT,"
                        + "`s_params` STRING,"
                        + "`c_id` BIGINT,"
                        + "`c_params` STRING"
                        + ") WITH ("
                        + "'connector' = 'print'"
                        + ");");

        // INNER JOIN
        tableEnv.executeSql(
                "INSERT INTO sink_table SELECT show_log_table.`log_id` as s_id, show_log_table.`show_params` as s_params, click_log_table.`log_id` as c_id, click_log_table.`click_params` as c_params FROM show_log_table INNER JOIN click_log_table ON show_log_table.`log_id` = click_log_table.`log_id`;");
    }
}
