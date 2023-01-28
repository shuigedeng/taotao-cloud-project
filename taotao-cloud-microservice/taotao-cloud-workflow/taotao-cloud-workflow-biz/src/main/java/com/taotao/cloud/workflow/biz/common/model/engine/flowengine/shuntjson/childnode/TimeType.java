package com.taotao.cloud.workflow.biz.common.model.engine.flowengine.shuntjson.childnode;

/**
 * 解析引擎
 *
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
