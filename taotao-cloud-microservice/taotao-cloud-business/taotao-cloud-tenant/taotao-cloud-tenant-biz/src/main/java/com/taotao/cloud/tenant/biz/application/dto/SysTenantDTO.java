package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户新增/修改DTO
 */
@Data
public class SysTenantDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID（修改时必传）
     */
    private Long id;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 负责人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 租户人员数量上限（0表示无限制）
     */
    private Integer userLimit;

    /**
     * 租户状态（0-禁用，1-正常）
     */
    private Integer tenantStatus;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 租户描述
     */
    private String tenantDesc;
}
