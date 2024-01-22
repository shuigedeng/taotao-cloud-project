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

package org.apache.flink.cep;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.NullByteKeySelector;
import org.apache.flink.cep.dynamic.operator.DynamicCepOperatorFactory;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

/**
 * Utility class for complex event processing.
 *
 * <p>Methods which transform a {@link DataStream} into a {@link PatternStream} to do CEP.
 */
public class CEP {
    /**
     * Creates a {@link PatternStream} from an input data stream and a pattern.
     *
     * @param input DataStream containing the input events
     * @param pattern Pattern specification which shall be detected
     * @param <T> Type of the input events
     * @return Resulting pattern stream
     */
    public static <T> PatternStream<T> pattern(DataStream<T> input, Pattern<T, ?> pattern) {
        return new PatternStream<>(input, pattern);
    }

    /**
     * Creates a {@link PatternStream} from an input data stream and a pattern.
     *
     * @param input DataStream containing the input events
     * @param pattern Pattern specification which shall be detected
     * @param comparator Comparator to sort events with equal timestamps
     * @param <T> Type of the input events
     * @return Resulting pattern stream
     */
    public static <T> PatternStream<T> pattern(
            DataStream<T> input, Pattern<T, ?> pattern, EventComparator<T> comparator) {
        final PatternStream<T> stream = new PatternStream<>(input, pattern);
        return stream.withComparator(comparator);
    }

    /**
     * Creates a {@link DataStream} containing the results of {@link PatternProcessFunction} to
     * fully matching event pattern processors.
     *
     * @param input DataStream containing the input events
     * @param patternProcessorDiscovererFactory Pattern processor discoverer factory to create the
     *     {@link PatternProcessorDiscoverer} for the {@link PatternProcessor} discovery
     * @param timeBehaviour How the system determines time for time-dependent order and operations
     *     that depend on time.
     * @param outTypeInfo Explicit specification of output type.
     * @param <T> Type of the input events
     * @param <R> Type of the resulting elements
     * @return {@link DataStream} which contains the resulting elements from the pattern processor
     *     discoverer
     */
    public static <T, R> SingleOutputStreamOperator<R> dynamicPatterns(
            DataStream<T> input,
            PatternProcessorDiscovererFactory<T> patternProcessorDiscovererFactory,
            TimeBehaviour timeBehaviour,
            TypeInformation<R> outTypeInfo) {
        final DynamicCepOperatorFactory<T, R> operatorFactory =
                new DynamicCepOperatorFactory<>(
                        patternProcessorDiscovererFactory,
                        input.getType().createSerializer(input.getExecutionConfig()),
                        timeBehaviour);
        if (input instanceof KeyedStream) {
            KeyedStream<T, ?> keyedStream = (KeyedStream<T, ?>) input;
            return keyedStream.transform("DynamicCepOperator", outTypeInfo, operatorFactory);
        } else {
            return input.keyBy(new NullByteKeySelector<>())
                    .transform("GlobalDynamicCepOperator", outTypeInfo, operatorFactory)
                    .forceNonParallel();
        }
    }
}
