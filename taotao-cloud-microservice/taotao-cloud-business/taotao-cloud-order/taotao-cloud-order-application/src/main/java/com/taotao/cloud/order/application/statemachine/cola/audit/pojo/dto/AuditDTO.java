package com.taotao.cloud.order.application.statemachine.cola.audit.pojo.dto;

import lombok.Data;

/**
 * 
 * @date 2023/7/12 16:22
 */
@Data
public class AuditDTO {

    private Long id;

    private String auditState;
}
