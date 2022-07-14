package com.taotao.cloud.workflow.biz.engine.enums;


/**
 * 流程状态
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月26日 上午9:18
 */
public enum FlowTaskStatusEnum{
    //等待提交
    Draft(0, "等待提交"),
    //等待审核
    Handle(1, "等待审核"),
    //审核通过
    Adopt(2, "审核通过"),
    //审核驳回
    Reject(3, "审核驳回"),
    //审核撤销
    Revoke(4, "审核撤销"),
    //审核作废
    Cancel(5, "审核作废");

    private int code;
    private String message;

    FlowTaskStatusEnum(int code, String message) {
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

    /**
     * 根据状态code获取枚举名称
     *
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (FlowTaskStatusEnum status : FlowTaskStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status.message;
            }
        }
        return null;
    }

    /**
     * 根据状态code获取枚举值
     *
     * @return
     */
    public static FlowTaskStatusEnum getByCode(Integer code) {
        for (FlowTaskStatusEnum status : FlowTaskStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
