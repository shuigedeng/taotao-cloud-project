package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * 提交状态
 *
 */
public enum FlowStatusEnum {
    //保存
    save("1"),
    // 提交
    submit("0");

    private String message;

    FlowStatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
