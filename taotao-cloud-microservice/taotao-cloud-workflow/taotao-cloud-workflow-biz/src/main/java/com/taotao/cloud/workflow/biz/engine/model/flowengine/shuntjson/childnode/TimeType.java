package com.taotao.cloud.workflow.biz.engine.model.flowengine.shuntjson.childnode;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 */
public enum TimeType {

    /**
     * 天
     */
    Day("day"),
    /**
     * 小时
     */
    Hour("hour"),
    /**
     * 秒
     */
    Minute("minute");

    private String message;

    TimeType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
