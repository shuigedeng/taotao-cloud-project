package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * task节点的状态
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
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
