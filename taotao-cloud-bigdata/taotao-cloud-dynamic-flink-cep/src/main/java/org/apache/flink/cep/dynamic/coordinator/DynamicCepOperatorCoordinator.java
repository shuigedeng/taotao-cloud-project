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

package org.apache.flink.cep.dynamic.coordinator;

import org.apache.flink.annotation.Internal;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.cep.dynamic.operator.DynamicCepOperator;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;
import org.apache.flink.cep.dynamic.processor.PatternProcessorManager;
import org.apache.flink.cep.dynamic.serializer.PatternProcessorSerializer;
import org.apache.flink.cep.event.UpdatePatternProcessorEvent;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.core.memory.DataInputViewStreamWrapper;
import org.apache.flink.core.memory.DataOutputViewStreamWrapper;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;
import org.apache.flink.util.ExceptionUtils;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.function.ThrowingRunnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.apache.flink.cep.dynamic.coordinator.DynamicCepOperatorCoordinatorSerdeUtils.readBytes;
import static org.apache.flink.cep.dynamic.coordinator.DynamicCepOperatorCoordinatorSerdeUtils.verifyCoordinatorSerdeVersion;
import static org.apache.flink.cep.dynamic.coordinator.DynamicCepOperatorCoordinatorSerdeUtils.writeCoordinatorSerdeVersion;
import static org.apache.flink.util.IOUtils.closeAll;

/**
 * The default implementation of the {@link OperatorCoordinator} for the {@link DynamicCepOperator}.
 *
 * <p>The <code>DynamicCepOperatorCoordinator</code> provides an event loop style thread model to
 * interact with the Flink runtime. The coordinator ensures that all the state manipulations are
 * made by its event loop thread.
 *
 * <p>The coordinator maintains a {@link DynamicCepOperatorCoordinatorContext}. When the coordinator
 * receives an action request from the Flink runtime, it updates the context.
 *
 * @param <T> Base type of the elements appearing in the pattern.
 */
@Internal
public class DynamicCepOperatorCoordinator<T>
        implements OperatorCoordinator, PatternProcessorManager<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicCepOperatorCoordinator.class);

    /** The name of the operator this DynamicCepOperatorCoordinator is associated with. */
    private final String operatorName;
    /** The factory for PatternProcessorDiscoverer to create processor discoverer. */
    private final PatternProcessorDiscovererFactory<T> discovererFactory;
    /** The context containing the states of the coordinator. */
    private final DynamicCepOperatorCoordinatorContext context;
    /** The serializer that handles the serde of the PatternProcessor collection. */
    private PatternProcessorSerializer<T> patternProcessorSerializer;
    /** The pattern processor discoverer to deal with the pattern processor updates. */
    private PatternProcessorDiscoverer<T> discoverer;
    /** The current pattern processors of the DynamicCepOperatorCoordinator to manage. */
    private List<PatternProcessor<T>> currentPatternProcessors = new ArrayList<>();
    /** A flag marking whether the coordinator has started. */
    private boolean started;

    public DynamicCepOperatorCoordinator(
            String operatorName,
            PatternProcessorDiscovererFactory<T> discovererFactory,
            DynamicCepOperatorCoordinatorContext context) {
        this.operatorName = operatorName;
        this.discovererFactory = discovererFactory;
        this.context = context;
        // We must initialize patternProcessorSerializer here as
        // RecreateOnResetOperatorCoordinator#resetAndStart will call 'resetToCheckpoint()' before
        // 'start()'
        this.patternProcessorSerializer = new PatternProcessorSerializer<>();
    }

    @Override
    public void start() throws Exception {
        LOG.info(
                "Starting pattern processor discoverer for {}: {}.",
                this.getClass().getSimpleName(),
                operatorName);

        // we mark this as started first, so that we can later distinguish the cases where 'start()'
        // wasn't called and where 'start()' failed.
        started = true;
        if (discoverer == null) {
            try {
                discoverer =
                        discovererFactory.createPatternProcessorDiscoverer(
                                context.getUserCodeClassloader());
            } catch (Throwable t) {
                ExceptionUtils.rethrowIfFatalError(t);
                LOG.error(
                        "Failed to create PatternProcessorDiscoverer for pattern processor {}",
                        operatorName,
                        t);
                context.failJob(t);
                return;
            }
        }

        // The pattern processor discovery is the first task in the coordinator executor.
        // We rely on the single-threaded coordinator executor to guarantee
        // the other methods are invoked after the discoverer has discovered.
        runInEventLoop(
                () -> discoverer.discoverPatternProcessorUpdates(this),
                "discovering the PatternProcessor updates.");
    }

    @Override
    public void close() throws Exception {
        LOG.info("Closing DynamicCepOperatorCoordinator for pattern processor {}.", operatorName);
        if (started) {
            closeAll(context, discoverer);
        }
        started = false;
        LOG.info("DynamicCepOperatorCoordinator for pattern processor {} closed.", operatorName);
    }

    @Override
    public void handleEventFromOperator(int subtask, OperatorEvent event) throws Exception {
        // no op
    }

    @Override
    public void checkpointCoordinator(long checkpointId, CompletableFuture<byte[]> resultFuture)
            throws Exception {
        runInEventLoop(
                () -> {
                    LOG.debug(
                            "Taking a state snapshot on operator {} for checkpoint {}",
                            operatorName,
                            checkpointId);
                    try {
                        resultFuture.complete(toBytes());
                    } catch (Throwable e) {
                        ExceptionUtils.rethrowIfFatalErrorOrOOM(e);
                        resultFuture.completeExceptionally(
                                new CompletionException(
                                        String.format(
                                                "Failed to checkpoint the current PatternProcessor for pattern processor %s",
                                                operatorName),
                                        e));
                    }
                },
                "taking checkpoint %d",
                checkpointId);
    }

    @Override
    public void notifyCheckpointComplete(long checkpointId) {
        LOG.info(
                "Marking checkpoint {} as completed for pattern processor {}.",
                checkpointId,
                operatorName);
    }

    @Override
    public void notifyCheckpointAborted(long checkpointId) {
        LOG.info(
                "Marking checkpoint {} as aborted for pattern processor {}.",
                checkpointId,
                operatorName);
    }

    @Override
    public void resetToCheckpoint(long checkpointId, @Nullable byte[] checkpointData)
            throws Exception {

        // The checkpoint data is null if there was no completed checkpoint before in that case we
        // don't restore here, but let a fresh PatternProcessorDiscoverer be created when "start()"
        // is called.
        if (checkpointData == null) {
            return;
        }

        LOG.info(
                "Restoring PatternProcessorDiscoverer of pattern processor {} from checkpoint.",
                operatorName);
        currentPatternProcessors = deserializeCheckpoint(checkpointData);
        discoverer =
                discovererFactory.createPatternProcessorDiscoverer(
                        context.getUserCodeClassloader());
    }

    @Override
    public void subtaskFailed(int subtask, @Nullable Throwable reason) {
        runInEventLoop(
                () -> {
                    LOG.info(
                            "Removing itself after failure for subtask {} of pattern processor {}.",
                            subtask,
                            operatorName);
                    context.subtaskNotReady(subtask);
                },
                "handling subtask %d failure",
                subtask);
    }

    @Override
    public void subtaskReset(int subtask, long checkpointId) {
        LOG.info(
                "Recovering subtask {} to checkpoint {} for pattern processor {} to checkpoint.",
                subtask,
                checkpointId,
                operatorName);
        runInEventLoop(
                () -> {
                    context.sendEventToOperator(
                            subtask, new UpdatePatternProcessorEvent<>(currentPatternProcessors));
                },
                "making event gateway to subtask %d available",
                subtask);
    }

    @Override
    public void subtaskReady(int subtask, SubtaskGateway gateway) {
        assert subtask == gateway.getSubtask();
        LOG.debug("Subtask {} of pattern processor {} is ready.", subtask, operatorName);
        runInEventLoop(
                () -> {
                    context.subtaskReady(gateway);
                    context.sendEventToOperator(
                            subtask, new UpdatePatternProcessorEvent<>(currentPatternProcessors));
                },
                "making event gateway to subtask %d available",
                subtask);
    }

    @Override
    public void onPatternProcessorsUpdated(List<PatternProcessor<T>> patternProcessors) {
        try {
            checkPatternProcessors(patternProcessors);
        } catch (Exception e) {
            LOG.error(
                    "Failed to send UpdatePatternProcessorEvent to pattern processor due to check failure {}",
                    operatorName,
                    e);
            context.failJob(e);
            return;
        }
        // TODO: discuss what we should do when patternProcessors is empty. If we allow empty
        // patternProcessors(i.e. "clear" used patternProcessors), the DynamicCepOperator will block
        // upstream data.
        if (patternProcessors.isEmpty()) {
            LOG.warn(
                    "The list of patternProcessors received by the coordination is empty. No update will be applied.");
            return;
        }

        // Sends the UpdatePatternProcessorEvent to DynamicCepOperator with the updated
        // pattern processors.
        for (int subtask : context.getSubtasks()) {
            try {
                context.sendEventToOperator(
                        subtask, new UpdatePatternProcessorEvent<>(patternProcessors));
            } catch (IOException e) {
                LOG.error(
                        "Failed to send UpdatePatternProcessorEvent to pattern processor {}",
                        operatorName,
                        e);
                context.failJob(e);
                return;
            }
        }

        currentPatternProcessors = patternProcessors;
        LOG.info("PatternProcessors have been updated.");
        currentPatternProcessors.forEach(
                patternProcessor -> LOG.debug("new PatternProcessors: " + patternProcessor));
    }

    @VisibleForTesting
    PatternProcessorDiscovererFactory<T> getPatternProcessorDiscovererFactory() {
        return this.discovererFactory;
    }

    @VisibleForTesting
    PatternProcessorDiscoverer<T> getDiscoverer() {
        return discoverer;
    }

    @VisibleForTesting
    List<PatternProcessor<T>> getCurrentPatternProcessors() {
        return this.currentPatternProcessors;
    }

    @VisibleForTesting
    DynamicCepOperatorCoordinatorContext getContext() {
        return context;
    }

    private void runInEventLoop(
            final ThrowingRunnable<Throwable> action,
            final String actionName,
            final Object... actionNameFormatParameters) {

        ensureStarted();

        // We may end up here even for a non-started discoverer, in case the instantiation failed,
        // and we get the 'subtaskFailed()' notification during the failover.
        // We need to ignore those.
        if (discoverer == null) {
            return;
        }

        context.runInCoordinatorThread(
                () -> {
                    try {
                        action.run();
                    } catch (Throwable t) {
                        // If we have a JVM critical error, promote it immediately, there is a good
                        // chance the logging or job failing will not succeed any more
                        ExceptionUtils.rethrowIfFatalErrorOrOOM(t);

                        final String actionString =
                                String.format(actionName, actionNameFormatParameters);
                        LOG.error(
                                "Uncaught exception in the PatternProcessorDiscover for PatternProcessor {} while {}. Triggering job failover.",
                                operatorName,
                                actionString,
                                t);
                        context.failJob(t);
                    }
                });
    }

    // --------------------- Serde -----------------------

    /**
     * Serialize the coordinator state. The current implementation may not be super efficient, but
     * it should not matter that much because most of the state should be rather small. Large states
     * themselves may already be a problem regardless of how the serialization is implemented.
     *
     * @return A byte array containing the serialized state of the pattern processor coordinator.
     * @throws Exception When something goes wrong in serialization.
     */
    private byte[] toBytes() throws Exception {
        return writeCheckpointBytes();
    }

    private byte[] writeCheckpointBytes() throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputViewStreamWrapper(baos)) {

            writeCoordinatorSerdeVersion(out);
            out.writeInt(patternProcessorSerializer.getVersion());
            byte[] serializedCheckpoint =
                    patternProcessorSerializer.serialize(currentPatternProcessors);
            out.writeInt(serializedCheckpoint.length);
            out.write(serializedCheckpoint);
            out.flush();
            return baos.toByteArray();
        }
    }

    /**
     * Restore the state of this pattern processor coordinator from the state bytes.
     *
     * @param bytes The checkpoint bytes that was returned from {@link #toBytes()}
     * @throws Exception When the deserialization failed.
     */
    private List<PatternProcessor<T>> deserializeCheckpoint(byte[] bytes) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                DataInputStream in = new DataInputViewStreamWrapper(bais)) {
            verifyCoordinatorSerdeVersion(in);
            int serializerVersion = in.readInt();
            int serializedCheckpointSize = in.readInt();
            byte[] serializedCheckpoint = readBytes(in, serializedCheckpointSize);

            return patternProcessorSerializer.deserialize(serializerVersion, serializedCheckpoint);
        }
    }

    // --------------------- private methods -------------
    /**
     * Check whether the {@link PatternProcessFunction} of the {@link PatternProcessor} implements
     * {@link CheckpointedFunction} and {@link ListCheckpointed}.
     *
     * @param patternProcessors The {@link PatternProcessor} corresponding to the {@link
     *     PatternProcessFunction}.
     */
    private void checkPatternProcessors(List<PatternProcessor<T>> patternProcessors) {
        Preconditions.checkNotNull(patternProcessors, "Patten processor cannot be null.");
        for (PatternProcessor<T> patternProcessor : patternProcessors) {
            Preconditions.checkNotNull(patternProcessor, "Patten processor cannot be null.");
            PatternProcessFunction<T, ?> patternProcessFunction =
                    patternProcessor.getPatternProcessFunction();
            if (patternProcessFunction == null) {
                throw new NullPointerException(
                        String.format(
                                "Patten process function of the pattern processor {id={%s}, version={%d}} is null",
                                patternProcessor.getId(), patternProcessor.getVersion()));
            }
            if (patternProcessFunction instanceof CheckpointedFunction
                    && patternProcessFunction instanceof ListCheckpointed) {
                throw new IllegalStateException(
                        String.format(
                                "Patten process function of the pattern processor {id={%s}, version={%d}} is not allowed to implement CheckpointedFunction AND ListCheckpointed.",
                                patternProcessor.getId(), patternProcessor.getVersion()));
            }
        }
    }

    private void ensureStarted() {
        if (!started) {
            throw new IllegalStateException("The coordinator has not started yet.");
        }
    }
}
