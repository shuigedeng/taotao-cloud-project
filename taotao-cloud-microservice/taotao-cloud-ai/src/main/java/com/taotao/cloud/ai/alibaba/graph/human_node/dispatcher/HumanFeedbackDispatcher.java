package com.taotao.cloud.ai.alibaba.graph.human_node.dispatcher;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.EdgeAction;

/**
 * @author yingzi
 * @since 2025/6/19
 */

public class HumanFeedbackDispatcher implements EdgeAction {
    @Override
    public String apply(OverAllState state) throws Exception {
        return (String) state.value("human_next_node", StateGraph.END);
    }
}
