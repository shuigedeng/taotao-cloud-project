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

package org.apache.rocketmq.flink.source.enumerator;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.common.config.OffsetResetStrategy;
import org.apache.rocketmq.flink.legacy.common.config.StartupMode;
import org.apache.rocketmq.flink.legacy.common.util.RocketMQUtils;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplit;

import org.apache.flink.annotation.Internal;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.connector.source.Boundedness;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.api.connector.source.SplitsAssignment;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.FlinkRuntimeException;
import org.apache.flink.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The enumerator class for RocketMQ source. */
@Internal
public class RocketMQSourceEnumerator
        implements SplitEnumerator<RocketMQPartitionSplit, RocketMQSourceEnumState> {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQSourceEnumerator.class);
    private final Map<MessageQueue, Long> offsetTable = new HashMap<>();
    private final long consumerOffsetTimestamp;
    /** The topic used for this RocketMQSource. */
    private final String topic;
    /** The consumer group used for this RocketMQSource. */
    private final String consumerGroup;
    /** The name server address used for this RocketMQSource. */
    private final String nameServerAddress;
    /** The stop timestamp for this RocketMQSource. */
    private final long stopInMs;
    /** The partition discovery interval for this RocketMQSource. */
    private final long partitionDiscoveryIntervalMs;
    /** The boundedness of this RocketMQSource. */
    private final Boundedness boundedness;

    /** The accessKey used for this RocketMQSource. */
    private final String accessKey;
    /** The secretKey used for this RocketMQSource. */
    private final String secretKey;

    private final SplitEnumeratorContext<RocketMQPartitionSplit> context;

    // The internal states of the enumerator.
    /**
     * This set is only accessed by the partition discovery callable in the callAsync() method, i.e
     * worker thread.
     */
    private final Set<Tuple3<String, String, Integer>> discoveredPartitions;
    /** The current assignment by reader id. Only accessed by the coordinator thread. */
    private final Map<Integer, List<RocketMQPartitionSplit>> readerIdToSplitAssignments;
    /**
     * The discovered and initialized partition splits that are waiting for owner reader to be
     * ready.
     */
    private final Map<Integer, Set<RocketMQPartitionSplit>> pendingPartitionSplitAssignment;

    // Lazily instantiated or mutable fields.
    private DefaultMQPullConsumer consumer;
    private boolean noMoreNewPartitionSplits = false;

    // consumer strategy
    private StartupMode startMode;
    private OffsetResetStrategy offsetResetStrategy;
    private Map<MessageQueue, Long> specificStartupOffsets;

    public RocketMQSourceEnumerator(
            String topic,
            String consumerGroup,
            String nameServerAddress,
            String accessKey,
            String secretKey,
            long stopInMs,
            long partitionDiscoveryIntervalMs,
            Boundedness boundedness,
            SplitEnumeratorContext<RocketMQPartitionSplit> context,
            StartupMode startMode,
            OffsetResetStrategy offsetResetStrategy,
            Map<MessageQueue, Long> specificStartupOffsets,
            long consumerOffsetTimestamp) {
        this(
                topic,
                consumerGroup,
                nameServerAddress,
                accessKey,
                secretKey,
                stopInMs,
                partitionDiscoveryIntervalMs,
                boundedness,
                context,
                new HashMap<>(),
                startMode,
                offsetResetStrategy,
                specificStartupOffsets,
                consumerOffsetTimestamp);
    }

    public RocketMQSourceEnumerator(
            String topic,
            String consumerGroup,
            String nameServerAddress,
            String accessKey,
            String secretKey,
            long stopInMs,
            long partitionDiscoveryIntervalMs,
            Boundedness boundedness,
            SplitEnumeratorContext<RocketMQPartitionSplit> context,
            Map<Integer, List<RocketMQPartitionSplit>> currentSplitsAssignments,
            StartupMode startMode,
            OffsetResetStrategy offsetResetStrategy,
            Map<MessageQueue, Long> specificStartupOffsets,
            long consumerOffsetTimestamp) {
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.nameServerAddress = nameServerAddress;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.stopInMs = stopInMs;
        this.partitionDiscoveryIntervalMs = partitionDiscoveryIntervalMs;
        this.boundedness = boundedness;
        this.context = context;
        this.discoveredPartitions = new HashSet<>();
        this.readerIdToSplitAssignments = new HashMap<>(currentSplitsAssignments);
        this.readerIdToSplitAssignments.forEach(
                (reader, splits) ->
                        splits.forEach(
                                s -> {
                                    // Recover offset from checkpoint
                                    offsetTable.put(
                                            new MessageQueue(
                                                    s.getTopic(), s.getBroker(), s.getPartition()),
                                            s.getStartingOffset());
                                    // Recover discovered partitions from checkpoint
                                    discoveredPartitions.add(
                                            new Tuple3<>(
                                                    s.getTopic(), s.getBroker(), s.getPartition()));
                                }));
        this.pendingPartitionSplitAssignment = new HashMap<>();
        this.consumerOffsetTimestamp = consumerOffsetTimestamp;
        this.startMode = startMode;
        this.offsetResetStrategy = offsetResetStrategy;
        this.specificStartupOffsets = specificStartupOffsets;
    }

    @Override
    public void start() {
        initialRocketMQConsumer();
        if (partitionDiscoveryIntervalMs > 0) {
            LOG.info(
                    "Starting the RocketMQSourceEnumerator for consumer group {} "
                            + "with partition discovery interval of {} ms.",
                    consumerGroup,
                    partitionDiscoveryIntervalMs);
            context.callAsync(
                    this::discoverAndInitializePartitionSplit,
                    this::handlePartitionSplitChanges,
                    0,
                    partitionDiscoveryIntervalMs);
        } else {
            LOG.info(
                    "Starting the RocketMQSourceEnumerator for consumer group {} "
                            + "without periodic partition discovery.",
                    consumerGroup);
            context.callAsync(
                    this::discoverAndInitializePartitionSplit, this::handlePartitionSplitChanges);
        }
    }

    @Override
    public void handleSplitRequest(int subtaskId, @Nullable String requesterHostname) {
        // the RocketMQ source pushes splits eagerly, rather than act upon split requests
    }

    @Override
    public void addSplitsBack(List<RocketMQPartitionSplit> splits, int subtaskId) {
        addPartitionSplitChangeToPendingAssignments(splits);
        assignPendingPartitionSplits(Collections.singleton(subtaskId));
    }

    @Override
    public void addReader(int subtaskId) {
        LOG.debug(
                "Adding reader {} to RocketMQSourceEnumerator for consumer group {}.",
                subtaskId,
                consumerGroup);
        assignPendingPartitionSplits(Collections.singleton(subtaskId));
    }

    @Override
    public RocketMQSourceEnumState snapshotState(long checkpointId) {
        return new RocketMQSourceEnumState(readerIdToSplitAssignments);
    }

    @Override
    public void close() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    // ----------------- private methods -------------------

    private Set<RocketMQPartitionSplit> discoverAndInitializePartitionSplit()
            throws MQClientException {
        Set<Tuple3<String, String, Integer>> newPartitions = new HashSet<>();
        Set<Tuple3<String, String, Integer>> removedPartitions =
                new HashSet<>(Collections.unmodifiableSet(discoveredPartitions));
        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(topic);
        Set<MessageQueue> storedMQs = offsetTable.keySet();
        if (messageQueues.removeAll(storedMQs) || storedMQs.isEmpty()) {
            initOffsets(messageQueues);
        }
        Set<RocketMQPartitionSplit> result = new HashSet<>();
        for (MessageQueue messageQueue : messageQueues) {
            Tuple3<String, String, Integer> topicPartition =
                    new Tuple3<>(
                            messageQueue.getTopic(),
                            messageQueue.getBrokerName(),
                            messageQueue.getQueueId());
            if (!removedPartitions.remove(topicPartition)) {
                newPartitions.add(topicPartition);
                result.add(
                        new RocketMQPartitionSplit(
                                topicPartition.f0,
                                topicPartition.f1,
                                topicPartition.f2,
                                offsetTable.get(messageQueue),
                                stopInMs));
            }
        }
        discoveredPartitions.addAll(Collections.unmodifiableSet(newPartitions));
        return result;
    }

    // This method should only be invoked in the coordinator executor thread.
    private void handlePartitionSplitChanges(
            Set<RocketMQPartitionSplit> partitionSplits, Throwable t) {
        if (t != null) {
            throw new FlinkRuntimeException("Failed to handle partition splits change due to ", t);
        }
        if (partitionDiscoveryIntervalMs < 0) {
            LOG.debug("Partition discovery is disabled.");
            noMoreNewPartitionSplits = true;
        }
        addPartitionSplitChangeToPendingAssignments(partitionSplits);
        assignPendingPartitionSplits(context.registeredReaders().keySet());
    }

    // This method should only be invoked in the coordinator executor thread.
    private void addPartitionSplitChangeToPendingAssignments(
            Collection<RocketMQPartitionSplit> newPartitionSplits) {
        int numReaders = context.currentParallelism();
        for (RocketMQPartitionSplit split : newPartitionSplits) {
            int ownerReader =
                    getSplitOwner(
                            split.getTopic(), split.getBroker(), split.getPartition(), numReaders);
            pendingPartitionSplitAssignment
                    .computeIfAbsent(ownerReader, r -> new HashSet<>())
                    .add(split);
        }
        LOG.debug(
                "Assigned {} to {} readers of consumer group {}.",
                newPartitionSplits,
                numReaders,
                consumerGroup);
    }

    // This method should only be invoked in the coordinator executor thread.
    private void assignPendingPartitionSplits(Set<Integer> pendingReaders) {
        Map<Integer, List<RocketMQPartitionSplit>> incrementalAssignment = new HashMap<>();
        pendingPartitionSplitAssignment.forEach(
                (ownerReader, pendingSplits) -> {
                    if (!pendingSplits.isEmpty()
                            && context.registeredReaders().containsKey(ownerReader)) {
                        // The owner reader is ready, assign the split to the owner reader.
                        incrementalAssignment
                                .computeIfAbsent(ownerReader, r -> new ArrayList<>())
                                .addAll(pendingSplits);
                    }
                });
        if (incrementalAssignment.isEmpty()) {
            // No assignment is made.
            return;
        }

        LOG.info("Assigning splits to readers {}", incrementalAssignment);
        context.assignSplits(new SplitsAssignment<>(incrementalAssignment));
        incrementalAssignment.forEach(
                (readerOwner, newPartitionSplits) -> {
                    // Update the split assignment.
                    readerIdToSplitAssignments
                            .computeIfAbsent(readerOwner, r -> new ArrayList<>())
                            .addAll(newPartitionSplits);
                    // Clear the pending splits for the reader owner.
                    pendingPartitionSplitAssignment.remove(readerOwner);
                });
        // Sends NoMoreSplitsEvent to the readers if there is no more partition splits
        // to be assigned.Whether bounded or unbounded flow，the idle subtask will be released.
        if (noMoreNewPartitionSplits) {
            LOG.debug(
                    "No more RocketMQPartitionSplits to assign. Sending NoMoreSplitsEvent to the readers "
                            + "in consumer group {}.",
                    consumerGroup);
            pendingReaders.forEach(context::signalNoMoreSplits);
        }
    }

    private void initialRocketMQConsumer() {
        try {
            if (!StringUtils.isNullOrWhitespaceOnly(accessKey)
                    && !StringUtils.isNullOrWhitespaceOnly(secretKey)) {
                AclClientRPCHook aclClientRPCHook =
                        new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
                consumer = new DefaultMQPullConsumer(consumerGroup, aclClientRPCHook);
            } else {
                consumer = new DefaultMQPullConsumer(consumerGroup);
            }

            consumer.setNamesrvAddr(nameServerAddress);
            consumer.setInstanceName(
                    String.join(
                            "||",
                            ManagementFactory.getRuntimeMXBean().getName(),
                            topic,
                            consumerGroup,
                            "" + System.nanoTime()));
            consumer.start();
        } catch (MQClientException e) {
            LOG.error("Failed to initial RocketMQ consumer.", e);
            consumer.shutdown();
        }
    }

    private void initOffsets(Set<MessageQueue> mqSets) {
        try {
            Map<MessageQueue, Long> map =
                    RocketMQUtils.initOffsets(
                            new ArrayList<>(mqSets),
                            consumer,
                            startMode,
                            offsetResetStrategy,
                            consumerOffsetTimestamp,
                            specificStartupOffsets);
            offsetTable.putAll(map);
        } catch (MQClientException e) {
            LOG.error("Failed to initial consumer offsets", e);
        }
    }

    /**
     * Returns the index of the target subtask that a specific RocketMQ partition should be assigned
     * to.
     *
     * <p>The resulting distribution of partitions of a single topic has the following contract:
     *
     * <ul>
     *   <li>1. Uniformly distributed across subtasks
     *   <li>2. Partitions are round-robin distributed (strictly clockwise w.r.t. ascending subtask
     *       indices) by using the partition id as the offset from a starting index (i.e., the index
     *       of the subtask which partition 0 of the topic will be assigned to, determined using the
     *       topic name).
     * </ul>
     *
     * @param topic the RocketMQ topic assigned.
     * @param broker the RocketMQ broker assigned.
     * @param partition the RocketMQ partition to assign.
     * @param numReaders the total number of readers.
     * @return the id of the subtask that owns the split.
     */
    @VisibleForTesting
    static int getSplitOwner(String topic, String broker, int partition, int numReaders) {
        int startIndex = (((topic + "-" + broker).hashCode() * 31) & 0x7FFFFFFF) % numReaders;

        // here, the assumption is that the id of RocketMQ partitions are always ascending
        // starting from 0, and therefore can be used directly as the offset clockwise from the
        // start index
        return (startIndex + partition) % numReaders;
    }
}
