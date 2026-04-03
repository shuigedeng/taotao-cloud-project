package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 角色名称（模糊查询）
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 角色状态（0-禁用，1-正常）
     */
    private Integer roleStatus;
}
