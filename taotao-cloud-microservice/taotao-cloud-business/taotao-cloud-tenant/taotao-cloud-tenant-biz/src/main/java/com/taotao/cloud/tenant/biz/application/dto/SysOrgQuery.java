package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOrgQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 组织名称（模糊查询）
     */
    private String orgName;

    /**
     * 父级组织ID
     */
    private Long parentId;

    /**
     * 组织类型（1-公司，2-部门，3-小组）
     */
    private Integer orgType;

    /**
     * 组织状态（0-禁用，1-正常）
     */
    private Integer orgStatus;
}
