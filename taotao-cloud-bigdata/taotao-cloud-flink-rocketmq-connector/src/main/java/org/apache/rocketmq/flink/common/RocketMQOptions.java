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

package org.apache.rocketmq.flink.common;

import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

/** Includes config options of RocketMQ connector type. */
public class RocketMQOptions {

    public static final ConfigOption<String> TOPIC =
            ConfigOptions.key("topic").stringType().noDefaultValue();

    public static final ConfigOption<String> CONSUMER_GROUP =
            ConfigOptions.key("consumerGroup").stringType().noDefaultValue();

    public static final ConfigOption<String> PRODUCER_GROUP =
            ConfigOptions.key("producerGroup").stringType().noDefaultValue();

    public static final ConfigOption<String> NAME_SERVER_ADDRESS =
            ConfigOptions.key("nameServerAddress").stringType().noDefaultValue();

    public static final ConfigOption<String> OPTIONAL_TAG =
            ConfigOptions.key("tag").stringType().noDefaultValue();

    public static final ConfigOption<String> OPTIONAL_SQL =
            ConfigOptions.key("sql").stringType().noDefaultValue();

    public static final ConfigOption<Long> OPTIONAL_END_TIME_STAMP =
            ConfigOptions.key("endTimestamp").longType().defaultValue(Long.MAX_VALUE);

    public static final ConfigOption<Long> OPTIONAL_PARTITION_DISCOVERY_INTERVAL_MS =
            ConfigOptions.key("partitionDiscoveryIntervalMs").longType().defaultValue(-1L);

    public static final ConfigOption<Boolean> OPTIONAL_USE_NEW_API =
            ConfigOptions.key("useNewApi").booleanType().defaultValue(true);

    public static final ConfigOption<String> OPTIONAL_ENCODING =
            ConfigOptions.key("encoding").stringType().defaultValue("UTF-8");

    public static final ConfigOption<String> OPTIONAL_FIELD_DELIMITER =
            ConfigOptions.key("fieldDelimiter").stringType().defaultValue("\u0001");

    public static final ConfigOption<String> OPTIONAL_LINE_DELIMITER =
            ConfigOptions.key("lineDelimiter").stringType().defaultValue("\n");

    public static final ConfigOption<Boolean> OPTIONAL_COLUMN_ERROR_DEBUG =
            ConfigOptions.key("columnErrorDebug").booleanType().defaultValue(true);

    public static final ConfigOption<String> OPTIONAL_LENGTH_CHECK =
            ConfigOptions.key("lengthCheck").stringType().defaultValue("NONE");

    public static final ConfigOption<Integer> OPTIONAL_WRITE_RETRY_TIMES =
            ConfigOptions.key("retryTimes").intType().defaultValue(10);

    public static final ConfigOption<Long> OPTIONAL_WRITE_SLEEP_TIME_MS =
            ConfigOptions.key("sleepTimeMs").longType().defaultValue(5000L);

    public static final ConfigOption<Boolean> OPTIONAL_WRITE_IS_DYNAMIC_TAG =
            ConfigOptions.key("isDynamicTag").booleanType().defaultValue(false);

    public static final ConfigOption<String> OPTIONAL_WRITE_DYNAMIC_TAG_COLUMN =
            ConfigOptions.key("dynamicTagColumn").stringType().noDefaultValue();

    public static final ConfigOption<Boolean> OPTIONAL_WRITE_DYNAMIC_TAG_COLUMN_WRITE_INCLUDED =
            ConfigOptions.key("dynamicTagColumnWriteIncluded").booleanType().defaultValue(true);

    public static final ConfigOption<String> OPTIONAL_WRITE_KEY_COLUMNS =
            ConfigOptions.key("keyColumns").stringType().noDefaultValue();

    public static final ConfigOption<Boolean> OPTIONAL_WRITE_KEYS_TO_BODY =
            ConfigOptions.key("writeKeysToBody").booleanType().defaultValue(false);

    public static final ConfigOption<String> OPTIONAL_ACCESS_KEY =
            ConfigOptions.key("accessKey").stringType().noDefaultValue();

    public static final ConfigOption<String> OPTIONAL_SECRET_KEY =
            ConfigOptions.key("secretKey").stringType().noDefaultValue();

    // --------------------------------------------------------------------------------------------
    // Scan specific options
    // --------------------------------------------------------------------------------------------

    public static final ConfigOption<StartupMode> OPTIONAL_SCAN_STARTUP_MODE =
            ConfigOptions.key("scan.startup.mode")
                    .enumType(StartupMode.class)
                    .defaultValue(StartupMode.GROUP_OFFSETS)
                    .withDescription("Startup mode for RocketMQ consumer.");

    public static final ConfigOption<OffsetResetStrategy> OPTIONAL_SCAN_OFFSET_RESET_STRATEGY =
            ConfigOptions.key("scan.offsetReset.strategy")
                    .enumType(OffsetResetStrategy.class)
                    .defaultValue(OffsetResetStrategy.LATEST)
                    .withDescription(
                            "The offsetReset strategy only be used if group offsets is not found");

    public static final ConfigOption<String> OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS =
            ConfigOptions.key("scan.startup.specific-offsets")
                    .stringType()
                    .noDefaultValue()
                    .withDescription(
                            "Optional offsets used in case of \"specific-offsets\" startup mode");

    public static final ConfigOption<Long> OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS =
            ConfigOptions.key("scan.startup.timestamp-millis")
                    .longType()
                    .defaultValue(-1L)
                    .withDescription(
                            "Optional timestamp used in case of \"timestamp\" startup mode");

    public static final ConfigOption<Boolean> OPTIONAL_COMMIT_OFFSET_AUTO =
            ConfigOptions.key("commit.offset.auto")
                    .booleanType()
                    .defaultValue(false)
                    .withDescription(
                            "Commit offset immediately when each message is fetched."
                                    + "If you don't enable the flink checkpoint, make sure this option is set to true.");
}
