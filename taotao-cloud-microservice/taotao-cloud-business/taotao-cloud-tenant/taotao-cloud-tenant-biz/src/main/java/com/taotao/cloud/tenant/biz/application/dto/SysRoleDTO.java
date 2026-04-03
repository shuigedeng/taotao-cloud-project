package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色新增/修改DTO
 */
@Data
public class SysRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID（修改时必传）
     */
    private Long id;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 角色名称（租户内唯一）
     */
    private String roleName;

    /**
     * 角色权限字符串（如：admin,user:view）
     */
    private String roleKey;

    /**
     * 权限范围（1-全部数据，2-本租户数据，3-本组织数据，4-本组织及子组织，5-个人数据）
     */
    private Integer dataScope;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 角色状态（0-禁用，1-正常）
     */
    private Integer roleStatus;

    /**
     * 备注
     */
    private String remark;
}
