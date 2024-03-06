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

package org.apache.rocketmq.flink.legacy.common.config;

/** RocketMQ startup mode. */
public enum StartupMode {
    EARLIEST("earliest-offset", "Start from the earliest offset possible."),
    LATEST("latest-offset", "Start from the latest offset."),
    GROUP_OFFSETS(
            "group-offsets",
            "Start from committed offsets in brokers of a specific consumer group."),
    TIMESTAMP("timestamp", "Start from user-supplied timestamp for each message queue."),
    SPECIFIC_OFFSETS(
            "specific-offsets",
            "Start from user-supplied specific offsets for each message queue.");

    private final String value;
    private final String description;

    StartupMode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }
}
