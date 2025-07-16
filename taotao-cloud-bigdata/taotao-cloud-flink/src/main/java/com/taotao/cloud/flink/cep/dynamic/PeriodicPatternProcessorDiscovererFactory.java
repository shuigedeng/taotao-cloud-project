/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.flink.cep.dynamic;

import java.util.List;
import javax.annotation.Nullable;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscovererFactory;

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
