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

package org.apache.rocketmq.flink.legacy.sourceFunction;

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.RocketMQConfig;
import org.apache.rocketmq.flink.legacy.RocketMQSourceFunction;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;
import org.apache.rocketmq.flink.legacy.common.serialization.SimpleStringDeserializationSchema;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.rocketmq.flink.legacy.RocketMQConfig.DEFAULT_CONSUMER_TAG;
import static org.apache.rocketmq.flink.legacy.common.util.TestUtils.getFieldValue;
import static org.apache.rocketmq.flink.legacy.common.util.TestUtils.setFieldValue;
import static org.junit.Assert.assertEquals;

/** Tests for {@link RocketMQSourceFunction}. */
public class RocketMQSourceFunctionTest {

    @Test
    public void testSetStartupMode() throws NoSuchFieldException, IllegalAccessException {
        RocketMQSourceFunction<String> source =
                new RocketMQSourceFunction<>(
                        new SimpleStringDeserializationSchema(), new Properties());
        assertEquals(StartupMode.GROUP_OFFSETS, getFieldValue(source, "startMode"));
        source.setStartFromEarliest();
        assertEquals(StartupMode.EARLIEST, getFieldValue(source, "startMode"));
        source.setStartFromLatest();
        assertEquals(StartupMode.LATEST, getFieldValue(source, "startMode"));
        source.setStartFromTimeStamp(0L);
        assertEquals(StartupMode.TIMESTAMP, getFieldValue(source, "startMode"));
        source.setStartFromSpecificOffsets(null);
        assertEquals(StartupMode.SPECIFIC_OFFSETS, getFieldValue(source, "startMode"));
        source.setStartFromGroupOffsets();
        assertEquals(StartupMode.GROUP_OFFSETS, getFieldValue(source, "startMode"));
        assertEquals(OffsetResetStrategy.LATEST, getFieldValue(source, "offsetResetStrategy"));
        source.setStartFromGroupOffsets(OffsetResetStrategy.EARLIEST);
        assertEquals(OffsetResetStrategy.EARLIEST, getFieldValue(source, "offsetResetStrategy"));
    }

    @Test
    public void testRestartFromCheckpoint() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(RocketMQConfig.CONSUMER_GROUP, "${ConsumerGroup}");
        properties.setProperty(RocketMQConfig.CONSUMER_TOPIC, "${SourceTopic}");
        properties.setProperty(RocketMQConfig.CONSUMER_TAG, DEFAULT_CONSUMER_TAG);
        RocketMQSourceFunction<String> source =
                new RocketMQSourceFunction<>(new SimpleStringDeserializationSchema(), properties);
        source.setStartFromLatest();
        setFieldValue(source, "restored", true);
        HashMap<MessageQueue, Long> map = new HashMap<>();
        map.put(new MessageQueue("tpc", "broker-0", 0), 20L);
        map.put(new MessageQueue("tpc", "broker-0", 1), 21L);
        map.put(new MessageQueue("tpc", "broker-1", 0), 30L);
        map.put(new MessageQueue("tpc", "broker-1", 1), 31L);
        setFieldValue(source, "restoredOffsets", map);
        setFieldValue(source, "offsetTable", new ConcurrentHashMap<>());
        source.initOffsetTableFromRestoredOffsets(new ArrayList<>(map.keySet()));
        Map<MessageQueue, Long> offsetTable = (Map) getFieldValue(source, "offsetTable");
        for (Map.Entry<MessageQueue, Long> entry : map.entrySet()) {
            assertEquals(offsetTable.containsKey(entry.getKey()), true);
            assertEquals(offsetTable.containsValue(entry.getValue()), true);
        }
    }
}
