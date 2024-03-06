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
import org.apache.rocketmq.flink.common.RocketMQOptions;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.ValidationException;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS;
import static org.apache.rocketmq.flink.common.RocketMQOptions.TOPIC;

/** util for {@link RocketMQOptions} */
public class RocketMQOptionsUtil {

    private static final String BROKER = "broker";
    private static final String QUEUE = "queue";
    private static final String OFFSET = "offset";

    /**
     * Parse SpecificOffsets String to Map
     *
     * @param specificOffsetsStr scan.startup.specific-offsets =
     *     broker:a,queue:0,offset:123;broker:a,queue:1,offset:38
     * @param topicName topic name
     * @return SpecificOffsets with Map format, key is messageQueue, and value is offset
     */
    public static Map<MessageQueue, Long> parseSpecificOffsets(
            String specificOffsetsStr, String topicName) {
        final Map<MessageQueue, Long> offsetMap = new HashMap<>();
        final String[] pairs = specificOffsetsStr.split(";");
        final String validationExceptionMessage =
                String.format(
                        "Invalid properties '%s' should follow the format "
                                + "'broker:a,queue:0,offset:123;broker:a,queue:1,offset:38', but is '%s'.",
                        OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS.key(), specificOffsetsStr);

        if (pairs.length == 0) {
            throw new ValidationException(validationExceptionMessage);
        }

        for (String pair : pairs) {
            if (StringUtils.isBlank(pair)) {
                throw new ValidationException(validationExceptionMessage);
            }

            final String[] kv = pair.split(",");
            if (kv.length != 3
                    || !kv[0].startsWith(BROKER + ':')
                    || !kv[1].startsWith(QUEUE + ':')
                    || !kv[2].startsWith(OFFSET + ':')) {
                throw new ValidationException(validationExceptionMessage);
            }

            String brokerValue = kv[0].substring(kv[0].indexOf(":") + 1);
            String queueValue = kv[1].substring(kv[1].indexOf(":") + 1);
            String offsetValue = kv[2].substring(kv[2].indexOf(":") + 1);
            try {
                final Integer queue = Integer.valueOf(queueValue);
                final Long offset = Long.valueOf(offsetValue);
                MessageQueue messageQueue = new MessageQueue(topicName, brokerValue, queue);
                offsetMap.put(messageQueue, offset);
            } catch (NumberFormatException e) {
                throw new ValidationException(validationExceptionMessage, e);
            }
        }
        return offsetMap;
    }

    /** validate properties when startUpMode is set */
    public static void validateStartUpMode(Configuration configuration) {
        configuration
                .getOptional(OPTIONAL_SCAN_STARTUP_MODE)
                .ifPresent(
                        mode -> {
                            switch (mode) {
                                case SPECIFIC_OFFSETS:
                                    if (!configuration
                                            .getOptional(OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS)
                                            .isPresent()) {
                                        throw new ValidationException(
                                                String.format(
                                                        "'%s' is required in '%s' startup mode"
                                                                + " but missing.",
                                                        OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS
                                                                .key(),
                                                        StartupMode.SPECIFIC_OFFSETS));
                                    }
                                    String specificOffsets =
                                            configuration.getString(
                                                    OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS);
                                    String topic = configuration.getString(TOPIC);
                                    RocketMQOptionsUtil.parseSpecificOffsets(
                                            specificOffsets, topic);
                                    break;
                                case TIMESTAMP:
                                    if (!configuration
                                            .getOptional(OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS)
                                            .isPresent()) {
                                        throw new ValidationException(
                                                String.format(
                                                        "'%s' is required in '%s' startup mode"
                                                                + " but missing.",
                                                        OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS
                                                                .key(),
                                                        StartupMode.TIMESTAMP));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        });
    }
}
