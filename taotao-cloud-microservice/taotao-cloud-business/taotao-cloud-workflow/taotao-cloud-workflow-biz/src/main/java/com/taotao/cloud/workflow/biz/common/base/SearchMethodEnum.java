package com.taotao.cloud.workflow.biz.common.base;

/**
 * 查询功能
 *
 */
public enum SearchMethodEnum {
    /**
     * like
     */
    Contains("Contains"),
    /**
     * 等于
     */
    Equal("Equal"),
    /**
     * 不等于
     */
    NotEqual("NotEqual"),
    /**
     * 小于
     */
    LessThan("LessThan"),
    /**
     * 小于等于
     */
    LessThanOrEqual("LessThanOrEqual"),
    /**
     * 大于
     */
    GreaterThan("GreaterThan"),
    /**
     * 大于等于
     */
    GreaterThanOrEqual("GreaterThanOrEqual");

    private String message;

    SearchMethodEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
