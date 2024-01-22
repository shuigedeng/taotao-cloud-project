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

import org.apache.flink.cep.nfa.aftermatch.AfterMatchSkipStrategy;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.Quantifier.ConsumingStrategy;
import org.apache.flink.cep.pattern.Quantifier.QuantifierProperty;
import org.apache.flink.cep.pattern.Quantifier.Times;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.RichOrCondition;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Node is used to describe a Pattern and contains all necessary fields of a Pattern. This class
 * is to (de)serialize Nodes in json format.
 */
public class NodeSpec {
    private final String name;
    private final QuantifierSpec quantifier;
    private final ConditionSpec condition;

    private final PatternNodeType type;

    public NodeSpec(String name, QuantifierSpec quantifier, ConditionSpec condition) {
        this(name, quantifier, condition, PatternNodeType.ATOMIC);
    }

    public NodeSpec(
            @JsonProperty("name") String name,
            @JsonProperty("quantifier") QuantifierSpec quantifier,
            @JsonProperty("condition") ConditionSpec condition,
            @JsonProperty("type") PatternNodeType type) {
        this.name = name;
        this.quantifier = quantifier;
        this.condition = condition;
        this.type = type;
    }

    /** Build NodeSpec from given Pattern. */
    public static NodeSpec fromPattern(Pattern<?, ?> pattern) {
        QuantifierSpec quantifier =
                new QuantifierSpec(
                        pattern.getQuantifier(), pattern.getTimes(), pattern.getUntilCondition());
        return new Builder()
                .name(pattern.getName())
                .quantifier(quantifier)
                .condition(ConditionSpec.fromCondition(pattern.getCondition()))
                .build();
    }

    /**
     * Converts the {@link GraphSpec} to the {@link Pattern}.
     *
     * @param classLoader The {@link ClassLoader} of the {@link Pattern}.
     * @return The converted {@link Pattern}.
     * @throws Exception Exceptions thrown while deserialization of the Pattern.
     */
    public Pattern<?, ?> toPattern(
            final Pattern<?, ?> previous,
            final AfterMatchSkipStrategy afterMatchSkipStrategy,
            final ConsumingStrategy consumingStrategy,
            final ClassLoader classLoader)
            throws Exception {
        // Build pattern
        if (this instanceof GraphSpec) {
            // TODO: should log if AfterMatchSkipStrategy of subgraph diff from the larger graph
            return ((GraphSpec) this).toPattern(classLoader);
        }
        Pattern<?, ?> pattern =
                new Pattern(this.getName(), previous, consumingStrategy, afterMatchSkipStrategy);
        final ConditionSpec conditionSpec = this.getCondition();
        if (conditionSpec != null) {
            IterativeCondition iterativeCondition = conditionSpec.toIterativeCondition(classLoader);
            if (iterativeCondition instanceof RichOrCondition) {
                pattern.or(iterativeCondition);
            } else {
                pattern.where(iterativeCondition);
            }
        }

        // Process quantifier's properties
        for (QuantifierProperty property : this.getQuantifier().getProperties()) {
            if (property.equals(QuantifierProperty.OPTIONAL)) {
                pattern.optional();
            } else if (property.equals(QuantifierProperty.GREEDY)) {
                pattern.greedy();
            } else if (property.equals(QuantifierProperty.LOOPING)) {
                final Times times = this.getQuantifier().getTimes();
                if (times != null) {
                    pattern.timesOrMore(times.getFrom(), times.getWindowTime());
                }
            } else if (property.equals(QuantifierProperty.TIMES)) {
                final Times times = this.getQuantifier().getTimes();
                if (times != null) {
                    pattern.times(times.getFrom(), times.getTo());
                }
            }
        }

        // Process innerConsumingStrategy of the quantifier
        final ConsumingStrategy innerConsumingStrategy =
                this.getQuantifier().getConsumingStrategy();
        if (innerConsumingStrategy.equals(ConsumingStrategy.SKIP_TILL_ANY)) {
            pattern.allowCombinations();
        } else if (innerConsumingStrategy.equals(ConsumingStrategy.STRICT)) {
            pattern.consecutive();
        }

        // Process until condition
        final ConditionSpec untilCondition = this.getQuantifier().getUntilCondition();
        if (untilCondition != null) {
            final IterativeCondition iterativeCondition =
                    untilCondition.toIterativeCondition(classLoader);
            pattern.until(iterativeCondition);
        }
        return pattern;
    }

    public String getName() {
        return name;
    }

    public PatternNodeType getType() {
        return type;
    }

    public QuantifierSpec getQuantifier() {
        return quantifier;
    }

    public ConditionSpec getCondition() {
        return condition;
    }

    /** Type of Node. */
    public enum PatternNodeType {
        // ATOMIC Node is the basic Pattern
        ATOMIC,
        // COMPOSITE Node is a Graph
        COMPOSITE
    }

    /** The Builder for ModeSpec. */
    private static final class Builder {
        private String name;
        private QuantifierSpec quantifier;
        private ConditionSpec condition;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantifier(QuantifierSpec quantifier) {
            this.quantifier = quantifier;
            return this;
        }

        public Builder condition(ConditionSpec condition) {
            this.condition = condition;
            return this;
        }

        public NodeSpec build() {
            return new NodeSpec(this.name, this.quantifier, this.condition);
        }
    }
}
