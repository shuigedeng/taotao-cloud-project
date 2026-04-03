package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 是否系统内置角色（0-否，1-是）
     */
    private Integer isSystem;

    /**
     * 备注
     */
    private String remark;
}
