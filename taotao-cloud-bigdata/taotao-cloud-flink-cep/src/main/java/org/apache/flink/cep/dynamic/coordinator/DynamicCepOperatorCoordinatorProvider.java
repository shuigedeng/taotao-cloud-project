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

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.runtime.operators.coordination.RecreateOnResetOperatorCoordinator;
import org.apache.flink.util.FatalExitExceptionHandler;

import javax.annotation.Nullable;

import java.util.concurrent.ThreadFactory;

/** The provider of {@link DynamicCepOperatorCoordinator}. */
public class DynamicCepOperatorCoordinatorProvider<T>
        extends RecreateOnResetOperatorCoordinator.Provider {

    private static final long serialVersionUID = 1L;

    private final String operatorName;
    private final PatternProcessorDiscovererFactory<T> discovererFactory;

    /**
     * Construct the {@link DynamicCepOperatorCoordinatorProvider}.
     *
     * @param operatorName The name of the operator.
     * @param operatorID The ID of the operator this coordinator corresponds to.
     * @param discovererFactory The factory for {@link PatternProcessorDiscoverer} to discover new
     *     pattern processors.
     */
    public DynamicCepOperatorCoordinatorProvider(
            String operatorName,
            OperatorID operatorID,
            PatternProcessorDiscovererFactory<T> discovererFactory) {
        super(operatorID);
        this.operatorName = operatorName;
        this.discovererFactory = discovererFactory;
    }

    @Override
    public OperatorCoordinator getCoordinator(OperatorCoordinator.Context context) {
        final String coordinatorThreadName = "DynamicCepOperatorCoordinator-" + operatorName;
        CoordinatorExecutorThreadFactory coordinatorThreadFactory =
                new CoordinatorExecutorThreadFactory(
                        coordinatorThreadName, context.getUserCodeClassloader());

        DynamicCepOperatorCoordinatorContext coordinatorContext =
                new DynamicCepOperatorCoordinatorContext(coordinatorThreadFactory, context);
        return new DynamicCepOperatorCoordinator<>(
                operatorName, discovererFactory, coordinatorContext);
    }

    /** A thread factory class that provides some helper methods. */
    public static class CoordinatorExecutorThreadFactory
            implements ThreadFactory, Thread.UncaughtExceptionHandler {

        private final String coordinatorThreadName;
        private final ClassLoader classLoader;
        private final Thread.UncaughtExceptionHandler errorHandler;

        @Nullable private Thread thread;

        // TODO discuss if we should fail the job(JM may restart the job later) or directly kill JM
        // process
        // Currently we choose to directly kill JM process
        CoordinatorExecutorThreadFactory(
                final String coordinatorThreadName, final ClassLoader contextClassLoader) {
            this(coordinatorThreadName, contextClassLoader, FatalExitExceptionHandler.INSTANCE);
        }

        @VisibleForTesting
        CoordinatorExecutorThreadFactory(
                final String coordinatorThreadName,
                final ClassLoader contextClassLoader,
                final Thread.UncaughtExceptionHandler errorHandler) {
            this.coordinatorThreadName = coordinatorThreadName;
            this.classLoader = contextClassLoader;
            this.errorHandler = errorHandler;
        }

        @Override
        public synchronized Thread newThread(Runnable r) {
            thread = new Thread(r, coordinatorThreadName);
            thread.setContextClassLoader(classLoader);
            thread.setUncaughtExceptionHandler(this);
            return thread;
        }

        @Override
        public synchronized void uncaughtException(Thread t, Throwable e) {
            errorHandler.uncaughtException(t, e);
        }

        public String getCoordinatorThreadName() {
            return coordinatorThreadName;
        }

        boolean isCurrentThreadCoordinatorThread() {
            return Thread.currentThread() == thread;
        }
    }
}
