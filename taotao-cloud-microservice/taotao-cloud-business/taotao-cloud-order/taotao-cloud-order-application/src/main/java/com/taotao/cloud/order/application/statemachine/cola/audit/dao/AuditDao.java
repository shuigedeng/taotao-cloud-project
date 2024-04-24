package com.taotao.cloud.order.application.statemachine.cola.audit.dao;


/**
 * 
 * @date 2023/7/12 16:42
 */
public interface AuditDao {

    AuditDTO selectById(Long id);

    void updateAuditStatus(String auditStatus, Long id);
}
