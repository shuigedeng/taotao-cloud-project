package com.taotao.cloud.flink.cep.dynamic;

import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;

import javax.annotation.Nullable;

import java.util.List;

/**
 * Implementation of the {@link PatternProcessorDiscovererFactory} that creates the {@link
 * PeriodicPatternProcessorDiscoverer} instance.
 *
 * @param <T> Base type of the elements appearing in the pattern.
 */
public abstract class PeriodicPatternProcessorDiscovererFactory<T>
        implements PatternProcessorDiscovererFactory<T> {

    @Nullable private final List<PatternProcessor<T>> initialPatternProcessors;
    private final Long intervalMillis;

    public PeriodicPatternProcessorDiscovererFactory(
            @Nullable final List<PatternProcessor<T>> initialPatternProcessors,
            @Nullable final Long intervalMillis) {
        this.initialPatternProcessors = initialPatternProcessors;
        this.intervalMillis = intervalMillis;
    }

    @Nullable
    public List<PatternProcessor<T>> getInitialPatternProcessors() {
        return initialPatternProcessors;
    }

    @Override
    public abstract PatternProcessorDiscoverer<T> createPatternProcessorDiscoverer(
            ClassLoader userCodeClassLoader) throws Exception;

    public Long getIntervalMillis() {
        return intervalMillis;
    }
}
