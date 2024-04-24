package com.taotao.cloud.order.application.statemachine.cola.audit.pojo.context;

import lombok.Data;

/**
 * 
 * @date 2023/7/12 15:55
 */
@Data
public class AuditContext {

    /**
     * id
     */
    private Long id;

    /**
     * 事件
     */
    private Integer auditEvent;
}
