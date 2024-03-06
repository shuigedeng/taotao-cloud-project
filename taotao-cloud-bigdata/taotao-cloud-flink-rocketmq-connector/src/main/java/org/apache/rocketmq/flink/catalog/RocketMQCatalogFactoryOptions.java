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

package org.apache.rocketmq.flink.catalog;

import org.apache.rocketmq.flink.common.constant.SchemaRegistryConstant;
import org.apache.rocketmq.flink.legacy.RocketMQConfig;

import org.apache.flink.annotation.Internal;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.table.catalog.CommonCatalogOptions;

/** {@link ConfigOption}s for {@link RocketMQCatalog}. */
@Internal
public final class RocketMQCatalogFactoryOptions {

    public static final String IDENTIFIER = "rocketmq_catalog";

    public static final ConfigOption<String> DEFAULT_DATABASE =
            ConfigOptions.key(CommonCatalogOptions.DEFAULT_DATABASE_KEY)
                    .stringType()
                    .defaultValue(RocketMQCatalog.DEFAULT_DB);

    public static final ConfigOption<String> NAME_SERVER_ADDR =
            ConfigOptions.key(RocketMQConfig.NAME_SERVER_ADDR)
                    .stringType()
                    .defaultValue("http://localhost:9876")
                    .withDescription("Required rocketmq name server address");

    public static final ConfigOption<String> SCHEMA_REGISTRY_BASE_URL =
            ConfigOptions.key(SchemaRegistryConstant.SCHEMA_REGISTRY_BASE_URL_KEY)
                    .stringType()
                    .defaultValue(SchemaRegistryConstant.SCHEMA_REGISTRY_BASE_URL)
                    .withDescription("Required schema registry server address");

    private RocketMQCatalogFactoryOptions() {}
}
