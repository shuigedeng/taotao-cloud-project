package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTenantQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户名称（模糊查询）
     */
    private String tenantName;

    /**
     * 负责人（模糊查询）
     */
    private String contactPerson;

    /**
     * 租户状态（0-禁用，1-正常）
     */
    private Integer tenantStatus;
}
