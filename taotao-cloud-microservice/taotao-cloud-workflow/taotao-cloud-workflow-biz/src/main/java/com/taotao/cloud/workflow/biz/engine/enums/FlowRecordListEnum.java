package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * 工作流开发
 *
 * @author JNPF开发平台组
 * @version V3.0.0
 * @copyright 引迈信息技术有限公司
 */
public enum FlowRecordListEnum {

    //部门
    department("1","部门"),
    // 角色
    role("2","角色"),
    //岗位
    position("3","岗位");

    private String code;
    private String message;

    FlowRecordListEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
