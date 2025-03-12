package com.taotao.cloud.order.biz.statemachine.cola.audit.pojo.param;

import lombok.Data;
import lombok.experimental.Accessors;

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
