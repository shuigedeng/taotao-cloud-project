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

package org.apache.rocketmq.flink.source.reader;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.legacy.common.util.MetricUtils;
import org.apache.rocketmq.flink.source.reader.deserializer.RocketMQDeserializationSchema;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplit;
import org.apache.rocketmq.remoting.exception.RemotingException;

import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.connector.base.source.reader.splitreader.SplitsAddition;
import org.apache.flink.connector.base.source.reader.splitreader.SplitsChange;
import org.apache.flink.util.Collector;
import org.apache.flink.util.Preconditions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.rocketmq.client.consumer.PullStatus.FOUND;

/**
 * A {@link SplitReader} implementation that reads records from RocketMQ partitions.
 *
 * <p>The returned type are in the format of {@code tuple3(record, offset and timestamp}.
 */
public class RocketMQPartitionSplitReader<T>
        implements SplitReader<Tuple3<T, Long, Long>, RocketMQPartitionSplit> {
    private static final Logger LOG = LoggerFactory.getLogger(RocketMQPartitionSplitReader.class);

    private final String topic;
    private final String tag;
    private final String sql;
    private final boolean commitOffsetAuto;

    private final RocketMQDeserializationSchema<T> deserializationSchema;
    private final Map<Tuple3<String, String, Integer>, Long> startingOffsets;
    private final Map<Tuple3<String, String, Integer>, Long> stoppingTimestamps;
    private final SimpleCollector<T> collector;

    private DefaultMQPullConsumer consumer;

    private volatile boolean wakeup = false;

    private static final int MAX_MESSAGE_NUMBER_PER_BLOCK = 64;

    private MetricUtils.TimestampGauge fetchDelay = new MetricUtils.TimestampGauge();

    public RocketMQPartitionSplitReader(
            String topic,
            String consumerGroup,
            String nameServerAddress,
            String accessKey,
            String secretKey,
            String tag,
            String sql,
            RocketMQDeserializationSchema<T> deserializationSchema,
            SourceReaderContext readerContext,
            boolean commitOffsetAuto) {
        this.topic = topic;
        this.tag = tag;
        this.sql = sql;
        this.deserializationSchema = deserializationSchema;
        this.startingOffsets = new HashMap<>();
        this.stoppingTimestamps = new HashMap<>();
        this.collector = new SimpleCollector<>();
        this.commitOffsetAuto = commitOffsetAuto;
        initialRocketMQConsumer(consumerGroup, nameServerAddress, accessKey, secretKey);
        readerContext.metricGroup().gauge(MetricUtils.CURRENT_FETCH_EVENT_TIME_LAG, fetchDelay);
    }

    @Override
    public RecordsWithSplitIds<Tuple3<T, Long, Long>> fetch() throws IOException {
        RocketMQPartitionSplitRecords<Tuple3<T, Long, Long>> recordsBySplits =
                new RocketMQPartitionSplitRecords<>();
        long fetchTime = 0L;
        Set<MessageQueue> messageQueues;
        try {
            messageQueues = consumer.fetchSubscribeMessageQueues(topic);
        } catch (MQClientException e) {
            LOG.error(
                    String.format(
                            "Fetch RocketMQ subscribe message queues of topic[%s] exception.",
                            topic),
                    e);
            recordsBySplits.prepareForRead();
            return recordsBySplits;
        }
        for (MessageQueue messageQueue : messageQueues) {
            Tuple3<String, String, Integer> topicPartition =
                    new Tuple3<>(
                            messageQueue.getTopic(),
                            messageQueue.getBrokerName(),
                            messageQueue.getQueueId());
            if (startingOffsets.containsKey(topicPartition)) {
                long messageOffset = startingOffsets.get(topicPartition);
                PullResult pullResult = null;
                try {
                    if (wakeup) {
                        LOG.info(
                                String.format(
                                        "Wake up pulling messages of topic[%s] broker[%s] queue[%d] tag[%s] sql[%s] from offset[%d].",
                                        messageQueue.getTopic(),
                                        messageQueue.getBrokerName(),
                                        messageQueue.getQueueId(),
                                        tag,
                                        sql,
                                        messageOffset));
                        wakeup = false;
                        recordsBySplits.prepareForRead();
                        return recordsBySplits;
                    }
                    if (StringUtils.isNotEmpty(sql)) {
                        pullResult =
                                consumer.pull(
                                        messageQueue,
                                        MessageSelector.bySql(sql),
                                        messageOffset,
                                        MAX_MESSAGE_NUMBER_PER_BLOCK);
                    } else {
                        pullResult =
                                consumer.pull(
                                        messageQueue,
                                        tag,
                                        messageOffset,
                                        MAX_MESSAGE_NUMBER_PER_BLOCK);
                    }
                    fetchTime = System.currentTimeMillis();
                } catch (MQClientException
                        | RemotingException
                        | MQBrokerException
                        | InterruptedException e) {
                    LOG.warn(
                            String.format(
                                    "Pull RocketMQ messages of topic[%s] broker[%s] queue[%d] tag[%s] sql[%s] from offset[%d] exception.",
                                    messageQueue.getTopic(),
                                    messageQueue.getBrokerName(),
                                    messageQueue.getQueueId(),
                                    tag,
                                    sql,
                                    messageOffset),
                            e);
                }
                startingOffsets.put(
                        topicPartition,
                        pullResult == null ? messageOffset : pullResult.getNextBeginOffset());
                if (pullResult != null && pullResult.getPullStatus() == FOUND) {
                    Collection<Tuple3<T, Long, Long>> recordsForSplit =
                            recordsBySplits.recordsForSplit(
                                    messageQueue.getTopic()
                                            + "-"
                                            + messageQueue.getBrokerName()
                                            + "-"
                                            + messageQueue.getQueueId());
                    for (MessageExt messageExt : pullResult.getMsgFoundList()) {
                        long stoppingTimestamp = getStoppingTimestamp(topicPartition);
                        long storeTimestamp = messageExt.getStoreTimestamp();
                        if (storeTimestamp > stoppingTimestamp) {
                            finishSplitAtRecord(
                                    topicPartition,
                                    stoppingTimestamp,
                                    messageExt.getQueueOffset(),
                                    recordsBySplits);
                            break;
                        }
                        // Add the record to the partition collector.
                        try {
                            deserializationSchema.deserialize(
                                    Collections.singletonList(messageExt), collector);
                            collector
                                    .getRecords()
                                    .forEach(
                                            r ->
                                                    recordsForSplit.add(
                                                            new Tuple3<>(
                                                                    r,
                                                                    messageExt.getQueueOffset(),
                                                                    messageExt
                                                                            .getStoreTimestamp())));
                            if (commitOffsetAuto) {
                                consumer.updateConsumeOffset(
                                        messageQueue, startingOffsets.get(topicPartition));
                                consumer.getOffsetStore()
                                        .persist(consumer.queueWithNamespace(messageQueue));
                            }
                            fetchDelay.report(Math.abs(fetchTime - storeTimestamp));
                        } catch (Exception e) {
                            throw new IOException(
                                    "Failed to deserialize consumer record due to", e);
                        } finally {
                            collector.reset();
                        }
                    }
                }
            }
        }
        recordsBySplits.prepareForRead();
        LOG.debug(
                String.format(
                        "Fetch record splits for MetaQ subscribe message queues of topic[%s].",
                        topic));
        return recordsBySplits;
    }

    @Override
    public void handleSplitsChanges(SplitsChange<RocketMQPartitionSplit> splitsChange) {
        // Get all the partition assignments and stopping timestamps..
        if (!(splitsChange instanceof SplitsAddition)) {
            throw new UnsupportedOperationException(
                    String.format(
                            "The SplitChange type of %s is not supported.",
                            splitsChange.getClass()));
        }
        // Set up the stopping timestamps.
        splitsChange
                .splits()
                .forEach(
                        split -> {
                            Tuple3<String, String, Integer> topicPartition =
                                    new Tuple3<>(
                                            split.getTopic(),
                                            split.getBroker(),
                                            split.getPartition());
                            startingOffsets.put(topicPartition, split.getStartingOffset());
                            stoppingTimestamps.put(topicPartition, split.getStoppingTimestamp());
                        });
    }

    @Override
    public void wakeUp() {
        LOG.debug("Wake up the split reader in case the fetcher thread is blocking in fetch().");
        wakeup = true;
    }

    @Override
    public void close() {
        consumer.shutdown();
    }

    private void finishSplitAtRecord(
            Tuple3<String, String, Integer> topicPartition,
            long stoppingTimestamp,
            long currentOffset,
            RocketMQPartitionSplitRecords<Tuple3<T, Long, Long>> recordsBySplits) {
        LOG.debug(
                "{} has reached stopping timestamp {}, current offset is {}",
                topicPartition.f0 + "-" + topicPartition.f1,
                stoppingTimestamp,
                currentOffset);
        recordsBySplits.addFinishedSplit(RocketMQPartitionSplit.toSplitId(topicPartition));
        startingOffsets.remove(topicPartition);
        stoppingTimestamps.remove(topicPartition);
    }

    private long getStoppingTimestamp(Tuple3<String, String, Integer> topicPartition) {
        return stoppingTimestamps.getOrDefault(topicPartition, Long.MAX_VALUE);
    }

    public void notifyCheckpointComplete(
            Map<MessageQueue, Long> committedOffsets, OffsetCommitCallback callback)
            throws MQClientException {
        if (commitOffsetAuto) {
            return;
        }
        for (Map.Entry<MessageQueue, Long> entry : committedOffsets.entrySet()) {
            consumer.updateConsumeOffset(entry.getKey(), entry.getValue());
            consumer.getOffsetStore().persist(consumer.queueWithNamespace(entry.getKey()));
            LOG.info("Offset commit success.{},offset:{}", entry.getKey(), entry.getValue());
        }
        callback.onComplete();
    }

    // --------------- private helper method ----------------------

    private void initialRocketMQConsumer(
            String consumerGroup, String nameServerAddress, String accessKey, String secretKey) {

        try {
            if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretKey)) {
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

    // ---------------- private helper class ------------------------

    private static class RocketMQPartitionSplitRecords<T> implements RecordsWithSplitIds<T> {
        private final Map<String, Collection<T>> recordsBySplits;
        private final Set<String> finishedSplits;
        private Iterator<Map.Entry<String, Collection<T>>> splitIterator;
        private String currentSplitId;
        private Iterator<T> recordIterator;

        public RocketMQPartitionSplitRecords() {
            this.recordsBySplits = new HashMap<>();
            this.finishedSplits = new HashSet<>();
        }

        private Collection<T> recordsForSplit(String splitId) {
            return recordsBySplits.computeIfAbsent(splitId, id -> new ArrayList<>());
        }

        private void addFinishedSplit(String splitId) {
            finishedSplits.add(splitId);
        }

        private void prepareForRead() {
            splitIterator = recordsBySplits.entrySet().iterator();
        }

        @Override
        @Nullable
        public String nextSplit() {
            if (splitIterator.hasNext()) {
                Map.Entry<String, Collection<T>> entry = splitIterator.next();
                currentSplitId = entry.getKey();
                recordIterator = entry.getValue().iterator();
                return currentSplitId;
            } else {
                currentSplitId = null;
                recordIterator = null;
                return null;
            }
        }

        @Override
        @Nullable
        public T nextRecordFromSplit() {
            Preconditions.checkNotNull(
                    currentSplitId,
                    "Make sure nextSplit() did not return null before "
                            + "iterate over the records split.");
            if (recordIterator.hasNext()) {
                return recordIterator.next();
            } else {
                return null;
            }
        }

        @Override
        public Set<String> finishedSplits() {
            return finishedSplits;
        }
    }

    private static class SimpleCollector<T> implements Collector<T> {
        private final List<T> records = new ArrayList<>();

        @Override
        public void collect(T record) {
            records.add(record);
        }

        @Override
        public void close() {}

        private List<T> getRecords() {
            return records;
        }

        private void reset() {
            records.clear();
        }
    }
}
