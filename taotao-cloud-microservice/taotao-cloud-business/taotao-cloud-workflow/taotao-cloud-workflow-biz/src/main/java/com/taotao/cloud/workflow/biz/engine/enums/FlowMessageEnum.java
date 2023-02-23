package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * 消息类型
 *
 */
public enum FlowMessageEnum {
    //发起
    me(1,"me"),
    //待办
    wait(2,"wait"),
    //抄送
    circulate(3,"circulate");

    private String message;
    private int code;

    FlowMessageEnum(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
