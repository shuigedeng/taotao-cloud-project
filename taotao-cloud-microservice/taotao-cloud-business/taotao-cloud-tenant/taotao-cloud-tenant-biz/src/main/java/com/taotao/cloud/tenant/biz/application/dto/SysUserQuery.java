package com.taotao.cloud.tenant.biz.application.dto;

import com.mdframe.forge.starter.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 用户真实姓名（模糊查询）
     */
    private String realName;

    /**
     * 用户类型（0-系统管理员，1-租户管理员，2-普通用户）
     */
    private Integer userType;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户状态（0-禁用，1-正常，2-锁定）
     */
    private Integer userStatus;

    /**
     * 创建部门
     */
    private Long createDept;
}
