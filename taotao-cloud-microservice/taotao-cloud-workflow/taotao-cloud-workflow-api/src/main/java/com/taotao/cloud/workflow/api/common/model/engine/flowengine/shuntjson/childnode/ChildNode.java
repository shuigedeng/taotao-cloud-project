package com.taotao.cloud.workflow.api.common.model.engine.flowengine.shuntjson.childnode;

import java.util.List;
import lombok.Data;

/**
 * 解析引擎
 *
 */
@Data
public class ChildNode {
    private String type;
    private String content;
    private Properties properties = new Properties();
    private String nodeId;
    private String prevId;
    private ChildNode childNode;
    private String conditionType;
    private List<ChildNode> conditionNodes;
    private Boolean isInterflow;

}
