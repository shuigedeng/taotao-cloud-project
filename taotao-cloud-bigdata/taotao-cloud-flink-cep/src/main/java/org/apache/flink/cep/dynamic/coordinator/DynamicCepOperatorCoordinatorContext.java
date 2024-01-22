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
import org.apache.flink.cep.dynamic.coordinator.DynamicCepOperatorCoordinatorProvider.CoordinatorExecutorThreadFactory;
import org.apache.flink.runtime.operators.coordination.ComponentClosingUtils;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;
import org.apache.flink.util.ExceptionUtils;
import org.apache.flink.util.FlinkRuntimeException;
import org.apache.flink.util.clock.Clock;
import org.apache.flink.util.clock.SystemClock;
import org.apache.flink.util.concurrent.ExecutorThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A context class for the {@link DynamicCepOperatorCoordinator}.
 *
 * <p>The context serves a few purposes:
 *
 * <ul>
 *   <li>Thread model enforcement - The context ensures that all the manipulations to the
 *       coordinator state are handled by the same thread.
 * </ul>
 */
@Internal
public class DynamicCepOperatorCoordinatorContext implements AutoCloseable {

    private static final Logger LOG =
            LoggerFactory.getLogger(DynamicCepOperatorCoordinatorContext.class);

    private final ScheduledExecutorService coordinatorExecutor;
    private final ScheduledExecutorService workerExecutor;
    private final CoordinatorExecutorThreadFactory coordinatorThreadFactory;
    private final OperatorCoordinator.Context operatorCoordinatorContext;
    private final Map<Integer, OperatorCoordinator.SubtaskGateway> subtaskGateways;

    private static final Clock clock = SystemClock.getInstance();

    public DynamicCepOperatorCoordinatorContext(
            CoordinatorExecutorThreadFactory coordinatorThreadFactory,
            OperatorCoordinator.Context operatorCoordinatorContext) {
        this(
                Executors.newScheduledThreadPool(1, coordinatorThreadFactory),
                Executors.newScheduledThreadPool(
                        1,
                        new ExecutorThreadFactory(
                                coordinatorThreadFactory.getCoordinatorThreadName() + "-worker")),
                coordinatorThreadFactory,
                operatorCoordinatorContext);
    }

    public DynamicCepOperatorCoordinatorContext(
            ScheduledExecutorService coordinatorExecutor,
            ScheduledExecutorService workerExecutor,
            CoordinatorExecutorThreadFactory coordinatorThreadFactory,
            OperatorCoordinator.Context operatorCoordinatorContext) {
        this.coordinatorExecutor = coordinatorExecutor;
        this.workerExecutor = workerExecutor;
        this.coordinatorThreadFactory = coordinatorThreadFactory;
        this.operatorCoordinatorContext = operatorCoordinatorContext;
        this.subtaskGateways = new HashMap<>(operatorCoordinatorContext.currentParallelism());
    }

    @Override
    public void close() throws InterruptedException {
        // Close quietly so the closing sequence will be executed completely.
        ComponentClosingUtils.shutdownExecutorForcefully(
                workerExecutor, Duration.ofNanos(Long.MAX_VALUE));
        ComponentClosingUtils.shutdownExecutorForcefully(
                coordinatorExecutor, Duration.ofNanos(Long.MAX_VALUE));
    }

    public void runInCoordinatorThread(Runnable runnable) {
        coordinatorExecutor.execute(runnable);
    }

    // --------- Package private methods for the DynamicCepOperatorCoordinator ------------
    ClassLoader getUserCodeClassloader() {
        return this.operatorCoordinatorContext.getUserCodeClassloader();
    }

    void subtaskReady(OperatorCoordinator.SubtaskGateway gateway) {
        final int subtask = gateway.getSubtask();
        if (subtaskGateways.get(subtask) == null) {
            subtaskGateways.put(subtask, gateway);
        } else {
            throw new IllegalStateException("Already have a subtask gateway for " + subtask);
        }
    }

    void subtaskNotReady(int subtaskIndex) {
        subtaskGateways.put(subtaskIndex, null);
    }

    Set<Integer> getSubtasks() {
        return subtaskGateways.keySet();
    }

    public void sendEventToOperator(int subtaskId, OperatorEvent event) {
        callInCoordinatorThread(
                () -> {
                    final OperatorCoordinator.SubtaskGateway gateway =
                            subtaskGateways.get(subtaskId);
                    if (gateway == null) {
                        LOG.warn(
                                String.format(
                                        "Subtask %d is not ready yet to receive events.",
                                        subtaskId));
                    } else {
                        gateway.sendEvent(event);
                    }
                    return null;
                },
                String.format("Failed to send event %s to subtask %d", event, subtaskId));
    }

    /**
     * Fail the job with the given cause.
     *
     * @param cause the cause of the job failure.
     */
    void failJob(Throwable cause) {
        operatorCoordinatorContext.failJob(cause);
    }

    // ---------------- private helper methods -----------------

    /**
     * A helper method that delegates the callable to the coordinator thread if the current thread
     * is not the coordinator thread, otherwise call the callable right away.
     *
     * @param callable the callable to delegate.
     */
    private <V> V callInCoordinatorThread(Callable<V> callable, String errorMessage) {
        // Ensure the split assignment is done by the coordinator executor.
        if (!coordinatorThreadFactory.isCurrentThreadCoordinatorThread()
                && !coordinatorExecutor.isShutdown()) {
            try {
                final Callable<V> guardedCallable =
                        () -> {
                            try {
                                return callable.call();
                            } catch (Throwable t) {
                                LOG.error(
                                        "Uncaught Exception in DynamicCepOperatorCoordinator Executor",
                                        t);
                                ExceptionUtils.rethrowException(t);
                                return null;
                            }
                        };

                return coordinatorExecutor.submit(guardedCallable).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new FlinkRuntimeException(errorMessage, e);
            }
        }

        try {
            return callable.call();
        } catch (Throwable t) {
            LOG.error("Uncaught Exception in Source Coordinator Executor", t);
            throw new FlinkRuntimeException(errorMessage, t);
        }
    }
}
