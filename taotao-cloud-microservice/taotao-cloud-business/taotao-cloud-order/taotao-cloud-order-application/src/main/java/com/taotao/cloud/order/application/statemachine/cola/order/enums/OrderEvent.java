package com.taotao.cloud.order.application.statemachine.cola.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单事件
 */
@Getter
@AllArgsConstructor
public enum OrderEvent {

    SAVE("保存"),
    SUBMIT("提交"),
    AUDIT_PASS("审核通过"),
    AUDIT_REJECT("审核驳回"),
    SEAL_SUCCEED("盖章成功"),
    SEAL_REJECT("盖章驳回"),
    CONFIRM_SUCCEED("确认成功"),
    CONFIRM_REJECT("确认驳回"),
    CONFIRM_SEAL_SUCCEED("确认待盖章成功"),
    CONFIRM_SEAL_FAIL("确认待盖章失败"),
    CANCELLATION("作废"),
    TERMINATION("发起终止"),
    TERMINATION_AUDIT_PASS("终止审核通过"),
    TERMINATION_AUDIT_REJECT("终止审核驳回"),
    COMPLETE("发起完结"),
    COMPLETE_AUDIT_PASS("完结审核通过"),
    COMPLETE_AUDIT_REJECT("完结审核驳回"),
    ;

    private final String desc;


}
