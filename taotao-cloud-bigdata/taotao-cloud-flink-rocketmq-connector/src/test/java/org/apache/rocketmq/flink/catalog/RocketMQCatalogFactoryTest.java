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

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.factories.FactoryUtil;

import org.junit.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RocketMQCatalogFactoryTest {

    @Test
    public void testCreateCatalog() {
        RocketMQCatalogFactory factory = new RocketMQCatalogFactory();
        FactoryUtil.DefaultCatalogContext context =
                new FactoryUtil.DefaultCatalogContext(
                        "rocketmq-catalog",
                        new HashMap<>(),
                        null,
                        this.getClass().getClassLoader());
        Catalog catalog = factory.createCatalog(context);
        assertNotNull(catalog);
    }

    @Test
    public void testFactoryIdentifier() {
        RocketMQCatalogFactory factory = new RocketMQCatalogFactory();
        assertEquals(factory.factoryIdentifier(), "rocketmq_catalog");
    }

    @Test
    public void testRequiredOptions() {
        RocketMQCatalogFactory factory = new RocketMQCatalogFactory();
        Set<ConfigOption<?>> options = factory.requiredOptions();
        assertNotNull(options);
    }

    @Test
    public void testOptionalOptions() {
        RocketMQCatalogFactory factory = new RocketMQCatalogFactory();
        Set<ConfigOption<?>> options = factory.optionalOptions();
        assertEquals(options.size(), 3);
    }
}
