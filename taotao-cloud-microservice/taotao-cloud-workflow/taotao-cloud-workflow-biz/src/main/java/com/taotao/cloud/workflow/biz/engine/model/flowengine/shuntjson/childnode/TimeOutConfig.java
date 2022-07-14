package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

import lombok.Data;

/**
 * 解析引擎
 *
 */
@Data
public class TimeOutConfig {
    /**开关**/
    private Boolean on = false;
    /**数量**/
    private Integer quantity;
    /**类型 day、 hour、 minute**/
    private String type;
    /**同意1 拒绝2**/
    private Integer handler;
}
