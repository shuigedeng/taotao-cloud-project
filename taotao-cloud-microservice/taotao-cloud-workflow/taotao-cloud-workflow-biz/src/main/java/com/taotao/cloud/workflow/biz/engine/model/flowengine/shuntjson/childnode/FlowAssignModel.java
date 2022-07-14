package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
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
