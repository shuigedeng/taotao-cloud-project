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
import org.apache.flink.cep.dynamic.impl.json.util.CepJsonUtils;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;

import javax.annotation.Nullable;
import java.util.Objects;

import static org.apache.flink.util.Preconditions.checkNotNull;

/** Implementation of PatternProcessor for test. */
public class TestPatternProcessor implements PatternProcessor<Event> {

    private static final long serialVersionUID = 1L;
    /** The ID of the pattern processor. */
    private final String id;

    /** The version of the pattern processor. */
    private final Integer version;

    /** The key of the key selector for the pattern processor. */
    private final @Nullable String key;

    private final @Nullable String patternStr;

    private final boolean shouldFail;

    private final @Nullable PatternProcessFunction<Event, ?> patternProcessFunction;


    public TestPatternProcessor(
            final String id,
            final Integer version,
            final @Nullable String key,
            final String patternStr,
            final @Nullable PatternProcessFunction<Event, ?> patternProcessFunction,
            final boolean shouldFail) {
        this.id = checkNotNull(id);
        this.version = checkNotNull(version);
        this.key = key;
        this.patternProcessFunction = patternProcessFunction;
        this.shouldFail = shouldFail;
        this.patternStr = patternStr;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public Pattern<Event, ?> getPattern(ClassLoader classLoader) {
        try {
            return (Pattern<Event, ?>) CepJsonUtils.convertJSONStringToPattern(this.patternStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the {@link PatternProcessFunction} to collect all the found matches.
     *
     * @return The pattern process function of the pattern processor.
     */
    @Override
    public PatternProcessFunction<Event, ?> getPatternProcessFunction() {
        if (shouldFail) {
            throw new IllegalStateException("Job fail as expected.");
        }
        return patternProcessFunction == null
                ? new TestPatternProcessFunction<>()
                : patternProcessFunction;
    }

    @Nullable
    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, key, patternProcessFunction);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PatternProcessor)) {
            return false;
        }
        PatternProcessor<?> other = (PatternProcessor<?>) obj;
        return this.getId().equals(other.getId()) && this.getVersion() == other.getVersion();
    }
}
