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

package com.taotao.cloud.flink.demo;

import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.core.table.sink.JdbcDynamicTableSink;
import org.apache.flink.connector.jdbc.dialect.JdbcDialect;
import org.apache.flink.table.api.ValidationException;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.legacy.api.TableSchema;
import org.apache.flink.table.types.DataType;

public class JdbcTableSinkWithFilterExample {

    public static DynamicTableSink createJdbcTableSink(
            String[] fieldNames,
            DataType[] fieldTypes,
            String sinkDialectName,
            String[] keyFields,
            String[] partitionFields,
            String[] staticPartitions,
            String[] computedColumns,
            String[] watermark) {
        JdbcDialect jdbcDialect =
                JdbcDialects.get(sinkDialectName)
                        .orElseThrow(
                                () ->
                                        new ValidationException(
                                                "Unsupported SQL dialect: " + sinkDialectName));

        // 定义表的schema
        TableSchema schema = TableSchema.builder().fields(fieldNames, fieldTypes).build();

        // 设置JDBC执行选项
        JdbcExecutionOptions executionOptions =
                JdbcExecutionOptions.builder()
                        .withBatchSize(1000)
                        .withBatchIntervalMs(100L)
                        .build();

        // 创建JDBC TableSink
        JdbcDynamicTableSink jdbcDynamicTableSink =
                JdbcDynamicTableSink.builder()
                        .setDriverName(jdbcDialect.getDriverClass())
                        .setDBUrl("jdbc:yourdatabase://host:port/database")
                        .setUsername("username")
                        .setPassword("password")
                        .setTableName("your_table_name")
                        .setSchema(schema)
                        .setExecutionOptions(executionOptions)
                        .build();

        // 设置过滤条件
        jdbcDynamicTableSink.setPartitionOption(JdbcExecutionOptions.PARTITION_KEY, keyFields);

        jdbcDynamicTableSink.setPartitionOption(JdbcExecutionOptions.PARTITION_COPY_OPTION, "true");

        jdbcDynamicTableSink.setPartitionOption(JdbcExecutionOptions.MAX_RETRIES, "3");

        // 这里的filter是假设你想要设置的过滤条件，例如'id > 100'
        jdbcDynamicTableSink.setTableOption(
                JdbcConnectorOptions.TABLE_EXECUTION_CONDITION, "id > 100");

        return jdbcDynamicTableSink;
    }
}
