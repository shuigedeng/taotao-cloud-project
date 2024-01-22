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

package org.apache.flink.cep.dynamic.impl.json.spec;

import org.apache.flink.cep.pattern.Quantifier;
import org.apache.flink.cep.pattern.Quantifier.ConsumingStrategy;
import org.apache.flink.cep.pattern.Quantifier.QuantifierProperty;
import org.apache.flink.cep.pattern.Quantifier.Times;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

import java.util.EnumSet;

/**
 * This class is to (de)serialize {@link Quantifier} in json format. It contains Times and
 * untilCondition as well, which are logically a part of a Quantifier.
 */
public class QuantifierSpec {

    private final ConsumingStrategy consumingStrategy;
    private final EnumSet<QuantifierProperty> properties;
    private final @Nullable Times times;
    private final @Nullable ConditionSpec untilCondition;

    public QuantifierSpec(
            @JsonProperty("consumingStrategy") ConsumingStrategy consumingStrategy,
            @JsonProperty("properties") EnumSet<QuantifierProperty> properties,
            @Nullable @JsonProperty("times") Times times,
            @Nullable @JsonProperty("untilCondition") ConditionSpec untilCondition) {
        this.consumingStrategy = consumingStrategy;
        this.properties = properties;
        this.times = times;
        this.untilCondition = untilCondition;
    }

    public QuantifierSpec(Quantifier quantifier, Times times, IterativeCondition untilCondition) {
        this.consumingStrategy = quantifier.getInnerConsumingStrategy();
        this.properties = EnumSet.noneOf(QuantifierProperty.class);
        for (QuantifierProperty property : QuantifierProperty.values()) {
            if (quantifier.hasProperty(property)) {
                this.properties.add(property);
            }
        }

        this.times =
                times == null
                        ? null
                        : Times.of(times.getFrom(), times.getTo(), times.getWindowTime());

        this.untilCondition =
                untilCondition == null ? null : new ClassConditionSpec(untilCondition);
    }

    public ConsumingStrategy getConsumingStrategy() {
        return consumingStrategy;
    }

    public EnumSet<QuantifierProperty> getProperties() {
        return properties;
    }

    @Nullable
    public Times getTimes() {
        return times;
    }

    @Nullable
    public ConditionSpec getUntilCondition() {
        return untilCondition;
    }
}
