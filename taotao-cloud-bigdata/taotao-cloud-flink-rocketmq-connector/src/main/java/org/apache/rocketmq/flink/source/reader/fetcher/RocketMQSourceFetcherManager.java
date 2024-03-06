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

package org.apache.rocketmq.flink.source.reader.fetcher;

import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.flink.source.reader.OffsetCommitCallback;
import org.apache.rocketmq.flink.source.reader.RocketMQPartitionSplitReader;
import org.apache.rocketmq.flink.source.split.RocketMQPartitionSplit;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.base.source.reader.RecordsWithSplitIds;
import org.apache.flink.connector.base.source.reader.SourceReaderBase;
import org.apache.flink.connector.base.source.reader.fetcher.SingleThreadFetcherManager;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcher;
import org.apache.flink.connector.base.source.reader.fetcher.SplitFetcherTask;
import org.apache.flink.connector.base.source.reader.splitreader.SplitReader;
import org.apache.flink.connector.base.source.reader.synchronization.FutureCompletingBlockingQueue;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The SplitFetcherManager for RocketMQ source. This class is needed to help commit the offsets to
 * RocketMQ using the consumer inside the {@link RocketMQPartitionSplitReader}.
 */
public class RocketMQSourceFetcherManager<T>
        extends SingleThreadFetcherManager<Tuple3<T, Long, Long>, RocketMQPartitionSplit> {

    /**
     * Creates a new SplitFetcherManager with a single I/O threads.
     *
     * @param elementsQueue The queue that is used to hand over data from the I/O thread (the
     *     fetchers) to the reader (which emits the records and book-keeps the state. This must be
     *     the same queue instance that is also passed to the {@link SourceReaderBase}.
     * @param splitReaderSupplier The factory for the split reader that connects to the source
     *     system.
     * @param splitFinishedHook Hook for handling finished splits in split fetchers.
     */
    public RocketMQSourceFetcherManager(
            FutureCompletingBlockingQueue<RecordsWithSplitIds<Tuple3<T, Long, Long>>> elementsQueue,
            Supplier<SplitReader<Tuple3<T, Long, Long>, RocketMQPartitionSplit>>
                    splitReaderSupplier,
            Consumer<Collection<String>> splitFinishedHook) {
        super(elementsQueue, splitReaderSupplier, splitFinishedHook);
    }

    public void commitOffsets(
            Map<MessageQueue, Long> committedOffsets, OffsetCommitCallback callback) {
        if (committedOffsets.isEmpty()) {
            return;
        }

        SplitFetcher<Tuple3<T, Long, Long>, RocketMQPartitionSplit> splitFetcher = fetchers.get(0);
        if (splitFetcher != null) {
            commit(splitFetcher, committedOffsets, callback);
        } else {
            splitFetcher = createSplitFetcher();
            commit(splitFetcher, committedOffsets, callback);
            startFetcher(splitFetcher);
        }
    }

    private void commit(
            SplitFetcher<Tuple3<T, Long, Long>, RocketMQPartitionSplit> splitFetcher,
            Map<MessageQueue, Long> committedOffsets,
            OffsetCommitCallback callback) {
        RocketMQPartitionSplitReader<T> rocketMQReader =
                (RocketMQPartitionSplitReader<T>) splitFetcher.getSplitReader();

        splitFetcher.enqueueTask(
                new SplitFetcherTask() {
                    @SneakyThrows
                    @Override
                    public boolean run() {
                        rocketMQReader.notifyCheckpointComplete(committedOffsets, callback);
                        return true;
                    }

                    @Override
                    public void wakeUp() {}
                });
    }
}
