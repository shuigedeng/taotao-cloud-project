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
import org.apache.rocketmq.flink.source.enumerator.RocketMQSourceEnumState;
import org.apache.rocketmq.flink.source.enumerator.RocketMQSourceEnumStateSerializer;
import org.apache.rocketmq.flink.source.enumerator.RocketMQSourceEnumerator;
import org.apache.rocketmq.flink.source.reader.RocketMQPartitionSplitReader;
import org.apache.rocketmq.flink.source.reader.RocketMQRecordEmitter;
import org.apache.rocketmq.flink.source.reader.RocketMQSourceReader;
import org.apache.rocketmq.flink.source.reader.deserializer.RocketMQDeserializationSchema;
import org.apache.rocketmq.flink.source.reader.fetcher.RocketMQSourceFetcherManager;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplit;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplitSerializer;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.connector.source.Boundedness;
import org.apache.flink.api.connector.source.Source;
import org.apache.flink.api.connector.source.SourceReader;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.util.UserCodeClassLoader;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** The Source implementation of RocketMQ. */
public class RocketMQSource<OUT>
        implements Source<OUT, RocketMQPartitionSplit, RocketMQSourceEnumState>,
                ResultTypeQueryable<OUT> {
    private static final long serialVersionUID = -1L;

    private final String topic;
    private final String consumerGroup;
    private final String nameServerAddress;
    private final String tag;
    private final String sql;

    private final String accessKey;
    private final String secretKey;

    private final long stopInMs;
    private final long partitionDiscoveryIntervalMs;

    // Boundedness
    private final Boundedness boundedness;
    private final RocketMQDeserializationSchema<OUT> deserializationSchema;

    // consumer strategy
    private StartupMode startMode = StartupMode.GROUP_OFFSETS;
    private OffsetResetStrategy offsetResetStrategy = OffsetResetStrategy.LATEST;
    private final Map<MessageQueue, Long> specificOffsets;
    private long consumerOffsetTimestamp;

    private boolean commitOffsetAuto;

    public RocketMQSource(
            String topic,
            String consumerGroup,
            String nameServerAddress,
            String accessKey,
            String secretKey,
            String tag,
            String sql,
            long stopInMs,
            long partitionDiscoveryIntervalMs,
            Boundedness boundedness,
            RocketMQDeserializationSchema<OUT> deserializationSchema,
            StartupMode startMode,
            OffsetResetStrategy offsetResetStrategy,
            Map<MessageQueue, Long> specificOffsets,
            long consumerOffsetTimestamp,
            boolean commitOffsetAuto) {
        Validate.isTrue(
                !(StringUtils.isNotEmpty(tag) && StringUtils.isNotEmpty(sql)),
                "Consumer tag and sql can not set value at the same time");
        Validate.isTrue(
                !(boundedness == Boundedness.BOUNDED && partitionDiscoveryIntervalMs > 0),
                "Bounded stream didn't support partitionDiscovery."
                        + "PartitionDiscovery will hold the split reader and keep the thread running "
                        + "even though all of subtasks has emitted the last record.");
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.nameServerAddress = nameServerAddress;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.tag = StringUtils.isEmpty(tag) ? RocketMQConfig.DEFAULT_CONSUMER_TAG : tag;
        this.sql = sql;
        this.stopInMs = stopInMs;
        this.partitionDiscoveryIntervalMs = partitionDiscoveryIntervalMs;
        this.boundedness = boundedness;
        this.deserializationSchema = deserializationSchema;
        if (null != startMode) {
            this.startMode = startMode;
        }
        if (null != offsetResetStrategy) {
            this.offsetResetStrategy = offsetResetStrategy;
        }
        this.specificOffsets = specificOffsets == null ? new HashMap<>() : specificOffsets;
        this.consumerOffsetTimestamp = consumerOffsetTimestamp;
        this.commitOffsetAuto = commitOffsetAuto;
    }

    @Override
    public Boundedness getBoundedness() {
        return this.boundedness;
    }

    @Override
    public SourceReader<OUT, RocketMQPartitionSplit> createReader(SourceReaderContext readerContext)
            throws Exception {
        FutureCompletingBlockingQueue<RecordsWithSplitIds<Tuple3<OUT, Long, Long>>> elementsQueue =
                new FutureCompletingBlockingQueue<>();
        deserializationSchema.open(
                new DeserializationSchema.InitializationContext() {
                    @Override
                    public MetricGroup getMetricGroup() {
                        return readerContext.metricGroup();
                    }

                    @Override
                    public UserCodeClassLoader getUserCodeClassLoader() {
                        return null;
                    }
                });

        Supplier<SplitReader<Tuple3<OUT, Long, Long>, RocketMQPartitionSplit>> splitReaderSupplier =
                () ->
                        new RocketMQPartitionSplitReader<>(
                                topic,
                                consumerGroup,
                                nameServerAddress,
                                accessKey,
                                secretKey,
                                tag,
                                sql,
                                deserializationSchema,
                                readerContext,
                                commitOffsetAuto);
        RocketMQRecordEmitter<OUT> recordEmitter = new RocketMQRecordEmitter<>();

        return new RocketMQSourceReader<>(
                elementsQueue,
                new RocketMQSourceFetcherManager<>(
                        elementsQueue, splitReaderSupplier, (ignore) -> {}),
                recordEmitter,
                new Configuration(),
                readerContext);
    }

    @Override
    public SplitEnumerator<RocketMQPartitionSplit, RocketMQSourceEnumState> createEnumerator(
            SplitEnumeratorContext<RocketMQPartitionSplit> enumContext) {

        return new RocketMQSourceEnumerator(
                topic,
                consumerGroup,
                nameServerAddress,
                accessKey,
                secretKey,
                stopInMs,
                partitionDiscoveryIntervalMs,
                boundedness,
                enumContext,
                startMode,
                offsetResetStrategy,
                specificOffsets,
                consumerOffsetTimestamp);
    }

    @Override
    public SplitEnumerator<RocketMQPartitionSplit, RocketMQSourceEnumState> restoreEnumerator(
            SplitEnumeratorContext<RocketMQPartitionSplit> enumContext,
            RocketMQSourceEnumState checkpoint) {
        Map<Integer, List<RocketMQPartitionSplit>> listMap = checkpoint.getCurrentAssignment();
        listMap.forEach((a, b) -> System.out.println("xixixixixix" + b));

        return new RocketMQSourceEnumerator(
                topic,
                consumerGroup,
                nameServerAddress,
                accessKey,
                secretKey,
                stopInMs,
                partitionDiscoveryIntervalMs,
                boundedness,
                enumContext,
                checkpoint.getCurrentAssignment(),
                startMode,
                offsetResetStrategy,
                specificOffsets,
                consumerOffsetTimestamp);
    }

    @Override
    public SimpleVersionedSerializer<RocketMQPartitionSplit> getSplitSerializer() {
        return new RocketMQPartitionSplitSerializer();
    }

    @Override
    public SimpleVersionedSerializer<RocketMQSourceEnumState> getEnumeratorCheckpointSerializer() {
        return new RocketMQSourceEnumStateSerializer();
    }

    @Override
    public TypeInformation<OUT> getProducedType() {
        return deserializationSchema.getProducedType();
    }

    /**
     * Builder to build a {@link RocketMQSource}
     *
     * @param <OUT>
     * @return
     */
    public static <OUT> RocketMQSourceBuilder<OUT> builder() {
        return new RocketMQSourceBuilder<>();
    }

    @VisibleForTesting
    Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.set(RocketMQOptions.OPTIONAL_SCAN_STARTUP_MODE, startMode);
        conf.set(RocketMQOptions.OPTIONAL_SCAN_OFFSET_RESET_STRATEGY, offsetResetStrategy);
        conf.set(RocketMQOptions.OPTIONAL_END_TIME_STAMP, stopInMs);
        conf.set(RocketMQOptions.OPTIONAL_SCAN_STARTUP_TIMESTAMP_MILLIS, consumerOffsetTimestamp);
        conf.set(
                RocketMQOptions.OPTIONAL_SCAN_STARTUP_SPECIFIC_OFFSETS, specificOffsets.toString());
        return conf;
    }
}
