package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 */
@Data
public class FormOperates {
    /**可读**/
    private boolean read;
    /**名称**/
    private String name;
    /**字段**/
    private String id;
    /**可写**/
    private boolean write;
    /**必填**/
    private boolean required;
}
