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
 * IntervalLeftJoinDemo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class IntervalLeftJoinDemo {

    public static void main( String[] args ) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(settings);
        tableEnv.executeSql(
                "CREATE TABLE show_log (\n"
                        + "    log_id BIGINT,\n"
                        + "    show_params STRING,\n"
                        + "    row_time AS CURRENT_TIMESTAMP,\n"
                        + "    WATERMARK FOR row_time AS row_time\n"
                        + ") WITH (\n"
                        + "  'connector' = 'datagen',\n"
                        + "  'rows-per-second' = '1',\n"
                        + "  'fields.show_params.length' = '1',\n"
                        + "  'fields.log_id.min' = '1',\n"
                        + "  'fields.log_id.max' = '10'\n"
                        + ")");

        tableEnv.executeSql(
                "CREATE TABLE click_log (\n"
                        + "    log_id BIGINT,\n"
                        + "    click_params STRING,\n"
                        + "    row_time AS CURRENT_TIMESTAMP,\n"
                        + "    WATERMARK FOR row_time AS row_time\n"
                        + ")\n"
                        + "WITH (\n"
                        + "  'connector' = 'datagen',\n"
                        + "  'rows-per-second' = '1',\n"
                        + "  'fields.click_params.length' = '1',\n"
                        + "  'fields.log_id.min' = '1',\n"
                        + "  'fields.log_id.max' = '10'\n"
                        + ")");

        tableEnv.executeSql(
                "CREATE TABLE sink_table (\n"
                        + "    s_id BIGINT,\n"
                        + "    s_params STRING,\n"
                        + "    c_id BIGINT,\n"
                        + "    c_params STRING\n"
                        + ") WITH (\n"
                        + "  'connector' = 'print'\n"
                        + ")");

        tableEnv.executeSql(
                "INSERT INTO sink_table\n"
                        + "SELECT\n"
                        + "    show_log.log_id as s_id,\n"
                        + "    show_log.show_params as s_params,\n"
                        + "    click_log.log_id as c_id,\n"
                        + "    click_log.click_params as c_params\n"
                        + "FROM show_log LEFT JOIN click_log ON show_log.log_id = click_log.log_id\n"
                        + "AND show_log.row_time BETWEEN click_log.row_time - INTERVAL '5' SECOND AND click_log.row_time + INTERVAL '5' SECOND");
    }
}
