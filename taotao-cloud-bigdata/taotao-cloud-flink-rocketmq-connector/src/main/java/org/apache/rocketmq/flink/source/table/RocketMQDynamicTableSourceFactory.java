/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.flink.source.table;

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;
import org.apache.rocketmq.flink.source.util.RocketMQOptionsUtil;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.utils.TableSchemaUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.flink.table.factories.FactoryUtil.createTableFactoryHelper;
import static org.apache.rocketmq.flink.common.RocketMQOptions.CONSUMER_GROUP;
import static org.apache.rocketmq.flink.common.RocketMQOptions.NAME_SERVER_ADDRESS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_ACCESS_KEY;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_COLUMN_ERROR_DEBUG;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_COMMIT_OFFSET_AUTO;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_ENCODING;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_END_TIME_STAMP;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_FIELD_DELIMITER;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_LENGTH_CHECK;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_LINE_DELIMITER;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_PARTITION_DISCOVERY_INTERVAL_MS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_OFFSET_RESET_STRATEGY;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SECRET_KEY;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SQL;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_TAG;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_USE_NEW_API;
import static org.apache.rocketmq.flink.common.RocketMQOptions.TOPIC;
import static org.apache.rocketmq.flink.source.util.RocketMQOptionsUtil.validateStartUpMode;

/**
 * Defines the {@link DynamicTableSourceFactory} implementation to create {@link
 * RocketMQScanTableSource}.
 */
public class RocketMQDynamicTableSourceFactory implements DynamicTableSourceFactory {

    @Override
    public String factoryIdentifier() {
        return "rocketmq";
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> requiredOptions = new HashSet<>();
        requiredOptions.add(TOPIC);
        requiredOptions.add(CONSUMER_GROUP);
        requiredOptions.add(NAME_SERVER_ADDRESS);
        return requiredOptions;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        Set<ConfigOption<?>> optionalOptions = new HashSet<>();
        optionalOptions.add(OPTIONAL_TAG);
        optionalOptions.add(OPTIONAL_SQL);
        optionalOptions.add(OPTIONAL_END_TIME_STAMP);
        optionalOptions.add(OPTIONAL_PARTITION_DISCOVERY_INTERVAL_MS);
        optionalOptions.add(OPTIONAL_USE_NEW_API);
        optionalOptions.add(OPTIONAL_ENCODING);
        optionalOptions.add(OPTIONAL_FIELD_DELIMITER);
        optionalOptions.add(OPTIONAL_LINE_DELIMITER);
        optionalOptions.add(OPTIONAL_COLUMN_ERROR_DEBUG);
        optionalOptions.add(OPTIONAL_LENGTH_CHECK);
        optionalOptions.add(OPTIONAL_ACCESS_KEY);
        optionalOptions.add(OPTIONAL_SECRET_KEY);
        optionalOptions.add(OPTIONAL_SCAN_STARTUP_MODE);
        optionalOptions.add(OPTIONAL_SCAN_OFFSET_RESET_STRATEGY);
        optionalOptions.add(OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS);
        optionalOptions.add(OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS);
        optionalOptions.add(OPTIONAL_COMMIT_OFFSET_AUTO);
        return optionalOptions;
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        FactoryUtil.TableFactoryHelper helper = createTableFactoryHelper(this, context);
        helper.validate();
        Map<String, String> rawProperties = context.getCatalogTable().getOptions();
        Configuration configuration = Configuration.fromMap(rawProperties);
        String topic = configuration.getString(TOPIC);
        String consumerGroup = configuration.getString(CONSUMER_GROUP);
        String nameServerAddress = configuration.getString(NAME_SERVER_ADDRESS);
        String tag = configuration.getString(OPTIONAL_TAG);
        String sql = configuration.getString(OPTIONAL_SQL);
        validateStartUpMode(configuration);
        String accessKey = configuration.getString(OPTIONAL_ACCESS_KEY);
        String secretKey = configuration.getString(OPTIONAL_SECRET_KEY);
        long stopInMs = configuration.getLong(OPTIONAL_END_TIME_STAMP);
        long partitionDiscoveryIntervalMs =
                configuration.getLong(OPTIONAL_PARTITION_DISCOVERY_INTERVAL_MS);
        boolean useNewApi = configuration.getBoolean(OPTIONAL_USE_NEW_API);
        DescriptorProperties descriptorProperties = new DescriptorProperties();
        descriptorProperties.putProperties(rawProperties);
        TableSchema physicalSchema =
                TableSchemaUtils.getPhysicalSchema(context.getCatalogTable().getSchema());
        descriptorProperties.putTableSchema("schema", physicalSchema);
        OffsetResetStrategy offsetResetStrategy =
                configuration.get(OPTIONAL_SCAN_OFFSET_RESET_STRATEGY);
        long consumerOffsetTimestamp =
                configuration.getLong(OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS);
        Map<MessageQueue, Long> specificOffsets = new HashMap<>();
        StartupMode startupMode = configuration.get(OPTIONAL_SCAN_STARTUP_MODE);
        if (startupMode.equals(StartupMode.SPECIFIC_OFFSETS)) {
            String specificOffsetsStr =
                    configuration.getString(OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS);
            specificOffsets = RocketMQOptionsUtil.parseSpecificOffsets(specificOffsetsStr, topic);
        }
        boolean commitOffsetAuto = configuration.getBoolean(OPTIONAL_COMMIT_OFFSET_AUTO);
        return new RocketMQScanTableSource(
                descriptorProperties,
                physicalSchema,
                topic,
                consumerGroup,
                nameServerAddress,
                accessKey,
                secretKey,
                tag,
                sql,
                stopInMs,
                useNewApi,
                partitionDiscoveryIntervalMs,
                startupMode,
                offsetResetStrategy,
                specificOffsets,
                consumerOffsetTimestamp,
                commitOffsetAuto);
    }
}
