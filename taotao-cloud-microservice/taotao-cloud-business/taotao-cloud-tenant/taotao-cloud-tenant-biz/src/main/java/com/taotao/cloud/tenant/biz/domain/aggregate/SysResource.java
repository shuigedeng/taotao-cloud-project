package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统资源表（菜单/按钮/API）实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_resource")
public class SysResource extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 父级资源ID（0为顶级）
     */
    private Long parentId;

    /**
     * 资源类型（1-目录，2-菜单，3-按钮，4-API接口）
     */
    private Integer resourceType;

    /**
     * 排序（值越小越靠前）
     */
    private Integer sort;

    /**
     * 资源路由（菜单/目录用）
     */
    private String path;

    /**
     * 前端组件路径（菜单用）
     */
    private String component;

    /**
     * 是否外链（0-否，1-是）
     */
    private Integer isExternal;

    /**
     * 是否公开资源（0-否，1-是，公开资源无需权限验证）
     */
    private Integer isPublic;

    /**
     * 菜单状态（0-隐藏，1-显示，仅菜单/目录用）
     */
    private Integer menuStatus;

    /**
     * 显示状态（0-隐藏，1-显示，所有资源通用）
     */
    private Integer visible;

    /**
     * 权限标识（如：sys:user:list，按钮/API用）
     */
    private String perms;

    /**
     * 图标（菜单/目录用）
     */
    private String icon;

    /**
     * API请求方法（GET/POST/PUT/DELETE，仅API用）
     */
    private String apiMethod;

    /**
     * API接口地址（仅API用）
     */
    private String apiUrl;

    /**
     * 是否缓存（0-否，1-是，菜单用）
     */
    private Integer keepAlive;

    /**
     * 是否总是显示（0-否，1-是，菜单用）
     */
    private Integer alwaysShow;

    /**
     * 重定向地址（菜单用）
     */
    private String redirect;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子资源列表（非数据库字段，用于树形结构）
     */
    @TableField(exist = false)
    private java.util.List<SysResource> children;
}
