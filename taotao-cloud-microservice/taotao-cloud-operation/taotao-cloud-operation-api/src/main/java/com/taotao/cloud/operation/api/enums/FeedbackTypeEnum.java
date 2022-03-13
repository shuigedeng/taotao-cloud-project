package com.taotao.cloud.operation.api.enums;

/**
 * 功能反馈枚举
 */
public enum FeedbackTypeEnum {

    /**
     * 功能建议
     */
    FUNCTION,

    /**
     * 优化反馈
     */
    OPTIMIZE ,

    /**
     * 其他意见
     */
    OTHER;

    public String value() {
        return this.name();
    }

}
