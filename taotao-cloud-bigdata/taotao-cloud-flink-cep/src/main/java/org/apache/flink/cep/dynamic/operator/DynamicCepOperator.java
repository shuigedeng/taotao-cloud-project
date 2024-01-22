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

package org.apache.flink.cep.dynamic.operator;

import org.apache.flink.annotation.Internal;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.functions.util.FunctionUtils;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.common.typeutils.base.ListSerializer;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.cep.configuration.SharedBufferCacheConfig;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.event.UpdatePatternProcessorEvent;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.functions.TimedOutPartialMatchHandler;
import org.apache.flink.cep.nfa.NFA;
import org.apache.flink.cep.nfa.NFAState;
import org.apache.flink.cep.nfa.NFAStateSerializer;
import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.nfa.compiler.NFACompiler;
import org.apache.flink.cep.nfa.sharedbuffer.SharedBuffer;
import org.apache.flink.cep.nfa.sharedbuffer.SharedBufferAccessor;
import org.apache.flink.cep.operator.CepOperator;
import org.apache.flink.cep.operator.CepRuntimeContext;
import org.apache.flink.cep.time.TimerService;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.runtime.operators.coordination.OperatorEvent;
import org.apache.flink.runtime.operators.coordination.OperatorEventHandler;
import org.apache.flink.runtime.state.StateInitializationContext;
import org.apache.flink.runtime.state.VoidNamespace;
import org.apache.flink.runtime.state.VoidNamespaceSerializer;
import org.apache.flink.streaming.api.graph.StreamConfig;
import org.apache.flink.streaming.api.operators.AbstractStreamOperator;
import org.apache.flink.streaming.api.operators.InternalTimer;
import org.apache.flink.streaming.api.operators.InternalTimerService;
import org.apache.flink.streaming.api.operators.OneInputStreamOperator;
import org.apache.flink.streaming.api.operators.Output;
import org.apache.flink.streaming.api.operators.TimestampedCollector;
import org.apache.flink.streaming.api.operators.Triggerable;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.apache.flink.streaming.runtime.tasks.StreamTask;
import org.apache.flink.util.FlinkRuntimeException;
import org.apache.flink.util.OutputTag;
import org.apache.flink.util.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.apache.flink.cep.operator.CepOperator.PATTERN_MATCHED_TIMES_METRIC_NAME;
import static org.apache.flink.cep.operator.CepOperator.PATTERN_MATCHING_AVG_TIME_METRIC_NAME;

/**
 * Pattern processor operator for a keyed input stream. For each key, the operator creates multiple
 * {@link NFA} and a priority queue to buffer out of order elements. Both data structures are stored
 * using the managed keyed state.
 *
 * @param <IN> Type of the input elements
 * @param <KEY> Type of the key on which the input stream is keyed
 * @param <OUT> Type of the output elements
 */
@Internal
public class DynamicCepOperator<IN, KEY, OUT> extends AbstractStreamOperator<OUT>
        implements OneInputStreamOperator<IN, OUT>,
                Triggerable<KEY, VoidNamespace>,
                OperatorEventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicCepOperator.class);

    private static final long serialVersionUID = 1L;

    private static final String LATE_ELEMENTS_DROPPED_METRIC_NAME = "numLateRecordsDropped";
    private static final String NFA_STATE_NAME = "nfaStateName";
    private static final String EVENT_QUEUE_STATE_NAME = "eventQueuesStateName";

    ///////////////			State			//////////////
    private final boolean isProcessingTime;
    private final TypeSerializer<IN> inputSerializer;
    private transient StateInitializationContext initializationContext;

    private transient Map<NFA<IN>, ValueState<NFAState>> computationStates;
    private transient Map<NFA<IN>, MapState<Long, List<IN>>> elementQueueStates;
    private transient Map<NFA<IN>, SharedBuffer<IN>> partialMatches;

    private transient Map<NFA<IN>, InternalTimerService<VoidNamespace>> timerServices;

    /** The pattern processors of the DynamicCepOperator to manage. */
    private transient Map<PatternProcessor<IN>, NFA<IN>> patternProcessors;

    /** Context passed to user function. */
    private transient ContextFunctionImpl context;

    /** Main output collector, that sets a proper timestamp to the StreamRecord. */
    private transient TimestampedCollector<OUT> collector;

    /** Wrapped RuntimeContext that limits the underlying context features. */
    private transient CepRuntimeContext cepRuntimeContext;

    /** Thin context passed to NFA that gives access to time related characteristics. */
    private transient Map<Tuple2<String, Integer>, TimerService> cepTimerServices;

    // ------------------------------------------------------------------------
    // Metrics
    // ------------------------------------------------------------------------

    private transient Map<Tuple2<String, Integer>, Counter> patternMatchedTimesMetricMap;

    private transient Map<Tuple2<String, Integer>, Counter> numLateRecordsDroppedMetricMap;

    private transient Map<Tuple2<String, Integer>, CepOperator.SimpleGauge<Long>>
            patternMatchingAvgTimeMetricMap;

    private transient Map<Tuple2<String, Integer>, Long> patternMatchingTimesMap;

    private transient Map<Tuple2<String, Integer>, Long> patternMatchingTotalTimeMap;

    public DynamicCepOperator(
            final TypeSerializer<IN> inputSerializer, final boolean isProcessingTime) {
        this.inputSerializer = Preconditions.checkNotNull(inputSerializer);
        this.isProcessingTime = isProcessingTime;
    }

    static String getNameSuffixedWithPatternProcessor(
            String name, Tuple2<String, Integer> patternProcessorIdentifier) {
        return String.format(
                "%s-%s-%s", name, patternProcessorIdentifier.f0, patternProcessorIdentifier.f1);
    }

    @Override
    public void setup(
            StreamTask<?, ?> containingTask,
            StreamConfig config,
            Output<StreamRecord<OUT>> output) {
        super.setup(containingTask, config, output);
        this.cepRuntimeContext = new CepRuntimeContext(getRuntimeContext());
    }

    @Override
    public void initializeState(StateInitializationContext context) throws Exception {
        super.initializeState(context);
        initializationContext = context;
    }

    @Override
    public void open() throws Exception {
        super.open();
        context = new ContextFunctionImpl();
        collector = new TimestampedCollector<>(output);
        numLateRecordsDroppedMetricMap = new HashMap<>();
        patternMatchedTimesMetricMap = new HashMap<>();
        patternMatchingAvgTimeMetricMap = new HashMap<>();
        patternMatchingTimesMap = new HashMap<>();
        patternMatchingTotalTimeMap = new HashMap<>();
        cepTimerServices = new HashMap<>();
    }

    @Override
    public void close() throws Exception {
        super.close();

        if (patternProcessors != null) {
            for (Map.Entry<PatternProcessor<IN>, NFA<IN>> entry : patternProcessors.entrySet()) {
                FunctionUtils.closeFunction(entry.getKey().getPatternProcessFunction());
                entry.getValue().close();
            }
        }

        if (partialMatches != null) {
            for (Map.Entry<NFA<IN>, SharedBuffer<IN>> entry : partialMatches.entrySet()) {
                entry.getValue().releaseCacheStatisticsTimer();
            }
        }
    }

    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        if (patternProcessors != null) {
            for (Map.Entry<PatternProcessor<IN>, NFA<IN>> entry : patternProcessors.entrySet()) {
                final NFA<IN> nfa = entry.getValue();
                if (isProcessingTime) {
                    final AfterMatchSkipStrategy afterMatchSkipStrategy =
                            Optional.ofNullable(
                                            entry.getKey()
                                                    .getPattern(this.getUserCodeClassloader())
                                                    .getAfterMatchSkipStrategy())
                                    .orElse(AfterMatchSkipStrategy.noSkip());
                    final PatternProcessor<IN> patternProcessor = entry.getKey();
                    final PatternProcessFunction<IN, ?> patternProcessFunction =
                            patternProcessor.getPatternProcessFunction();
                    // there can be no out of order elements in processing time
                    final NFAState nfaState = getNFAState(nfa);
                    long timestamp = getProcessingTimeService().getCurrentProcessingTime();
                    advanceTime(patternProcessor, nfa, nfaState, timestamp, patternProcessFunction);
                    processEvent(
                            nfa,
                            nfaState,
                            element.getValue(),
                            timestamp,
                            afterMatchSkipStrategy,
                            patternProcessor);
                    updateNFA(nfa, nfaState);
                } else {
                    long timestamp = element.getTimestamp();
                    IN value = element.getValue();

                    // In event-time processing we assume correctness of the watermark.
                    // Events with timestamp smaller than or equal with the last seen watermark are
                    // considered late.
                    // Late events are put in a dedicated side output, if the user has specified
                    // one.

                    if (timestamp > timerServices.get(nfa).currentWatermark()) {

                        // we have an event with a valid timestamp, so
                        // we buffer it until we receive the proper watermark.

                        saveRegisterWatermarkTimer(nfa);
                        bufferEvent(nfa, value, timestamp);
                    } else {
                        numLateRecordsDroppedMetricMap
                                .get(Tuple2.of(entry.getKey().getId(), entry.getKey().getVersion()))
                                .inc();
                    }
                }
            }
        } else {
            LOG.info(
                    "There are no available pattern processors in the {}",
                    this.getClass().getSimpleName());
        }
    }

    /**
     * Registers a timer for {@code current watermark + 1}, this means that we get triggered
     * whenever the watermark advances, which is what we want for working off the queue of buffered
     * elements.
     */
    private void saveRegisterWatermarkTimer(NFA<IN> nfa) {
        long currentWatermark = timerServices.get(nfa).currentWatermark();
        // protect against overflow
        if (currentWatermark + 1 > currentWatermark) {
            timerServices
                    .get(nfa)
                    .registerEventTimeTimer(VoidNamespace.INSTANCE, currentWatermark + 1);
        }
    }

    private void bufferEvent(NFA<IN> nfa, IN event, long currentTime) throws Exception {
        List<IN> elementsForTimestamp = elementQueueStates.get(nfa).get(currentTime);
        if (elementsForTimestamp == null) {
            elementsForTimestamp = new ArrayList<>();
        }

        elementsForTimestamp.add(event);
        elementQueueStates.get(nfa).put(currentTime, elementsForTimestamp);
    }

    @Override
    public void onEventTime(InternalTimer<KEY, VoidNamespace> timer) throws Exception {

        // 1) get the queue of pending elements for the key and the corresponding NFA,
        // 2) process the pending elements in event time order and custom comparator if exists
        //		by feeding them in the NFA
        // 3) advance the time to the current watermark, so that expired patterns are discarded.
        // 4) update the stored state for the key, by only storing the new NFA and MapState iff they
        //		have state to be used later.
        // 5) update the last seen watermark.

        if (patternProcessors != null) {
            for (Map.Entry<PatternProcessor<IN>, NFA<IN>> entry : patternProcessors.entrySet()) {
                final AfterMatchSkipStrategy afterMatchSkipStrategy =
                        Optional.ofNullable(
                                        entry.getKey()
                                                .getPattern(this.getUserCodeClassloader())
                                                .getAfterMatchSkipStrategy())
                                .orElse(AfterMatchSkipStrategy.noSkip());
                final PatternProcessor<IN> patternProcessor = entry.getKey();
                final PatternProcessFunction<IN, ?> patternProcessFunction =
                        patternProcessor.getPatternProcessFunction();
                final NFA<IN> nfa = entry.getValue();

                // STEP 1
                final PriorityQueue<Long> sortedTimestamps = getSortedTimestamps(nfa);
                final NFAState nfaState = getNFAState(nfa);

                // STEP 2
                while (!sortedTimestamps.isEmpty()
                        && sortedTimestamps.peek() <= timerServices.get(nfa).currentWatermark()) {
                    long timestamp = sortedTimestamps.poll();
                    advanceTime(patternProcessor, nfa, nfaState, timestamp, patternProcessFunction);
                    try (Stream<IN> elements =
                            elementQueueStates.get(nfa).get(timestamp).stream()) {
                        elements.forEachOrdered(
                                event -> {
                                    try {
                                        processEvent(
                                                nfa,
                                                nfaState,
                                                event,
                                                timestamp,
                                                afterMatchSkipStrategy,
                                                patternProcessor);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                    elementQueueStates.get(nfa).remove(timestamp);
                }

                // STEP 3
                advanceTime(
                        patternProcessor,
                        nfa,
                        nfaState,
                        timerServices.get(nfa).currentWatermark(),
                        patternProcessFunction);

                // STEP 4
                updateNFA(nfa, nfaState);

                if (!sortedTimestamps.isEmpty() || !partialMatches.get(nfa).isEmpty()) {
                    saveRegisterWatermarkTimer(nfa);
                }
            }
        }
    }

    @Override
    public void onProcessingTime(InternalTimer<KEY, VoidNamespace> timer) throws Exception {
        // 1) get the queue of pending elements for the key and the corresponding NFA,
        // 2) process the pending elements in process time order and custom comparator if exists
        //		by feeding them in the NFA
        // 3) update the stored state for the key, by only storing the new NFA and MapState iff they
        //		have state to be used later.

        if (patternProcessors != null) {
            for (Map.Entry<PatternProcessor<IN>, NFA<IN>> entry : patternProcessors.entrySet()) {
                final AfterMatchSkipStrategy afterMatchSkipStrategy =
                        Optional.ofNullable(
                                        entry.getKey()
                                                .getPattern(this.getUserCodeClassloader())
                                                .getAfterMatchSkipStrategy())
                                .orElse(AfterMatchSkipStrategy.noSkip());
                final PatternProcessor<IN> patternProcessor = entry.getKey();
                final PatternProcessFunction<IN, ?> patternProcessFunction =
                        patternProcessor.getPatternProcessFunction();
                final NFA<IN> nfa = entry.getValue();

                // STEP 1
                final PriorityQueue<Long> sortedTimestamps = getSortedTimestamps(nfa);
                final NFAState nfaState = getNFAState(nfa);

                // STEP 2
                while (!sortedTimestamps.isEmpty()) {
                    long timestamp = sortedTimestamps.poll();
                    advanceTime(patternProcessor, nfa, nfaState, timestamp, patternProcessFunction);
                    try (Stream<IN> elements =
                            elementQueueStates.get(nfa).get(timestamp).stream()) {
                        elements.forEachOrdered(
                                event -> {
                                    try {
                                        processEvent(
                                                nfa,
                                                nfaState,
                                                event,
                                                timestamp,
                                                afterMatchSkipStrategy,
                                                patternProcessor);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                    elementQueueStates.get(nfa).remove(timestamp);
                }

                // STEP 3
                updateNFA(nfa, nfaState);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleOperatorEvent(OperatorEvent event) {
        if (event instanceof UpdatePatternProcessorEvent) {
            try {
                handleUpdatePatternProcessorEvent((UpdatePatternProcessorEvent<IN>) event);
            } catch (IOException e) {
                throw new FlinkRuntimeException("Failed to deserialize the pattern processors.", e);
            } catch (Exception e) {
                throw new FlinkRuntimeException(
                        "Failed to create the NFA from the pattern processors.", e);
            }
        } else {
            throw new IllegalStateException("Received unexpected operator event " + event);
        }
    }

    private NFAState getNFAState(NFA<IN> nfa) throws IOException {
        NFAState nfaState = computationStates.get(nfa).value();
        return nfaState != null ? nfaState : nfa.createInitialNFAState();
    }

    private void updateNFA(NFA<IN> nfa, NFAState nfaState) throws IOException {
        if (nfaState.isStateChanged()) {
            nfaState.resetStateChanged();
            computationStates.get(nfa).update(nfaState);
        }
    }

    private PriorityQueue<Long> getSortedTimestamps(NFA<IN> nfa) throws Exception {
        PriorityQueue<Long> sortedTimestamps = new PriorityQueue<>();
        for (Long timestamp : elementQueueStates.get(nfa).keys()) {
            sortedTimestamps.offer(timestamp);
        }
        return sortedTimestamps;
    }

    /**
     * Process the given event by giving it to the NFA and outputting the produced set of matched
     * event sequences.
     *
     * @param nfaState Our NFAState object
     * @param event The current event to be processed
     * @param timestamp The timestamp of the event
     */
    private void processEvent(
            NFA<IN> nfa,
            NFAState nfaState,
            IN event,
            long timestamp,
            AfterMatchSkipStrategy afterMatchSkipStrategy,
            PatternProcessor<IN> patternProcessor)
            throws Exception {
        try (SharedBufferAccessor<IN> sharedBufferAccessor =
                partialMatches.get(nfa).getAccessor()) {
            Tuple2<String, Integer> patternProcessorIdentifier =
                    Tuple2.of(patternProcessor.getId(), patternProcessor.getVersion());
            TimerService timerService = cepTimerServices.get(patternProcessorIdentifier);
            long processStartTime = System.nanoTime();
            Collection<Map<String, List<IN>>> patterns =
                    nfa.process(
                            sharedBufferAccessor,
                            nfaState,
                            event,
                            timestamp,
                            afterMatchSkipStrategy,
                            timerService);
            long processEndTime = System.nanoTime();
            patternMatchingTotalTimeMap.merge(
                    patternProcessorIdentifier, processEndTime - processStartTime, Long::sum);
            patternMatchingTimesMap.merge(patternProcessorIdentifier, 1L, Long::sum);
            patternMatchingAvgTimeMetricMap
                    .get(patternProcessorIdentifier)
                    .update(
                            patternMatchingTotalTimeMap.get(patternProcessorIdentifier)
                                    / patternMatchingTimesMap.get(patternProcessorIdentifier));
            processMatchedSequences(patternProcessor, patterns, timestamp);
        }
    }

    /**
     * Advances the time for the given NFA to the given timestamp. This means that no more events
     * with timestamp <b>lower</b> than the given timestamp should be passed to the nfa, This can
     * lead to pruning and timeouts.
     */
    private void advanceTime(
            PatternProcessor<IN> patternProcessor,
            NFA<IN> nfa,
            NFAState nfaState,
            long timestamp,
            PatternProcessFunction<IN, ?> function)
            throws Exception {
        try (SharedBufferAccessor<IN> sharedBufferAccessor =
                partialMatches.get(nfa).getAccessor()) {
            Tuple2<
                            Collection<Map<String, List<IN>>>,
                            Collection<Tuple2<Map<String, List<IN>>, Long>>>
                    pendingMatchesAndTimeout =
                            nfa.advanceTime(sharedBufferAccessor, nfaState, timestamp);

            Collection<Map<String, List<IN>>> pendingMatches = pendingMatchesAndTimeout.f0;
            Collection<Tuple2<Map<String, List<IN>>, Long>> timedOut = pendingMatchesAndTimeout.f1;
            if (!pendingMatches.isEmpty()) {
                processMatchedSequences(patternProcessor, pendingMatches, timestamp);
            }
            if (!timedOut.isEmpty()) {
                processTimedOutSequences(function, timedOut);
            }
        }
    }

    private void processMatchedSequences(
            PatternProcessor<IN> patternProcessor,
            Iterable<Map<String, List<IN>>> matchingSequences,
            long timestamp)
            throws Exception {
        setTimestamp(timestamp);
        for (Map<String, List<IN>> matchingSequence : matchingSequences) {
            patternMatchedTimesMetricMap
                    .get(Tuple2.of(patternProcessor.getId(), patternProcessor.getVersion()))
                    .inc();
            context.setPatternProcessor(patternProcessor);
            ((PatternProcessFunction<IN, OUT>) patternProcessor.getPatternProcessFunction())
                    .processMatch(matchingSequence, context, collector);
        }
    }

    private void processTimedOutSequences(
            PatternProcessFunction<IN, ?> function,
            Collection<Tuple2<Map<String, List<IN>>, Long>> timedOutSequences)
            throws Exception {
        if (function instanceof TimedOutPartialMatchHandler) {

            @SuppressWarnings("unchecked")
            TimedOutPartialMatchHandler<IN> timeoutHandler =
                    (TimedOutPartialMatchHandler<IN>) function;

            for (Tuple2<Map<String, List<IN>>, Long> matchingSequence : timedOutSequences) {
                setTimestamp(matchingSequence.f1);
                timeoutHandler.processTimedOutMatch(matchingSequence.f0, context);
            }
        }
    }

    private void setTimestamp(long timestamp) {
        if (!isProcessingTime) {
            collector.setAbsoluteTimestamp(timestamp);
        }

        context.setTimestamp(timestamp);
    }

    private void handleUpdatePatternProcessorEvent(UpdatePatternProcessorEvent<IN> event)
            throws Exception {
        final List<PatternProcessor<IN>> eventPatternProcessors = event.patternProcessors();
        // clear outdated states in memory when pattern processors are updated to newer version
        clearOutdatedElementQueueState(eventPatternProcessors);
        clearOutdatedComputationState(eventPatternProcessors);
        clearOutdatedSharedBufferState(eventPatternProcessors);

        patternProcessors = new HashMap<>(eventPatternProcessors.size());
        computationStates = new HashMap<>(eventPatternProcessors.size());
        elementQueueStates = new HashMap<>(eventPatternProcessors.size());
        partialMatches = new HashMap<>(eventPatternProcessors.size());
        timerServices = new HashMap<>(eventPatternProcessors.size());

        final Map<Tuple2<String, Integer>, TimerService> newCepTimerServices =
                new HashMap<>(eventPatternProcessors.size());

        // Metrics
        final Map<Tuple2<String, Integer>, Counter> newNumLateRecordsDroppedMetricMap =
                new HashMap<>(eventPatternProcessors.size());
        final Map<Tuple2<String, Integer>, Counter> newPatternMatchedTimesMetricMap =
                new HashMap<>(eventPatternProcessors.size());
        final Map<Tuple2<String, Integer>, CepOperator.SimpleGauge<Long>>
                newPatternMatchingAvgTimeMap = new HashMap<>(eventPatternProcessors.size());

        for (PatternProcessor<IN> patternProcessor : eventPatternProcessors) {
            final Tuple2<String, Integer> patternProcessorIdentifier =
                    Tuple2.of(patternProcessor.getId(), patternProcessor.getVersion());
            final PatternProcessFunction<IN, ?> patternProcessFunction =
                    patternProcessor.getPatternProcessFunction();
            FunctionUtils.setFunctionRuntimeContext(patternProcessFunction, cepRuntimeContext);
            FunctionUtils.openFunction(patternProcessFunction, new Configuration());

            final NFACompiler.NFAFactory<IN> nfaFactory =
                    NFACompiler.compileFactory(
                            patternProcessor.getPattern(this.getUserCodeClassloader()),
                            patternProcessFunction instanceof TimedOutPartialMatchHandler);
            final NFA<IN> nfa = nfaFactory.createNFA();
            nfa.open(cepRuntimeContext, new Configuration());
            final SharedBuffer<IN> partialMatch =
                    new SharedBuffer<>(
                            initializationContext.getKeyedStateStore(),
                            inputSerializer,
                            SharedBufferCacheConfig.of(getOperatorConfig().getConfiguration()),
                            patternProcessor,
                            eventPatternProcessors.size());
            // initializeState through the provided context
            final ValueState<NFAState> computationState =
                    initializationContext
                            .getKeyedStateStore()
                            .getState(
                                    new ValueStateDescriptor<>(
                                            getNameSuffixedWithPatternProcessor(
                                                    NFA_STATE_NAME, patternProcessorIdentifier),
                                            new NFAStateSerializer()));
            final MapState<Long, List<IN>> elementQueueState =
                    initializationContext
                            .getKeyedStateStore()
                            .getMapState(
                                    new MapStateDescriptor<>(
                                            getNameSuffixedWithPatternProcessor(
                                                    EVENT_QUEUE_STATE_NAME,
                                                    patternProcessorIdentifier),
                                            LongSerializer.INSTANCE,
                                            new ListSerializer<>(inputSerializer)));
            if (initializationContext.isRestored()) {
                partialMatch.migrateOldState(getKeyedStateBackend(), computationState);
            }
            final InternalTimerService<VoidNamespace> timerService =
                    getInternalTimerService(
                            String.format(
                                    "watermark-callbacks-%s-%s",
                                    patternProcessor.getId(), patternProcessor.getVersion()),
                            VoidNamespaceSerializer.INSTANCE,
                            this);
            final TimerService cepTimerService = new TimerServiceImpl(nfa);
            patternProcessors.put(patternProcessor, nfa);

            LOG.info(
                    "Use patternProcessors version: "
                            + patternProcessor.getVersion()
                            + " with "
                            + patternProcessor.getPattern(this.getUserCodeClassloader()));
            computationStates.put(nfa, computationState);
            elementQueueStates.put(nfa, elementQueueState);
            partialMatches.put(nfa, partialMatch);
            timerServices.put(nfa, timerService);
            cepTimerServices.put(patternProcessorIdentifier, cepTimerService);
            newCepTimerServices.put(
                    patternProcessorIdentifier,
                    cepTimerServices.getOrDefault(patternProcessorIdentifier, cepTimerService));
            // metrics
            this.addCounterMetric(
                    LATE_ELEMENTS_DROPPED_METRIC_NAME,
                    numLateRecordsDroppedMetricMap,
                    newNumLateRecordsDroppedMetricMap,
                    patternProcessorIdentifier);
            this.addCounterMetric(
                    PATTERN_MATCHED_TIMES_METRIC_NAME,
                    patternMatchedTimesMetricMap,
                    newPatternMatchedTimesMetricMap,
                    patternProcessorIdentifier);
            this.addLongGaugeMetric(
                    PATTERN_MATCHING_AVG_TIME_METRIC_NAME,
                    patternMatchingAvgTimeMetricMap,
                    newPatternMatchingAvgTimeMap,
                    patternProcessorIdentifier);
        }
        numLateRecordsDroppedMetricMap = newNumLateRecordsDroppedMetricMap;
        patternMatchedTimesMetricMap = newPatternMatchedTimesMetricMap;
        patternMatchingAvgTimeMetricMap = newPatternMatchingAvgTimeMap;
        cepTimerServices = newCepTimerServices;
    }

    @VisibleForTesting
    int getNumOfPatternProcessors() {
        return patternProcessors.size();
    }

    @VisibleForTesting
    long getPatternMatchedTimes(Tuple2<String, Integer> identifier) {
        return patternMatchedTimesMetricMap.get(identifier).getCount();
    }

    @VisibleForTesting
    long getLateRecordsNumber(Tuple2<String, Integer> identifier) {
        return numLateRecordsDroppedMetricMap.get(identifier).getCount();
    }

    @VisibleForTesting
    long getPatternMatchingAvgTime(Tuple2<String, Integer> identifier) {
        return patternMatchingAvgTimeMetricMap.get(identifier).getValue();
    }

    @VisibleForTesting
    Map<NFA<IN>, SharedBuffer<IN>> getPartialMatches() {
        return partialMatches;
    }

    @VisibleForTesting
    Map<NFA<IN>, ValueState<NFAState>> getComputationStates() {
        return computationStates;
    }

    @VisibleForTesting
    Map<NFA<IN>, MapState<Long, List<IN>>> getElementQueueStates() {
        return elementQueueStates;
    }

    @VisibleForTesting
    Map<PatternProcessor<IN>, NFA<IN>> getPatternProcessors() {
        return patternProcessors;
    }

    @VisibleForTesting
    StateInitializationContext getInitializationContext() {
        return initializationContext;
    }

    private void addCounterMetric(
            String metricName,
            Map<Tuple2<String, Integer>, Counter> oldMetricMap,
            Map<Tuple2<String, Integer>, Counter> newMetricMap,
            Tuple2<String, Integer> identifier) {
        final Counter patternMatchedTimes =
                !oldMetricMap.containsKey(identifier)
                        ? metrics.counter(
                                getNameSuffixedWithPatternProcessor(metricName, identifier))
                        : oldMetricMap.get(identifier);
        newMetricMap.put(identifier, patternMatchedTimes);
    }

    private void addLongGaugeMetric(
            String metricName,
            Map<Tuple2<String, Integer>, CepOperator.SimpleGauge<Long>> oldMetricMap,
            Map<Tuple2<String, Integer>, CepOperator.SimpleGauge<Long>> newMetricMap,
            Tuple2<String, Integer> identifier) {
        final CepOperator.SimpleGauge<Long> patternMatchedTimes =
                !oldMetricMap.containsKey(identifier)
                        ? metrics.gauge(
                                getNameSuffixedWithPatternProcessor(metricName, identifier),
                                new CepOperator.SimpleGauge<>(0L))
                        : oldMetricMap.get(identifier);
        newMetricMap.put(identifier, patternMatchedTimes);
    }

    private void clearOutdatedComputationState(List<PatternProcessor<IN>> eventPatternProcessors) {
        if (computationStates != null && !computationStates.isEmpty()) {
            clearState(
                    eventPatternProcessors,
                    patternProcessors,
                    p -> {
                        computationStates.get(patternProcessors.get(p)).clear();
                    });
        }
    }

    private void clearOutdatedElementQueueState(List<PatternProcessor<IN>> eventPatternProcessors) {
        if (elementQueueStates != null && !elementQueueStates.isEmpty()) {
            clearState(
                    eventPatternProcessors,
                    patternProcessors,
                    p -> {
                        elementQueueStates.get(patternProcessors.get(p)).clear();
                    });
        }
    }

    private void clearOutdatedSharedBufferState(List<PatternProcessor<IN>> eventPatternProcessors) {
        if (partialMatches != null && !partialMatches.isEmpty()) {
            clearState(
                    eventPatternProcessors,
                    patternProcessors,
                    p -> {
                        partialMatches.get(patternProcessors.get(p)).clear();
                    });
        }
    }

    private static <T> void clearState(
            List<PatternProcessor<T>> newPatternProcessors,
            Map<PatternProcessor<T>, NFA<T>> oldPatternProcessors,
            Consumer<PatternProcessor<T>> fn) {
        // We use (id, version) as the identifier of a PatternProcessor, thus there are 2 cases
        // where we should clear outdated states:
        // Case1: states of current patternProcessors that are older(i.e. version is smaller) than
        // patternProcessors in events
        // Case2: states of current patternProcessors that do not exist(i.e. deleted) in events

        oldPatternProcessors
                .keySet()
                .forEach(
                        oldPatternProcessor -> {
                            // Case1
                            for (PatternProcessor<T> newPatternProcessor : newPatternProcessors) {
                                if (newPatternProcessor.getId().equals(oldPatternProcessor.getId())
                                        && newPatternProcessor.getVersion()
                                                > oldPatternProcessor.getVersion()) {

                                    fn.accept(oldPatternProcessor);
                                }
                            }
                            // Case2
                            boolean isDeleted = true;
                            for (PatternProcessor<T> newPatternProcessor : newPatternProcessors) {
                                if (newPatternProcessor
                                        .getId()
                                        .equals(oldPatternProcessor.getId())) {
                                    if (newPatternProcessor.getVersion()
                                            < oldPatternProcessor.getVersion()) {
                                        LOG.warn(
                                                "Detect newPatternProcessor in new operator event(id: {} version: {}) has smaller version than current one(id: {} version: {}).",
                                                newPatternProcessor.getId(),
                                                newPatternProcessor.getVersion(),
                                                oldPatternProcessor.getId(),
                                                oldPatternProcessor.getVersion());
                                    }
                                    isDeleted = false;
                                    break;
                                }
                            }

                            if (isDeleted) {
                                fn.accept(oldPatternProcessor);
                            }
                        });
    }

    /**
     * Gives {@link NFA} access to {@link InternalTimerService} and tells if {@link
     * DynamicCepOperator} works in processing time. Should be instantiated once per operator.
     */
    private class TimerServiceImpl implements TimerService {

        private final NFA<?> nfa;

        public TimerServiceImpl(NFA<?> nfa) {
            this.nfa = nfa;
        }

        @Override
        public long currentProcessingTime() {
            return timerServices.get(nfa).currentProcessingTime();
        }
    }

    /**
     * Implementation of {@link PatternProcessFunction.Context}. Design to be instantiated once per
     * operator. It serves three methods:
     *
     * <ul>
     *   <li>gives access to currentProcessingTime through {@link InternalTimerService}
     *   <li>gives access to timestamp of current record (or null if Processing time)
     *   <li>enables side outputs with proper timestamp of StreamRecord handling based on either
     *       Processing or Event time
     * </ul>
     */
    public class ContextFunctionImpl implements PatternProcessFunction.Context {

        private Long timestamp;
        private PatternProcessor<?> patternProcessor;

        @Override
        public <X> void output(final OutputTag<X> outputTag, final X value) {
            final StreamRecord<X> record;
            if (isProcessingTime) {
                record = new StreamRecord<>(value);
            } else {
                record = new StreamRecord<>(value, timestamp());
            }
            output.collect(outputTag, record);
        }

        void setPatternProcessor(PatternProcessor<?> patternProcessor) {
            this.patternProcessor = patternProcessor;
        }

        void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public PatternProcessor<?> patternProcessor() {
            return patternProcessor;
        }

        @Override
        public long timestamp() {
            return timestamp;
        }

        @Override
        public long currentProcessingTime() {
            return timerServices
                    .get(patternProcessors.get(patternProcessor))
                    .currentProcessingTime();
        }
    }
}
