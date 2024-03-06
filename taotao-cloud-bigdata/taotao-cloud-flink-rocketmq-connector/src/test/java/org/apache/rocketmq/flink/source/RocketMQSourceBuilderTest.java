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

package org.apache.rocketmq.flink.source;

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.common.RocketMQOptions;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;
import org.apache.rocketmq.flink.source.reader.deserializer.RocketMQValueOnlyDeserializationSchemaWrapper;
import org.apache.rocketmq.flink.source.reader.deserializer.SimpleStringSchema;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/** Tests for {@link RocketMQSourceBuilder}. */
public class RocketMQSourceBuilderTest {

    private RocketMQSourceBuilder<String> builder;

    @Before
    public void open() {
        builder =
                new RocketMQSourceBuilder<String>()
                        .setNameServerAddress("localhost:5789")
                        .setTopic("tp_test")
                        .setConsumerGroup("group_test")
                        .setDeserializer(
                                new RocketMQValueOnlyDeserializationSchemaWrapper<>(
                                        new SimpleStringSchema()));
    }

    @Test
    public void testPartitionDiscoverOnBoundedness() {
        final IllegalArgumentException exception =
                Assert.assertThrows(
                        IllegalArgumentException.class,
                        () ->
                                builder.setStopInMs(3000L)
                                        .setPartitionDiscoveryIntervalMs(50000L)
                                        .build());
        MatcherAssert.assertThat(
                exception.getMessage(),
                CoreMatchers.containsString("Bounded stream didn't support partitionDiscovery."));
    }

    @Test
    public void testStartFromEarliest() {
        RocketMQSource<String> source = builder.setStartFromEarliest().build();
        Assert.assertEquals(
                StartupMode.EARLIEST,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE));
        Assert.assertEquals(
                Long.MAX_VALUE,
                source.getConfiguration().getLong(RocketMQOptions.OPTIONAL_END_TIME_STAMP));
    }

    @Test
    public void testStartFromLatest() {
        RocketMQSource<String> source = builder.setStartFromLatest().build();
        Assert.assertEquals(
                StartupMode.LATEST,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE));
        Assert.assertEquals(
                Long.MAX_VALUE,
                source.getConfiguration().getLong(RocketMQOptions.OPTIONAL_END_TIME_STAMP));
    }

    @Test
    public void testStartFromTimeStamp() {
        long startFlag = 1666794040000L;
        RocketMQSource<String> source = builder.setStartFromTimeStamp(startFlag).build();
        Assert.assertEquals(
                StartupMode.TIMESTAMP,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE));
        Assert.assertEquals(
                startFlag,
                source.getConfiguration()
                        .getLong(RocketMQOptions.OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS));
        Assert.assertEquals(
                Long.MAX_VALUE,
                source.getConfiguration().getLong(RocketMQOptions.OPTIONAL_END_TIME_STAMP));
    }

    @Test
    public void testStartFromGroupOffsets() {
        RocketMQSource<String> source = builder.setStartFromGroupOffsets().build();
        Assert.assertEquals(
                StartupMode.GROUP_OFFSETS,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE));
        Assert.assertEquals(
                OffsetResetStrategy.LATEST,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_OFFSET_RESET_STRATEGY));
        Assert.assertEquals(
                OffsetResetStrategy.EARLIEST,
                builder.setStartFromGroupOffsets(OffsetResetStrategy.EARLIEST)
                        .build()
                        .getConfiguration()
                        .get(RocketMQOptions.OPTIONAL_SCAN_OFFSET_RESET_STRATEGY));
    }

    @Test
    public void StartFromSpecificOffsets() {
        HashMap<MessageQueue, Long> specificOffsets = new HashMap<>();
        specificOffsets.put(new MessageQueue("topic", "broker", 0), 1L);
        specificOffsets.put(new MessageQueue("topic", "broker", 1), 2L);
        RocketMQSource<String> source =
                builder.setStartFromSpecificOffsets(specificOffsets)
                        .setCommitOffsetAuto(true)
                        .build();
        Assert.assertEquals(
                StartupMode.SPECIFIC_OFFSETS,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE));
        Assert.assertEquals(
                OffsetResetStrategy.LATEST,
                source.getConfiguration().get(RocketMQOptions.OPTIONAL_SCAN_OFFSET_RESET_STRATEGY));
        Assert.assertEquals(
                specificOffsets.toString(),
                source.getConfiguration()
                        .get(RocketMQOptions.OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS));
    }
}
