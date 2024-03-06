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

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.source.reader.fetcher.RocketMQSourceFetcherManager;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplit;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplitState;

import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.source.reader.RecordEmitter;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.SingleThreadMultiplexSourceReaderBase;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** The source reader for RocketMQ partitions. */
public class RocketMQSourceReader<T>
        extends SingleThreadMultiplexSourceReaderBase<
                Tuple3<T, Long, Long>, T, RocketMQPartitionSplit, RocketMQPartitionSplitState> {

    private static final Logger LOG = LoggerFactory.getLogger(RocketMQSourceReader.class);
    // These maps need to be concurrent because it will be accessed by both the main thread
    // and the split fetcher thread in the callback.
    private final SortedMap<Long, Map<MessageQueue, Long>> offsetsToCommit;
    private final ConcurrentMap<MessageQueue, Long> offsetsOfFinishedSplits;

    public RocketMQSourceReader(
            FutureCompletingBlockingQueue<RecordsWithSplitIds<Tuple3<T, Long, Long>>> elementsQueue,
            RocketMQSourceFetcherManager rocketMQSourceFetcherManager,
            RecordEmitter<Tuple3<T, Long, Long>, T, RocketMQPartitionSplitState> recordEmitter,
            Configuration config,
            SourceReaderContext context) {
        super(elementsQueue, rocketMQSourceFetcherManager, recordEmitter, config, context);
        this.offsetsToCommit = Collections.synchronizedSortedMap(new TreeMap<>());
        this.offsetsOfFinishedSplits = new ConcurrentHashMap<>();
    }

    @Override
    protected void onSplitFinished(Map<String, RocketMQPartitionSplitState> map) {
        map.forEach(
                (ignored, splitState) -> {
                    if (splitState.getCurrentOffset() >= 0) {
                        offsetsOfFinishedSplits.put(
                                new MessageQueue(
                                        splitState.getTopic(),
                                        splitState.getBroker(),
                                        splitState.getPartition()),
                                splitState.getCurrentOffset());
                    }
                });
    }

    @Override
    protected RocketMQPartitionSplitState initializedState(RocketMQPartitionSplit partitionSplit) {
        return new RocketMQPartitionSplitState(partitionSplit);
    }

    @Override
    protected RocketMQPartitionSplit toSplitType(
            String splitId, RocketMQPartitionSplitState splitState) {
        return splitState.toRocketMQPartitionSplit();
    }

    @Override
    public List<RocketMQPartitionSplit> snapshotState(long checkpointId) {
        List<RocketMQPartitionSplit> splits = super.snapshotState(checkpointId);

        if (splits.isEmpty() && offsetsOfFinishedSplits.isEmpty()) {
            offsetsToCommit.put(checkpointId, Collections.emptyMap());
        } else {
            Map<MessageQueue, Long> offsetMap =
                    offsetsToCommit.computeIfAbsent(checkpointId, id -> new HashMap<>());
            for (RocketMQPartitionSplit split : splits) {
                if (split.getStartingOffset() >= 0) {
                    offsetMap.put(
                            new MessageQueue(
                                    split.getTopic(), split.getBroker(), split.getPartition()),
                            split.getStartingOffset());
                }
            }
            // Put offsets of all the finished splits.
            offsetMap.putAll(offsetsOfFinishedSplits);
        }
        return splits;
    }

    @Override
    public void notifyCheckpointComplete(long checkpointId) {
        LOG.debug("Committing offsets for checkpoint {}", checkpointId);
        Map<MessageQueue, Long> committedOffsets = offsetsToCommit.get(checkpointId);
        if (committedOffsets == null || committedOffsets.isEmpty()) {
            LOG.debug(
                    "Offsets for checkpoint {} either do not exist or have already been committed.",
                    checkpointId);
            return;
        }
        ((RocketMQSourceFetcherManager<T>) splitFetcherManager)
                .commitOffsets(
                        committedOffsets,
                        () -> {
                            offsetsOfFinishedSplits
                                    .entrySet()
                                    .removeIf(
                                            entry -> committedOffsets.containsKey(entry.getKey()));
                            while (!offsetsToCommit.isEmpty()
                                    && offsetsToCommit.firstKey() <= checkpointId) {
                                offsetsToCommit.remove(offsetsToCommit.firstKey());
                            }
                        });
    }
}
