package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * 工作流开发
 */
public enum FlowRecordEnum {

    //拒绝
    reject(0, "拒绝"),
    //同意
    audit(1, "同意"),
    //提交
    submit(2, "提交"),
    //撤回
    revoke(3, "撤回"),
    //终止
    cancel(4, "终止"),
    //指派
    assign(5, "指派"),
    //加签
    copyId(6, "加签"),
    //转办
    transfer(7, "转办"),
    //结束
    end(100, "结束"),
    //节点撤回
    recall(-1, "节点撤回");

    private Integer code;
    private String message;

    FlowRecordEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
