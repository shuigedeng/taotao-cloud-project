package com.taotao.cloud.order.application.statemachine.cola.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    /**
     * 外部系统审批通过后,可以作废该笔订单
     */
    CANCELLATION(-1, "作废"),

    /**
     * 待提交
     */
    DRAFT(0, "草稿"),


    /**
     * 外部审批中
     */
    AUDITING(2, "审批中"),


    /**
     * 外部系统审批不通过
     */
    APPROVAL_FAIL(3, "退回"),


    /**
     * 核心企业待盖章
     */
    WAIT_SIGN_SEAL(4, "待签约"),


    /**
     * 一般贸易商待确认
     */
    WAIT_CONFIRM(5, "待确认"),

    /**
     * 一般贸易商驳回
     */
    REJECT(6, "驳回"),


    /**
     * 一般贸易商确认完
     */
    EXECUTING(7, "执行中"),

    /**
     * 核心企业可以对该笔订单确认完成
     */
    COMPLETED(8, "已完成"),

    /**
     * 发起终止申请
     */
    FREEZING(9, "冻结中"),

    /**
     * 终止申请状态变为已确认
     */
    TERMINATED(10, "已终止"),

    /**
     * 核心企业可以对该笔订单发起数据完结审核
     */
    WAIT_COMPLETE(11, "完结中"),

    /**
     * 确认待盖章
     */
    CONFIRM_WAIT_SIGN_SEAL(12, "确认待盖章");


    private final int value;
    private final String cnname;



}
