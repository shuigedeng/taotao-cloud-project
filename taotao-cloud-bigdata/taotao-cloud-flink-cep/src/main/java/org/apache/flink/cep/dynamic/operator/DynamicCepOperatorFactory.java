/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.apache.flink.cep.dynamic.operator;

import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.cep.TimeBehaviour;
import org.apache.flink.cep.dynamic.coordinator.DynamicCepOperatorCoordinatorProvider;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;
import org.apache.flink.runtime.jobgraph.OperatorID;
import org.apache.flink.runtime.operators.coordination.OperatorCoordinator;
import org.apache.flink.streaming.api.operators.AbstractStreamOperatorFactory;
import org.apache.flink.streaming.api.operators.CoordinatedOperatorFactory;
import org.apache.flink.streaming.api.operators.OneInputStreamOperatorFactory;
import org.apache.flink.streaming.api.operators.StreamOperator;
import org.apache.flink.streaming.api.operators.StreamOperatorParameters;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeServiceAware;

import static org.apache.flink.util.Preconditions.checkNotNull;

/** The Factory class for {@link DynamicCepOperator}. */
public class DynamicCepOperatorFactory<IN, OUT> extends AbstractStreamOperatorFactory<OUT>
        implements OneInputStreamOperatorFactory<IN, OUT>,
                CoordinatedOperatorFactory<OUT>,
                ProcessingTimeServiceAware {

    private static final long serialVersionUID = 1L;

    private final PatternProcessorDiscovererFactory<IN> discovererFactory;

    private final TypeSerializer<IN> inputSerializer;

    private final TimeBehaviour timeBehaviour;

    public DynamicCepOperatorFactory(
            final PatternProcessorDiscovererFactory<IN> discovererFactory,
            final TypeSerializer<IN> inputSerializer,
            final TimeBehaviour timeBehaviour) {
        this.discovererFactory = checkNotNull(discovererFactory);
        this.inputSerializer = checkNotNull(inputSerializer);
        this.timeBehaviour = timeBehaviour;
    }

    @Override
    public <T extends StreamOperator<OUT>> T createStreamOperator(
            StreamOperatorParameters<OUT> parameters) {
        final OperatorID operatorId = parameters.getStreamConfig().getOperatorID();

        final DynamicCepOperator<IN, ?, OUT> dynamicCepOperator =
                new DynamicCepOperator<>(
                        inputSerializer, timeBehaviour == TimeBehaviour.ProcessingTime);
        dynamicCepOperator.setup(
                parameters.getContainingTask(),
                parameters.getStreamConfig(),
                parameters.getOutput());
        dynamicCepOperator.setProcessingTimeService(parameters.getProcessingTimeService());
        parameters
                .getOperatorEventDispatcher()
                .registerEventHandler(operatorId, dynamicCepOperator);

        @SuppressWarnings("unchecked")
        final T castedOperator = (T) dynamicCepOperator;
        return castedOperator;
    }

    @Override
    public OperatorCoordinator.Provider getCoordinatorProvider(
            String operatorName, OperatorID operatorID) {
        return new DynamicCepOperatorCoordinatorProvider<>(
                operatorName, operatorID, discovererFactory);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends StreamOperator> getStreamOperatorClass(ClassLoader classLoader) {
        return DynamicCepOperator.class;
    }
}
