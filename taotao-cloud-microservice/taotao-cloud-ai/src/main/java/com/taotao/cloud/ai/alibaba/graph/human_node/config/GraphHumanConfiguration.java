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

package com.taotao.cloud.ai.alibaba.graph.human_node.config;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncEdgeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.spring.ai.tutorial.graph.human.dispatcher.HumanFeedbackDispatcher;
import com.spring.ai.tutorial.graph.human.node.ExpanderNode;
import com.spring.ai.tutorial.graph.human.node.HumanFeedbackNode;
import com.spring.ai.tutorial.graph.human.node.TranslateNode;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @since 2025/6/13
 */
@Configuration
public class GraphHumanConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(GraphHumanConfiguration.class);

    @Bean
    public StateGraph humanGraph(ChatClient.Builder chatClientBuilder) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory =
                () -> {
                    HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
                    // 用户输入
                    keyStrategyHashMap.put("query", new ReplaceStrategy());
                    keyStrategyHashMap.put("thread_id", new ReplaceStrategy());

                    keyStrategyHashMap.put("expander_number", new ReplaceStrategy());
                    keyStrategyHashMap.put("expander_content", new ReplaceStrategy());

                    // 人类反馈
                    keyStrategyHashMap.put("feed_back", new ReplaceStrategy());
                    keyStrategyHashMap.put("human_next_node", new ReplaceStrategy());

                    // 是否需要翻译
                    keyStrategyHashMap.put("translate_language", new ReplaceStrategy());
                    keyStrategyHashMap.put("translate_content", new ReplaceStrategy());
                    return keyStrategyHashMap;
                };

        StateGraph stateGraph =
                new StateGraph(keyStrategyFactory)
                        .addNode("expander", node_async(new ExpanderNode(chatClientBuilder)))
                        .addNode("translate", node_async(new TranslateNode(chatClientBuilder)))
                        .addNode("human_feedback", node_async(new HumanFeedbackNode()))
                        .addEdge(StateGraph.START, "expander")
                        .addEdge("expander", "human_feedback")
                        .addConditionalEdges(
                                "human_feedback",
                                AsyncEdgeAction.edge_async((new HumanFeedbackDispatcher())),
                                Map.of("translate", "translate", StateGraph.END, StateGraph.END))
                        .addEdge("translate", StateGraph.END);

        // 添加 PlantUML 打印
        GraphRepresentation representation =
                stateGraph.getGraph(GraphRepresentation.Type.PLANTUML, "human flow");
        logger.info("\n=== expander UML Flow ===");
        logger.info(representation.content());
        logger.info("==================================\n");

        return stateGraph;
    }
}
