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
import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.Quantifier.ConsumingStrategy;
import org.apache.flink.cep.pattern.WithinType;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Graph is used to describe a complex Pattern which contains Nodes(i.e {@link Pattern}) and
 * Edges. The Node of a Graph can be a embedded Graph as well. This class is to (de)serialize Graphs
 * in json format.
 */
public class GraphSpec extends NodeSpec {
    private final int version = 1;
    private final List<NodeSpec> nodes;
    private final List<EdgeSpec> edges;
    private final WindowSpec window;
    private final AfterMatchSkipStrategySpec afterMatchStrategy;

    public GraphSpec(
            @JsonProperty("name") String name,
            @JsonProperty("quantifier") QuantifierSpec quantifier,
            @JsonProperty("condition") ConditionSpec condition,
            @JsonProperty("nodes") List<NodeSpec> nodes,
            @JsonProperty("edges") List<EdgeSpec> edges,
            @JsonProperty("window") WindowSpec window,
            @JsonProperty("afterMatchStrategy") AfterMatchSkipStrategySpec afterMatchStrategy) {
        super(name, quantifier, condition, PatternNodeType.COMPOSITE);
        this.nodes = nodes;
        this.edges = edges;
        this.window = window;
        this.afterMatchStrategy = afterMatchStrategy;
    }

    public static GraphSpec fromPattern(Pattern<?, ?> pattern) {
        // Build the metadata of the GraphSpec from pattern
        // Name
        String name =
                pattern instanceof GroupPattern
                        ? ((GroupPattern<?, ?>) pattern).getRawPattern().getName()
                        : pattern.getName();

        // Quantifier
        QuantifierSpec quantifier =
                new QuantifierSpec(
                        pattern.getQuantifier(), pattern.getTimes(), pattern.getUntilCondition());

        // Window
        Map<WithinType, Time> window = new HashMap<>();
        if (pattern.getWindowTime(WithinType.FIRST_AND_LAST) != null) {
            window.put(WithinType.FIRST_AND_LAST, pattern.getWindowTime(WithinType.FIRST_AND_LAST));
        } else if (pattern.getWindowTime(WithinType.PREVIOUS_AND_CURRENT) != null) {
            window.put(
                    WithinType.PREVIOUS_AND_CURRENT,
                    pattern.getWindowTime(WithinType.PREVIOUS_AND_CURRENT));
        }

        Builder builder =
                new Builder()
                        .name(name)
                        .quantifier(quantifier)
                        .afterMatchStrategy(pattern.getAfterMatchSkipStrategy());
        if (window.size() > 0) {
            builder.window(WindowSpec.fromWindowTime(window));
        }

        // Build nested pattern sequence
        List<NodeSpec> nodes = new ArrayList<>();
        List<EdgeSpec> edges = new ArrayList<>();
        while (pattern != null) {
            if (pattern instanceof GroupPattern) {
                // Process sub graph recursively
                GraphSpec subgraphSpec =
                        GraphSpec.fromPattern(((GroupPattern<?, ?>) pattern).getRawPattern());
                nodes.add(subgraphSpec);
            } else {
                // Build nodeSpec
                NodeSpec nodeSpec = NodeSpec.fromPattern(pattern);
                nodes.add(nodeSpec);
            }
            if (pattern.getPrevious() != null) {
                edges.add(
                        new EdgeSpec(
                                pattern.getPrevious() instanceof GroupPattern
                                        ? ((GroupPattern<?, ?>) pattern.getPrevious())
                                                .getRawPattern()
                                                .getName()
                                        : pattern.getPrevious().getName(),
                                pattern instanceof GroupPattern
                                        ? ((GroupPattern<?, ?>) pattern).getRawPattern().getName()
                                        : pattern.getName(),
                                pattern.getQuantifier().getConsumingStrategy()));
            }
            pattern = pattern.getPrevious();
        }
        builder.nodes(nodes).edges(edges);
        return builder.build();
    }

    /**
     * Converts the {@link GraphSpec} to the {@link Pattern}.
     *
     * @param classLoader The {@link ClassLoader} of the {@link Pattern}.
     * @return The converted {@link Pattern}.
     * @throws Exception Exceptions thrown while deserialization of the Pattern.
     */
    public Pattern<?, ?> toPattern(final ClassLoader classLoader) throws Exception {
        // Construct cache of nodes and edges for later use
        final Map<String, NodeSpec> nodeCache = new HashMap<>();
        for (NodeSpec node : nodes) {
            nodeCache.put(node.getName(), node);
        }
        final Map<String, EdgeSpec> edgeCache = new HashMap<>();
        for (EdgeSpec edgeSpec : edges) {
            edgeCache.put(edgeSpec.getSource(), edgeSpec);
        }

        // Build pattern sequence
        String currentNodeName = findBeginPatternName();
        Pattern<?, ?> prevPattern = null;
        String prevNodeName = null;
        while (currentNodeName != null) {
            NodeSpec currentNodeSpec = nodeCache.get(currentNodeName);
            EdgeSpec edgeToCurrentNode = edgeCache.get(prevNodeName);
            // Build the atomic pattern
            // Note, the afterMatchStrategy of neighboring patterns should adopt the GraphSpec's
            // afterMatchStrategy
            Pattern<?, ?> currentPattern =
                    currentNodeSpec.toPattern(
                            prevPattern,
                            afterMatchStrategy.toAfterMatchSkipStrategy(),
                            prevNodeName == null
                                    ? ConsumingStrategy.STRICT
                                    : edgeToCurrentNode.getType(),
                            classLoader);
            if (currentNodeSpec instanceof GraphSpec) {
                ConsumingStrategy strategy =
                        prevNodeName == null
                                ? ConsumingStrategy.STRICT
                                : edgeToCurrentNode.getType();
                prevPattern =
                        buildGroupPattern(
                                strategy, currentPattern, prevPattern, prevNodeName == null);
            } else {
                prevPattern = currentPattern;
            }
            prevNodeName = currentNodeName;
            currentNodeName =
                    edgeCache.get(currentNodeName) == null
                            ? null
                            : edgeCache.get(currentNodeName).getTarget();
        }
        // Add window semantics
        if (window != null && prevPattern != null) {
            prevPattern.within(this.window.getTime(), this.window.getType());
        }

        return prevPattern;
    }

    public int getVersion() {
        return version;
    }

    public List<NodeSpec> getNodes() {
        return nodes;
    }

    public List<EdgeSpec> getEdges() {
        return edges;
    }

    public WindowSpec getWindow() {
        return window;
    }

    public AfterMatchSkipStrategySpec getAfterMatchStrategy() {
        return afterMatchStrategy;
    }

    private String findBeginPatternName() {
        final Set<String> nodeSpecSet = new HashSet<>();
        for (NodeSpec node : nodes) {
            nodeSpecSet.add(node.getName());
        }
        for (EdgeSpec edgeSpec : edges) {
            nodeSpecSet.remove(edgeSpec.getTarget());
        }
        if (nodeSpecSet.size() != 1) {
            throw new IllegalStateException(
                    "There must be exactly one begin node, but there are "
                            + nodeSpecSet.size()
                            + " nodes that are not pointed by any other nodes.");
        }
        Iterator<String> iterator = nodeSpecSet.iterator();

        if (!iterator.hasNext()) {
            throw new RuntimeException("Could not find the begin node.");
        }

        return iterator.next();
    }

    private GroupPattern<?, ?> buildGroupPattern(
            ConsumingStrategy strategy,
            Pattern<?, ?> currentPattern,
            Pattern<?, ?> prevPattern,
            boolean isBeginPattern) {
        // TODO: make constructor of GroupPattern public so we can avoid using follow hack to
        // construct GroupPattern
        if (strategy.equals(ConsumingStrategy.STRICT)) {
            if (isBeginPattern) {
                currentPattern = Pattern.begin(currentPattern);
            } else {
                currentPattern = prevPattern.next((Pattern) currentPattern);
            }
        } else if (strategy.equals(ConsumingStrategy.SKIP_TILL_NEXT)) {
            currentPattern = prevPattern.followedBy((Pattern) currentPattern);
        } else if (strategy.equals(ConsumingStrategy.SKIP_TILL_ANY)) {
            currentPattern = prevPattern.followedByAny((Pattern) currentPattern);
        }
        return (GroupPattern<?, ?>) currentPattern;
    }

    /** Builder for GraphSpec. */
    private static final class Builder {
        private String name;
        private QuantifierSpec quantifier;
        private List<NodeSpec> nodes;
        private List<EdgeSpec> edges;
        private WindowSpec window;
        private AfterMatchSkipStrategySpec afterMatchStrategy;

        private Builder() {}

        public Builder nodes(List<NodeSpec> nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder edges(List<EdgeSpec> edges) {
            this.edges = edges;
            return this;
        }

        public Builder window(WindowSpec window) {
            this.window = window;
            return this;
        }

        public Builder afterMatchStrategy(AfterMatchSkipStrategy afterMatchStrategy) {
            this.afterMatchStrategy =
                    AfterMatchSkipStrategySpec.fromAfterMatchSkipStrategy(afterMatchStrategy);
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder quantifier(QuantifierSpec quantifier) {
            this.quantifier = quantifier;
            return this;
        }

        public GraphSpec build() {
            return new GraphSpec(name, quantifier, null, nodes, edges, window, afterMatchStrategy);
        }
    }
}
