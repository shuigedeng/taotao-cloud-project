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

package org.apache.rocketmq.flink.sink.demo.dynamic;

import com.alibaba.ververica.cep.demo.event.Event;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;
import org.apache.flink.cep.dynamic.processor.PatternProcessorManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** TestPatternProcessorDiscoverer. */
public class TestPatternProcessorDiscoverer implements PatternProcessorDiscoverer<Event> {
    private boolean closeMark = false;
    private final List<PatternProcessor<Event>> patternProcessors;
    private final ClassLoader userCodeClassLoader;
    private final String patternStr;

    public TestPatternProcessorDiscoverer(String patternStr, ClassLoader userCodeClassLoader) {
        this.patternStr = patternStr;
        this.patternProcessors = new ArrayList<>();
        this.userCodeClassLoader = userCodeClassLoader;
    }

    @Override
    public void discoverPatternProcessorUpdates(
            PatternProcessorManager<Event> patternProcessorManager) {

        try {
            patternProcessors.add(
                    new TestPatternProcessor(
                            "1",
                            patternProcessors.size() + 1,
                            null,
                            this.patternStr,
                            new TestPatternProcessFunction<>(),
                            false));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        patternProcessorManager.onPatternProcessorsUpdated(patternProcessors);
    }

    @Override
    public void close() throws IOException {
        closeMark = true;
    }

    public boolean getCloseMark() {
        return closeMark;
    }
}
