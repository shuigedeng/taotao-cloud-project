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

package org.apache.rocketmq.flink.source.util;

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.ValidationException;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS;

/** Tests for {@link RocketMQOptionsUtil}. */
public class RocketMQOptionsUtilTest {

    @Test
    public void testParseSpecificOffsets() {
        String topic = "tp_test";
        Map<MessageQueue, Long> expect = new HashMap<>();
        expect.put(new MessageQueue(topic, "a", 0), 123L);
        expect.put(new MessageQueue(topic, "a", 1), 38L);
        String specificOffsetsStr = "broker:a,queue:0,offset:123;broker:a,queue:1,offset:38";
        Map<MessageQueue, Long> actual =
                RocketMQOptionsUtil.parseSpecificOffsets(specificOffsetsStr, topic);
        Assert.assertEquals(expect, actual);
    }

    @Test(expected = ValidationException.class)
    public void testException() {
        String topic = "tp_test";
        String specificOffsetsStr = "";
        RocketMQOptionsUtil.parseSpecificOffsets(specificOffsetsStr, topic);
    }

    @Test(expected = ValidationException.class)
    public void testException2() {
        String topic = "tp_test";
        String specificOffsetsStr = "broker:a,queue:0,offset:123;broker:a,queue:1";
        RocketMQOptionsUtil.parseSpecificOffsets(specificOffsetsStr, topic);
    }

    @Test
    public void testValidateStartUpMode() {
        // specific-offsets
        Configuration conf = new Configuration();
        conf.set(OPTIONAL_SCAN_STARTUP_MODE, StartupMode.SPECIFIC_OFFSETS);
        try {
            RocketMQOptionsUtil.validateStartUpMode(conf);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(e instanceof ValidationException);
        }
        conf.set(
                OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS,
                "broker:a,queue:0,offset:123;broker:a,queue:1,offset:38");
        RocketMQOptionsUtil.validateStartUpMode(conf);
        // timestamp
        conf.set(OPTIONAL_SCAN_STARTUP_MODE, StartupMode.TIMESTAMP);
        try {
            RocketMQOptionsUtil.validateStartUpMode(conf);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.assertTrue(e instanceof ValidationException);
        }
        conf.set(OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS, 200L);
        RocketMQOptionsUtil.validateStartUpMode(conf);
    }
}
