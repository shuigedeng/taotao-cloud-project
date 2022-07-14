package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 */
@Data
public class FlowAssignModel {
    /**
     * 父字段
     **/
    private String parentField;
    /**
     * 子字段
     **/
    private String childField;
}
