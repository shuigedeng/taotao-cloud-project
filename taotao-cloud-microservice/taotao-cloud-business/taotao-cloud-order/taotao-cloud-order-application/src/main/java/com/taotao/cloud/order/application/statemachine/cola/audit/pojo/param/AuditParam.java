package com.taotao.cloud.order.application.statemachine.cola.audit.pojo.param;

import lombok.Data;

/**
 * 
 * @date 2023/7/12 16:13
 */
@Data
public class AuditParam {

    /**
     * id
     */
    private Long id;

    /**
     * 事件
     */
    private Integer auditEvent;
}
