package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * task节点的状态
 */
public enum FlowHandleEventEnum {
    //审核
    Audit("Audit"),
    //驳回
    Reject("Reject"),
    //撤回
    Recall("Recall"),
    //终止
    Cancel("Cancel");

    private String message;

    FlowHandleEventEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
