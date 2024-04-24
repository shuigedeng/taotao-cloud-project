package com.taotao.cloud.order.application.statemachine.cola.audit.service;

import com.alibaba.cola.statemachine.Condition;

/**
 * 
 * @date 2023/7/12 17:46
 */
public interface ConditionService {

    /**
     * 通用通过/驳回条件
     * 覆盖审核正向流程，以及驳回流程
     * 已申请->爸爸同意->妈妈统一
     * 已申请->爸爸不同意
     * 爸爸同意->妈妈不同意
     *
     * @return Condition
     */
    Condition<AuditContext> passOrRejectCondition();

    /**
     * 已完成条件
     *
     * @return Condition
     */
    Condition<AuditContext> doneCondition();
}
