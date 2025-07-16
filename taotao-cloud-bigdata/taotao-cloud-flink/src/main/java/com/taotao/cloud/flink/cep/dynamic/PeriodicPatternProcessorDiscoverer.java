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

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorManager;

/**
 * Implementation of the {@link PatternProcessorDiscoverer} that periodically discovers the pattern
 * processor updates.
 *
 * @param <T> Base type of the elements appearing in the pattern.
 */
public abstract class PeriodicPatternProcessorDiscoverer<T>
        implements PatternProcessorDiscoverer<T> {

    private final Long intervalMillis;

    private final Timer timer;

    /**
     * Creates a new {@link PatternProcessorDiscoverer} using the given initial {@link
     * PatternProcessor} and the time interval how often to check the pattern processor updates.
     *
     * @param intervalMillis Time interval in milliseconds how often to check updates.
     */
    public PeriodicPatternProcessorDiscoverer(final Long intervalMillis) {
        this.intervalMillis = intervalMillis;
        this.timer = new Timer();
    }

    /**
     * Returns whether there are updated pattern processors.
     *
     * @return Whether there are updated pattern processors.
     */
    public abstract boolean arePatternProcessorsUpdated();

    /**
     * Returns the latest pattern processors.
     *
     * @return The list of {@link PatternProcessor}.
     */
    public abstract List<PatternProcessor<T>> getLatestPatternProcessors() throws Exception;

    @Override
    public void discoverPatternProcessorUpdates(
            PatternProcessorManager<T> patternProcessorManager) {
        // Periodically discovers the pattern processor updates.
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (arePatternProcessorsUpdated()) {
                            List<PatternProcessor<T>> patternProcessors = null;
                            try {
                                patternProcessors = getLatestPatternProcessors();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            patternProcessorManager.onPatternProcessorsUpdated(patternProcessors);
                        }
                    }
                },
                0,
                intervalMillis);
    }

    @Override
    public void close() throws IOException {
        timer.cancel();
    }
}
