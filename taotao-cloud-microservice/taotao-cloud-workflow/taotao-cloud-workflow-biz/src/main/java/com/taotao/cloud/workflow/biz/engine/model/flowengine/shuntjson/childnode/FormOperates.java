package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:12
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
