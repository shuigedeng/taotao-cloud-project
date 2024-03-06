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
import org.apache.rocketmq.flink.legacy.RocketMQConfig;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;
import org.apache.rocketmq.flink.source.reader.deserializer.RocketMQDeserializationSchema;

import org.apache.flink.api.connector.source.Boundedness;

import org.apache.commons.lang.Validate;

import java.util.Map;
import java.util.Properties;

/**
 * The @builder class for {@link RocketMQSource}to make it easier for the users to construct a
 * {@link RocketMQSource}.
 */
public class RocketMQSourceBuilder<OUT> {
    private static final String[] REQUIRED_CONFIGS = {
        RocketMQConfig.NAME_SERVER_ADDR,
        RocketMQConfig.CONSUMER_GROUP,
        RocketMQConfig.CONSUMER_TOPIC
    };
    protected Properties props;
    private RocketMQDeserializationSchema<OUT> deserializationSchema;
    private Boundedness boundedness;
    private long consumerOffsetTimestamp;
    private long stopInMs;
    private long partitionDiscoveryIntervalMs;
    private boolean commitOffsetAuto;

    /** The startup mode for the consumer (default is {@link StartupMode#GROUP_OFFSETS}). */
    private StartupMode startMode = StartupMode.GROUP_OFFSETS;

    /**
     * If StartupMode#GROUP_OFFSETS has no commit offset.OffsetResetStrategy would offer init
     * strategy.
     */
    private OffsetResetStrategy offsetResetStrategy = OffsetResetStrategy.LATEST;

    /**
     * Specific startup offsets; only relevant when startup mode is {@link
     * StartupMode#SPECIFIC_OFFSETS}.
     */
    private Map<MessageQueue, Long> specificStartupOffsets;

    public RocketMQSourceBuilder() {
        this.props = new Properties();
        this.deserializationSchema = null;
        this.boundedness = Boundedness.CONTINUOUS_UNBOUNDED;
        this.stopInMs = RocketMQOptions.OPTIONAL_END_TIME_STAMP.defaultValue();
        this.partitionDiscoveryIntervalMs =
                RocketMQOptions.OPTIONAL_PARTITION_DISCOVERY_INTERVAL_MS.defaultValue();
        this.commitOffsetAuto = RocketMQOptions.OPTIONAL_COMMIT_OFFSET_AUTO.defaultValue();
    }

    public RocketMQSourceBuilder<OUT> setTopic(String topic) {
        return setProperty(RocketMQConfig.CONSUMER_TOPIC, topic);
    }

    public RocketMQSourceBuilder<OUT> setConsumerGroup(String consumerGroup) {
        return setProperty(RocketMQConfig.CONSUMER_GROUP, consumerGroup);
    }

    public RocketMQSourceBuilder<OUT> setNameServerAddress(String nameServerAddress) {
        return setProperty(RocketMQConfig.NAME_SERVER_ADDR, nameServerAddress);
    }

    public RocketMQSourceBuilder<OUT> setTag(String tag) {
        return setProperty(RocketMQConfig.CONSUMER_TAG, tag);
    }

    public RocketMQSourceBuilder<OUT> setSql(String sql) {
        return setProperty(RocketMQConfig.CONSUMER_SQL, sql);
    }

    public RocketMQSourceBuilder<OUT> setAccessKey(String accessKey) {
        return setProperty(RocketMQConfig.ACCESS_KEY, accessKey);
    }

    public RocketMQSourceBuilder<OUT> setSecretKey(String secretKey) {
        return setProperty(RocketMQConfig.SECRET_KEY, secretKey);
    }

    public RocketMQSourceBuilder<OUT> setDeserializer(
            RocketMQDeserializationSchema<OUT> deserializationSchema) {
        this.deserializationSchema = deserializationSchema;
        return this;
    }

    public RocketMQSourceBuilder<OUT> setStopInMs(long stopInMs) {
        this.boundedness = Boundedness.BOUNDED;
        this.stopInMs = stopInMs;
        return this;
    }

    public RocketMQSourceBuilder<OUT> setPartitionDiscoveryIntervalMs(
            long partitionDiscoveryIntervalMs) {
        this.partitionDiscoveryIntervalMs = partitionDiscoveryIntervalMs;
        return this;
    }

    public RocketMQSourceBuilder<OUT> setProperty(String key, String value) {
        props.setProperty(key, value);
        return this;
    }

    public RocketMQSourceBuilder<OUT> setProperties(Properties props) {
        this.props.putAll(props);
        return this;
    }

    /** consume from the min offset at every restart with no state */
    public RocketMQSourceBuilder<OUT> setStartFromEarliest() {
        this.startMode = StartupMode.EARLIEST;
        return this;
    }

    /** consume from the max offset of each broker's queue at every restart with no state */
    public RocketMQSourceBuilder<OUT> setStartFromLatest() {
        this.startMode = StartupMode.LATEST;
        return this;
    }

    /** consume from the closest offset */
    public RocketMQSourceBuilder<OUT> setStartFromTimeStamp(long consumerOffsetTimestamp) {
        this.startMode = StartupMode.TIMESTAMP;
        this.consumerOffsetTimestamp = consumerOffsetTimestamp;
        return this;
    }

    /** consume from the group offsets those was stored in brokers. */
    public RocketMQSourceBuilder<OUT> setStartFromGroupOffsets() {
        this.startMode = StartupMode.GROUP_OFFSETS;
        return this;
    }

    /**
     * consume from the group offsets those was stored in brokers. If there is no committed
     * offset,#{@link OffsetResetStrategy} would provide initialization policy.
     */
    public RocketMQSourceBuilder<OUT> setStartFromGroupOffsets(
            OffsetResetStrategy offsetResetStrategy) {
        this.startMode = StartupMode.GROUP_OFFSETS;
        this.offsetResetStrategy = offsetResetStrategy;
        return this;
    }

    /**
     * consume from the specific offset. Group offsets is enable while the broker didn't specify
     * offset.
     */
    public RocketMQSourceBuilder<OUT> setStartFromSpecificOffsets(
            Map<MessageQueue, Long> specificOffsets) {
        this.specificStartupOffsets = specificOffsets;
        this.startMode = StartupMode.SPECIFIC_OFFSETS;
        return this;
    }

    /**
     * If checkpoint is disabled,this option must be set to true.
     *
     * @param commitOffsetAuto
     */
    public RocketMQSourceBuilder<OUT> setCommitOffsetAuto(boolean commitOffsetAuto) {
        this.commitOffsetAuto = commitOffsetAuto;
        return this;
    }

    public RocketMQSource<OUT> build() {
        sanityCheck();
        return new RocketMQSource(
                props.getProperty(RocketMQConfig.CONSUMER_TOPIC),
                props.getProperty(RocketMQConfig.CONSUMER_GROUP),
                props.getProperty(RocketMQConfig.NAME_SERVER_ADDR),
                props.getProperty(RocketMQConfig.ACCESS_KEY),
                props.getProperty(RocketMQConfig.SECRET_KEY),
                props.getProperty(RocketMQConfig.CONSUMER_TAG),
                props.getProperty(RocketMQConfig.CONSUMER_SQL),
                stopInMs,
                partitionDiscoveryIntervalMs,
                boundedness,
                deserializationSchema,
                startMode,
                offsetResetStrategy,
                specificStartupOffsets,
                consumerOffsetTimestamp,
                commitOffsetAuto);
    }

    private void sanityCheck() {
        for (String requiredConfig : REQUIRED_CONFIGS) {
            Validate.notNull(
                    props.getProperty(requiredConfig),
                    String.format("Property %s is required but not provided", requiredConfig));
        }
    }
}
