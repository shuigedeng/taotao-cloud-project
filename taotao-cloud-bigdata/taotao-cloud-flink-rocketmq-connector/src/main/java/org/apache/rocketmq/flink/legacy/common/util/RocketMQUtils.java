/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.flink.legacy.common.util;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class RocketMQUtils {
    private static final Logger log = LoggerFactory.getLogger(RocketMQUtils.class);

    public static int getInteger(Properties props, String key, int defaultValue) {
        return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public static long getLong(Properties props, String key, long defaultValue) {
        return Long.parseLong(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        return Boolean.parseBoolean(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public static AccessChannel getAccessChannel(
            Properties props, String key, AccessChannel defaultValue) {
        return AccessChannel.valueOf(props.getProperty(key, String.valueOf(defaultValue)));
    }

    public static String getInstanceName(String... args) {
        if (null != args && args.length > 0) {
            return String.join("_", args);
        }
        return ManagementFactory.getRuntimeMXBean().getName() + "_" + System.nanoTime();
    }

    /**
     * Average Hashing queue algorithm Refer:
     * org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely
     */
    public static List<MessageQueue> allocate(
            Collection<MessageQueue> mqSet, int numberOfParallelTasks, int indexOfThisTask) {
        ArrayList<MessageQueue> mqAll = new ArrayList<>(mqSet);
        Collections.sort(mqAll);
        List<MessageQueue> result = new ArrayList<>();
        int mod = mqAll.size() % numberOfParallelTasks;
        int averageSize =
                mqAll.size() <= numberOfParallelTasks
                        ? 1
                        : (mod > 0 && indexOfThisTask < mod
                                ? mqAll.size() / numberOfParallelTasks + 1
                                : mqAll.size() / numberOfParallelTasks);
        int startIndex =
                (mod > 0 && indexOfThisTask < mod)
                        ? indexOfThisTask * averageSize
                        : indexOfThisTask * averageSize + mod;
        int range = Math.min(averageSize, mqAll.size() - startIndex);
        for (int i = 0; i < range; i++) {
            result.add(mqAll.get((startIndex + i) % mqAll.size()));
        }
        return result;
    }

    public static Map<MessageQueue, Long> initOffsets(
            List<MessageQueue> messageQueues,
            DefaultMQPullConsumer consumer,
            StartupMode startMode,
            OffsetResetStrategy offsetResetStrategy,
            long specificTimeStamp,
            Map<MessageQueue, Long> specificStartupOffsets)
            throws MQClientException {
        Map<MessageQueue, Long> offsetTable = new ConcurrentHashMap<>();
        for (MessageQueue mq : messageQueues) {
            long offset;
            switch (startMode) {
                case LATEST:
                    offset = consumer.maxOffset(mq);
                    break;
                case EARLIEST:
                    offset = consumer.minOffset(mq);
                    break;
                case GROUP_OFFSETS:
                    offset = consumer.fetchConsumeOffset(mq, false);
                    // the min offset return if consumer group first join,return a negative number
                    // if catch exception when fetch from broker.
                    // If you want consumer from earliest,please use OffsetResetStrategy.EARLIEST
                    if (offset <= 0) {
                        switch (offsetResetStrategy) {
                            case LATEST:
                                offset = consumer.maxOffset(mq);
                                log.info(
                                        "current consumer thread:{} has no committed offset,use Strategy:{} instead",
                                        mq,
                                        offsetResetStrategy);
                                break;
                            case EARLIEST:
                                log.info(
                                        "current consumer thread:{} has no committed offset,use Strategy:{} instead",
                                        mq,
                                        offsetResetStrategy);
                                offset = consumer.minOffset(mq);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case TIMESTAMP:
                    offset = consumer.searchOffset(mq, specificTimeStamp);
                    break;
                case SPECIFIC_OFFSETS:
                    if (specificStartupOffsets == null) {
                        throw new RuntimeException(
                                "StartMode is specific_offsets.But none offsets has been specified");
                    }
                    Long specificOffset = specificStartupOffsets.get(mq);
                    if (specificOffset != null) {
                        offset = specificOffset;
                    } else {
                        offset = consumer.fetchConsumeOffset(mq, false);
                    }
                    break;
                default:
                    throw new IllegalArgumentException(
                            "current startMode is not supported" + startMode);
            }
            log.info(
                    "current consumer queue:{} start from offset of: {}",
                    mq.getBrokerName() + "-" + mq.getQueueId(),
                    offset);
            offsetTable.put(mq, offset);
        }
        return offsetTable;
    }
}
