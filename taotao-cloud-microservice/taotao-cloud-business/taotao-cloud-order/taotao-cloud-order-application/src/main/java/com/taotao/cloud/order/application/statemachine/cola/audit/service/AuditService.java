package com.taotao.cloud.order.application.statemachine.cola.audit.service;


/**
 * 
 * @date 2023/7/12 15:53
 */

public interface AuditService {

    /**
     * 状态机执行
     *
     * @param auditContext auditContext
     */
    void audit(AuditContext auditContext);

    /**
     * 获取状态机Uml图
     *
     * @return String
     */
    String uml();

}
